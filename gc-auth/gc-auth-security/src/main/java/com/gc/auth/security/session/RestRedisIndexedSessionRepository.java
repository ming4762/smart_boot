package com.gc.auth.security.session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.*;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.events.SessionCreatedEvent;
import org.springframework.session.events.SessionDeletedEvent;
import org.springframework.session.events.SessionDestroyedEvent;
import org.springframework.session.events.SessionExpiredEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author jackson
 * 2020/1/25 5:10 下午
 */
@Component
@Primary
public class RestRedisIndexedSessionRepository implements FindByIndexNameSessionRepository<RestRedisIndexedSessionRepository.RestRedisSession>, MessageListener {

    private static final Log logger = LogFactory.getLog(RedisIndexedSessionRepository.class);

    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    /**
     * The default Redis database used by Spring Session.
     */
    public static final int DEFAULT_DATABASE = 0;

    /**
     * The default namespace for each key and channel in Redis used by Spring Session.
     */
    public static final String DEFAULT_NAMESPACE = "spring:session";

    private int database = DEFAULT_DATABASE;

    /**
     * The namespace for every key used by Spring Session in Redis.
     */
    private String namespace = DEFAULT_NAMESPACE + ":";

    private String sessionCreatedChannelPrefix;

    private String sessionDeletedChannel;

    private String sessionExpiredChannel;

    private final RedisOperations<Object, Object> sessionRedisOperations;

    private final RedisSessionExpirationPolicy expirationPolicy;

    private ApplicationEventPublisher eventPublisher = (event) -> {
    };

    /**
     * If non-null, this value is used to override the default value for
     */
    private Integer defaultMaxInactiveInterval;

    private IndexResolver<Session> indexResolver = new DelegatingIndexResolver<>(new PrincipalNameIndexResolver<>());

    private RedisSerializer<Object> defaultSerializer = new JdkSerializationRedisSerializer();

    private FlushMode flushMode = FlushMode.ON_SAVE;

    private SaveMode saveMode = SaveMode.ON_SET_ATTRIBUTE;

    /**
     * Creates a new instance. For an example, refer to the class level javadoc.
     * @param sessionRedisOperations the {@link RedisOperations} to use for managing the
     * sessions. Cannot be null.
     */
    public RestRedisIndexedSessionRepository(RedisOperations<Object, Object> sessionRedisOperations) {
        Assert.notNull(sessionRedisOperations, "sessionRedisOperations cannot be null");
        this.sessionRedisOperations = sessionRedisOperations;
        this.expirationPolicy = new RedisSessionExpirationPolicy(sessionRedisOperations, this::getExpirationsKey,
                this::getSessionKey);
        configureSessionChannels();
    }

