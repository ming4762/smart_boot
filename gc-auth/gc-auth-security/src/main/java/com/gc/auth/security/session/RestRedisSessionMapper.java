package com.gc.auth.security.session;

import org.springframework.session.MapSession;
import org.springframework.session.Session;
import org.springframework.util.Assert;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.function.Function;

/**
 * @author jackson
 * 2020/1/25 7:46 下午
 */
public class RestRedisSessionMapper implements Function<Map<String, Object>, MapSession> {
    /**
     * The key in the hash representing {@link Session#getCreationTime()}.
     */
    static final String CREATION_TIME_KEY = "creationTime";

    /**
     * The key in the hash representing {@link Session#getLastAccessedTime()}.
     */
    static final String LAST_ACCESSED_TIME_KEY = "lastAccessedTime";

    /**
     * The key in the hash representing {@link Session#getMaxInactiveInterval()}.
     */
    static final String MAX_INACTIVE_INTERVAL_KEY = "maxInactiveInterval";

    /**
     * The prefix of the key in the hash used for session attributes. For example, if the
     * session contained an attribute named {@code attributeName}, then there would be an
     * entry in the hash named {@code sessionAttr:attributeName} that mapped to its value.
     */
    static final String ATTRIBUTE_PREFIX = "sessionAttr:";

    private final String sessionId;

    RestRedisSessionMapper(String sessionId) {
        Assert.hasText(sessionId, "sessionId must not be empty");
        this.sessionId = sessionId;
    }

    @Override
    public MapSession apply(Map<String, Object> map) {
        Assert.notEmpty(map, "map must not be empty");
        MapSession session = new MapSession(this.sessionId);
        Long creationTime = (Long) map.get(CREATION_TIME_KEY);
        if (creationTime == null) {
            handleMissingKey(CREATION_TIME_KEY);
        }
        session.setCreationTime(Instant.ofEpochMilli(creationTime));
        Long lastAccessedTime = (Long) map.get(LAST_ACCESSED_TIME_KEY);
        if (lastAccessedTime == null) {
            handleMissingKey(LAST_ACCESSED_TIME_KEY);
        }
        session.setLastAccessedTime(Instant.ofEpochMilli(lastAccessedTime));
        Integer maxInactiveInterval = (Integer) map.get(MAX_INACTIVE_INTERVAL_KEY);
        if (maxInactiveInterval == null) {
            handleMissingKey(MAX_INACTIVE_INTERVAL_KEY);
        }
        session.setMaxInactiveInterval(Duration.ofSeconds(maxInactiveInterval));
        map.forEach((name, value) -> {
            if (name.startsWith(ATTRIBUTE_PREFIX)) {
                session.setAttribute(name.substring(ATTRIBUTE_PREFIX.length()), value);
            }
        });
        return session;
    }

    private static void handleMissingKey(String key) {
        throw new IllegalStateException(key + " key must not be null");
    }

}