    /**
     * Sets the {@link ApplicationEventPublisher} that is used to publish
     * {@link SessionDestroyedEvent}. The default is to not publish a
     * {@link SessionDestroyedEvent}.
     * @param applicationEventPublisher the {@link ApplicationEventPublisher} that is used
     * to publish {@link SessionDestroyedEvent}. Cannot be null.
     */
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        Assert.notNull(applicationEventPublisher, "applicationEventPublisher cannot be null");
        this.eventPublisher = applicationEventPublisher;
    }

    /**
     * Sets the maximum inactive interval in seconds between requests before newly created
     * sessions will be invalidated. A negative time indicates that the session will never
     * timeout. The default is 1800 (30 minutes).
     * @param defaultMaxInactiveInterval the number of seconds that the {@link Session}
     * should be kept alive between client requests.
     */
    public void setDefaultMaxInactiveInterval(int defaultMaxInactiveInterval) {
        this.defaultMaxInactiveInterval = defaultMaxInactiveInterval;
    }

    /**
     * Set the {@link IndexResolver} to use.
     * @param indexResolver the index resolver
     */
    public void setIndexResolver(IndexResolver<Session> indexResolver) {
        Assert.notNull(indexResolver, "indexResolver cannot be null");
        this.indexResolver = indexResolver;
    }

    /**
     * Sets the default redis serializer. Replaces default serializer which is based on
     * {@link JdkSerializationRedisSerializer}.
     * @param defaultSerializer the new default redis serializer
     */
    public void setDefaultSerializer(RedisSerializer<Object> defaultSerializer) {
        Assert.notNull(defaultSerializer, "defaultSerializer cannot be null");
        this.defaultSerializer = defaultSerializer;
    }

    /**
     * Sets the redis flush mode. Default flush mode is {@link FlushMode#ON_SAVE}.
     * @param flushMode the flush mode
     */
    public void setFlushMode(FlushMode flushMode) {
        Assert.notNull(flushMode, "flushMode cannot be null");
        this.flushMode = flushMode;
    }

    /**
     * Set the save mode.
     * @param saveMode the save mode
     */
    public void setSaveMode(SaveMode saveMode) {
        Assert.notNull(saveMode, "saveMode must not be null");
        this.saveMode = saveMode;
    }

    /**
     * Sets the database index to use. Defaults to {@link #DEFAULT_DATABASE}.
     * @param database the database index to use
     */
    public void setDatabase(int database) {
        this.database = database;
        configureSessionChannels();
    }

    private void configureSessionChannels() {
        this.sessionCreatedChannelPrefix = this.namespace + "event:" + this.database + ":created:";
        this.sessionDeletedChannel = "__keyevent@" + this.database + "__:del";
        this.sessionExpiredChannel = "__keyevent@" + this.database + "__:expired";
    }

    /**
     * Returns the {@link RedisOperations} used for sessions.
     * @return the {@link RedisOperations} used for sessions
     */
    public RedisOperations<Object, Object> getSessionRedisOperations() {
        return this.sessionRedisOperations;
    }

    @Override
    public void save(RestRedisSession session) {
        session.save();
        if (session.isNew) {
            String sessionCreatedKey = getSessionCreatedChannel(session.getId());
            this.sessionRedisOperations.convertAndSend(sessionCreatedKey, session.delta);
            session.isNew = false;
        }
    }

    public void cleanupExpiredSessions() {
        this.expirationPolicy.cleanExpiredSessions();
    }

    @Override
    public RestRedisSession findById(String id) {
        return getSession(id, false);
    }

    @Override
    public Map<String, RestRedisIndexedSessionRepository.RestRedisSession> findByIndexNameAndIndexValue(String indexName, String indexValue) {
        if (!PRINCIPAL_NAME_INDEX_NAME.equals(indexName)) {
            return Collections.emptyMap();
        }
        String principalKey = getPrincipalKey(indexValue);
        Set<Object> sessionIds = this.sessionRedisOperations.boundSetOps(principalKey).members();
        Map<String, RestRedisIndexedSessionRepository.RestRedisSession> sessions = new HashMap<>(sessionIds.size());
        for (Object id : sessionIds) {
            RestRedisIndexedSessionRepository.RestRedisSession session = findById((String) id);
            if (session != null) {
                sessions.put(session.getId(), session);
            }
        }
        return sessions;
    }

    /**
     * Gets the session.
     * @param id the session id
     * @param allowExpired if true, will also include expired sessions that have not been
     * deleted. If false, will ensure expired sessions are not returned.
     * @return the Redis session
     */
    private RestRedisIndexedSessionRepository.RestRedisSession getSession(String id, boolean allowExpired) {
        Map<Object, Object> entries = getSessionBoundHashOperations(id).entries();
        if (entries.isEmpty()) {
            return null;
        }
        MapSession loaded = loadSession(id, entries);
        if (!allowExpired && loaded.isExpired()) {
            return null;
        }
        RestRedisIndexedSessionRepository.RestRedisSession result = new RestRedisIndexedSessionRepository.RestRedisSession(loaded, false);
        result.originalLastAccessTime = loaded.getLastAccessedTime();
        return result;
    }

    private MapSession loadSession(String id, Map<Object, Object> entries) {
        MapSession loaded = new MapSession(id);
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            String key = (String) entry.getKey();
            if (RestRedisSessionMapper.CREATION_TIME_KEY.equals(key)) {
                loaded.setCreationTime(Instant.ofEpochMilli((long) entry.getValue()));
            }
            // TODO session有效时间
            else if (RestRedisSessionMapper.MAX_INACTIVE_INTERVAL_KEY.equals(key)) {
                loaded.setMaxInactiveInterval(Duration.ofSeconds((int) entry.getValue()));
            }
            else if (RestRedisSessionMapper.LAST_ACCESSED_TIME_KEY.equals(key)) {
                loaded.setLastAccessedTime(Instant.ofEpochMilli((long) entry.getValue()));
            }
            else if (key.startsWith(RestRedisSessionMapper.ATTRIBUTE_PREFIX)) {
                loaded.setAttribute(key.substring(RestRedisSessionMapper.ATTRIBUTE_PREFIX.length()), entry.getValue());
            }
        }
        return loaded;
    }

    @Override
    public void deleteById(String sessionId) {
        RestRedisIndexedSessionRepository.RestRedisSession session = getSession(sessionId, true);
        if (session == null) {
            return;
        }

        cleanupPrincipalIndex(session);
        this.expirationPolicy.onDelete(session);

        String expireKey = getExpiredKey(session.getId());
        this.sessionRedisOperations.delete(expireKey);

        session.setMaxInactiveInterval(Duration.ZERO);
        save(session);
    }

    /**
     * TODO: 待完善
     * 重写该方法设置session的有效期
     * @return
     */
    @Override
    public RestRedisIndexedSessionRepository.RestRedisSession createSession() {
        MapSession cached = new MapSession();
        if (this.defaultMaxInactiveInterval != null) {
            cached.setMaxInactiveInterval(Duration.ofSeconds(this.defaultMaxInactiveInterval));
        }
        RestRedisIndexedSessionRepository.RestRedisSession session = new RestRedisIndexedSessionRepository.RestRedisSession(cached, true);
        session.flushImmediateIfNecessary();
        return session;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] messageChannel = message.getChannel();
        byte[] messageBody = message.getBody();

        String channel = new String(messageChannel);

        if (channel.startsWith(this.sessionCreatedChannelPrefix)) {
            // TODO: is this thread safe?
            @SuppressWarnings("unchecked")
            Map<Object, Object> loaded = (Map<Object, Object>) this.defaultSerializer.deserialize(message.getBody());
            handleCreated(loaded, channel);
            return;
        }

        String body = new String(messageBody);
        if (!body.startsWith(getExpiredKeyPrefix())) {
            return;
        }

        boolean isDeleted = channel.equals(this.sessionDeletedChannel);
        if (isDeleted || channel.equals(this.sessionExpiredChannel)) {
            int beginIndex = body.lastIndexOf(":") + 1;
            int endIndex = body.length();
            String sessionId = body.substring(beginIndex, endIndex);

            RestRedisIndexedSessionRepository.RestRedisSession session = getSession(sessionId, true);

            if (session == null) {
                logger.warn("Unable to publish SessionDestroyedEvent for session " + sessionId);
                return;
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Publishing SessionDestroyedEvent for session " + sessionId);
            }

            cleanupPrincipalIndex(session);

            if (isDeleted) {
                handleDeleted(session);
            }
            else {
                handleExpired(session);
            }
        }
    }

    private void cleanupPrincipalIndex(RestRedisIndexedSessionRepository.RestRedisSession session) {
        String sessionId = session.getId();
        Map<String, String> indexes = RestRedisIndexedSessionRepository.this.indexResolver.resolveIndexesFor(session);
        String principal = indexes.get(PRINCIPAL_NAME_INDEX_NAME);
        if (principal != null) {
            this.sessionRedisOperations.boundSetOps(getPrincipalKey(principal)).remove(sessionId);
        }
    }

    private void handleCreated(Map<Object, Object> loaded, String channel) {
        String id = channel.substring(channel.lastIndexOf(":") + 1);
        Session session = loadSession(id, loaded);
        publishEvent(new SessionCreatedEvent(this, session));
    }

    private void handleDeleted(RestRedisIndexedSessionRepository.RestRedisSession session) {
        publishEvent(new SessionDeletedEvent(this, session));
    }

    private void handleExpired(RestRedisIndexedSessionRepository.RestRedisSession session) {
        publishEvent(new SessionExpiredEvent(this, session));
    }

    private void publishEvent(ApplicationEvent event) {
        try {
            this.eventPublisher.publishEvent(event);
        }
        catch (Throwable ex) {
            logger.error("Error publishing " + event + ".", ex);
        }
    }

    public void setRedisKeyNamespace(String namespace) {
        Assert.hasText(namespace, "namespace cannot be null or empty");
        this.namespace = namespace.trim() + ":";
        configureSessionChannels();
    }

    /**
     * Gets the Hash key for this session by prefixing it appropriately.
     * @param sessionId the session id
     * @return the Hash key for this session by prefixing it appropriately.
     */
    String getSessionKey(String sessionId) {
        return this.namespace + "sessions:" + sessionId;
    }

    String getPrincipalKey(String principalName) {
        return this.namespace + "index:" + FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME + ":"
                + principalName;
    }

    String getExpirationsKey(long expiration) {
        return this.namespace + "expirations:" + expiration;
    }

    private String getExpiredKey(String sessionId) {
        return getExpiredKeyPrefix() + sessionId;
    }

    private String getSessionCreatedChannel(String sessionId) {
        return getSessionCreatedChannelPrefix() + sessionId;
    }

    private String getExpiredKeyPrefix() {
        return this.namespace + "sessions:expires:";
    }

    /**
     * Gets the prefix for the channel that {@link SessionCreatedEvent}s are published to.
     * The suffix is the session id of the session that was created.
     * @return the prefix for the channel that {@link SessionCreatedEvent}s are published
     * to
     */
    public String getSessionCreatedChannelPrefix() {
        return this.sessionCreatedChannelPrefix;
    }

    /**
     * Gets the name of the channel that {@link SessionDeletedEvent}s are published to.
     * @return the name for the channel that {@link SessionDeletedEvent}s are published to
     */
    public String getSessionDeletedChannel() {
        return this.sessionDeletedChannel;
    }

    /**
     * Gets the name of the channel that {@link SessionExpiredEvent}s are published to.
     * @return the name for the channel that {@link SessionExpiredEvent}s are published to
     */
    public String getSessionExpiredChannel() {
        return this.sessionExpiredChannel;
    }

    /**
     * Gets the {@link BoundHashOperations} to operate on a {@link Session}.
     * @param sessionId the id of the {@link Session} to work with
     * @return the {@link BoundHashOperations} to operate on a {@link Session}
     */
    private BoundHashOperations<Object, Object, Object> getSessionBoundHashOperations(String sessionId) {
        String key = getSessionKey(sessionId);
        return this.sessionRedisOperations.boundHashOps(key);
    }

    /**
     * Gets the key for the specified session attribute.
     * @param attributeName the attribute name
     * @return the attribute key name
     */
    static String getSessionAttrNameKey(String attributeName) {
        return RestRedisSessionMapper.ATTRIBUTE_PREFIX + attributeName;
    }

    public class RestRedisSession implements Session {

        private final MapSession cached;

        private Instant originalLastAccessTime;

        private Map<String, Object> delta = new HashMap<>();

        private boolean isNew;

        private String originalPrincipalName;

        private String originalSessionId;

        RestRedisSession(MapSession cached, boolean isNew) {
            this.cached = cached;
            this.isNew = isNew;
            this.originalSessionId = cached.getId();
            Map<String, String> indexes = RestRedisIndexedSessionRepository.this.indexResolver.resolveIndexesFor(this);
            this.originalPrincipalName = indexes.get(PRINCIPAL_NAME_INDEX_NAME);
            if (this.isNew) {
                this.delta.put(RestRedisSessionMapper.CREATION_TIME_KEY, cached.getCreationTime().toEpochMilli());
                this.delta.put(RestRedisSessionMapper.MAX_INACTIVE_INTERVAL_KEY,
                        (int) cached.getMaxInactiveInterval().getSeconds());
                this.delta.put(RestRedisSessionMapper.LAST_ACCESSED_TIME_KEY, cached.getLastAccessedTime().toEpochMilli());
            }
            if (this.isNew || (RestRedisIndexedSessionRepository.this.saveMode == SaveMode.ALWAYS)) {
                getAttributeNames().forEach((attributeName) -> this.delta.put(getSessionAttrNameKey(attributeName),
                        cached.getAttribute(attributeName)));
            }
        }

        @Override
        public void setLastAccessedTime(Instant lastAccessedTime) {
            this.cached.setLastAccessedTime(lastAccessedTime);
            this.delta.put(RestRedisSessionMapper.LAST_ACCESSED_TIME_KEY, getLastAccessedTime().toEpochMilli());
            flushImmediateIfNecessary();
        }

        @Override
        public boolean isExpired() {
            return this.cached.isExpired();
        }

        @Override
        public Instant getCreationTime() {
            return this.cached.getCreationTime();
        }

        @Override
        public String getId() {
            return this.cached.getId();
        }

        @Override
        public String changeSessionId() {
            return this.cached.changeSessionId();
        }

        @Override
        public Instant getLastAccessedTime() {
            return this.cached.getLastAccessedTime();
        }

        @Override
        public void setMaxInactiveInterval(Duration interval) {
            this.cached.setMaxInactiveInterval(interval);
            this.delta.put(RestRedisSessionMapper.MAX_INACTIVE_INTERVAL_KEY, (int) getMaxInactiveInterval().getSeconds());
            flushImmediateIfNecessary();
        }

        @Override
        public Duration getMaxInactiveInterval() {
            return this.cached.getMaxInactiveInterval();
        }

        @Override
        public <T> T getAttribute(String attributeName) {
            T attributeValue = this.cached.getAttribute(attributeName);
            if (attributeValue != null
                    && RestRedisIndexedSessionRepository.this.saveMode.equals(SaveMode.ON_GET_ATTRIBUTE)) {
                this.delta.put(getSessionAttrNameKey(attributeName), attributeValue);
            }
            return attributeValue;
        }

        @Override
        public Set<String> getAttributeNames() {
            return this.cached.getAttributeNames();
        }

        @Override
        public void setAttribute(String attributeName, Object attributeValue) {
            this.cached.setAttribute(attributeName, attributeValue);
            this.delta.put(getSessionAttrNameKey(attributeName), attributeValue);
            flushImmediateIfNecessary();
        }

        @Override
        public void removeAttribute(String attributeName) {
            this.cached.removeAttribute(attributeName);
            this.delta.put(getSessionAttrNameKey(attributeName), null);
            flushImmediateIfNecessary();
        }

        private void flushImmediateIfNecessary() {
            if (RestRedisIndexedSessionRepository.this.flushMode == FlushMode.IMMEDIATE) {
                save();
            }
        }

        private void save() {
            saveChangeSessionId();
            saveDelta();
        }

        /**
         * Saves any attributes that have been changed and updates the expiration of this
         * session.
         */
        private void saveDelta() {
            if (this.delta.isEmpty()) {
                return;
            }
            String sessionId = getId();
            getSessionBoundHashOperations(sessionId).putAll(this.delta);
            String principalSessionKey = getSessionAttrNameKey(
                    FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME);
            String securityPrincipalSessionKey = getSessionAttrNameKey(SPRING_SECURITY_CONTEXT);
            if (this.delta.containsKey(principalSessionKey) || this.delta.containsKey(securityPrincipalSessionKey)) {
                if (this.originalPrincipalName != null) {
                    String originalPrincipalRedisKey = getPrincipalKey(this.originalPrincipalName);
                    RestRedisIndexedSessionRepository.this.sessionRedisOperations.boundSetOps(originalPrincipalRedisKey)
                            .remove(sessionId);
                }
                Map<String, String> indexes = RestRedisIndexedSessionRepository.this.indexResolver.resolveIndexesFor(this);
                String principal = indexes.get(PRINCIPAL_NAME_INDEX_NAME);
                this.originalPrincipalName = principal;
                if (principal != null) {
                    String principalRedisKey = getPrincipalKey(principal);
                    RestRedisIndexedSessionRepository.this.sessionRedisOperations.boundSetOps(principalRedisKey)
                            .add(sessionId);
                }
            }

            this.delta = new HashMap<>(this.delta.size());

            Long originalExpiration = (this.originalLastAccessTime != null)
                    ? this.originalLastAccessTime.plus(getMaxInactiveInterval()).toEpochMilli() : null;
            RestRedisIndexedSessionRepository.this.expirationPolicy.onExpirationUpdated(originalExpiration, this);
        }

        private void saveChangeSessionId() {
            String sessionId = getId();
            if (sessionId.equals(this.originalSessionId)) {
                return;
            }
            if (!this.isNew) {
                String originalSessionIdKey = getSessionKey(this.originalSessionId);
                String sessionIdKey = getSessionKey(sessionId);
                try {
                    RestRedisIndexedSessionRepository.this.sessionRedisOperations.rename(originalSessionIdKey,
                            sessionIdKey);
                }
                catch (NonTransientDataAccessException ex) {
                    handleErrNoSuchKeyError(ex);
                }
                String originalExpiredKey = getExpiredKey(this.originalSessionId);
                String expiredKey = getExpiredKey(sessionId);
                try {
                    RestRedisIndexedSessionRepository.this.sessionRedisOperations.rename(originalExpiredKey, expiredKey);
                }
                catch (NonTransientDataAccessException ex) {
                    handleErrNoSuchKeyError(ex);
                }
            }
            this.originalSessionId = sessionId;
        }

        private void handleErrNoSuchKeyError(NonTransientDataAccessException ex) {
            if (!"ERR no such key".equals(NestedExceptionUtils.getMostSpecificCause(ex).getMessage())) {
                throw ex;
            }
        }
    }
}
