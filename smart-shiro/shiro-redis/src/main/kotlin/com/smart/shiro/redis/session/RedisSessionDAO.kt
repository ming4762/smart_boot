package com.smart.shiro.redis.session

import com.smart.starter.redis.service.RedisService
import org.apache.shiro.session.Session
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO
import org.slf4j.LoggerFactory
import java.io.Serializable

/**
 * shiro redis session dao
 * @author ming
 * 2019/6/22 下午6:59
 */
class RedisSessionDAO : EnterpriseCacheSessionDAO {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(this :: class.java)
    }

    var keyPrefix = "shiro_redis_session:"

    private var redisService: RedisService

    constructor(redisService: RedisService) {
        this.redisService = redisService
    }

    constructor(redisService: RedisService, keyPrefix: String): this(redisService) {
        this.keyPrefix = keyPrefix
    }

    override fun doCreate(session: Session): Serializable {
        val sessionId = super.doCreate(session)
        // 保存session
//        this.redisService.put(this.prefixKey(sessionId), session)
        return sessionId
    }

    /**
     * 重写读取session 从redis获取
     */
    override fun doReadSession(sessionId: Serializable?): Session? {
        if (sessionId == null) {
            LOGGER.warn("session id is null")
            return null
        }
        return this.redisService.get(this.prefixKey(sessionId), Session :: class.java)
    }

    /**
     * 更新session
     */
    override fun doUpdate(session: Session?) {
        super.doUpdate(session)
        if (session != null) {
            this.redisService.put(this.prefixKey(session.id), session)
        }
    }

    /**
     * 删除session
     */
    override fun doDelete(session: Session?) {
        super.doDelete(session)
        if (session != null) {
            this.redisService.delete(this.prefixKey(session.id))
        }
    }

    private fun prefixKey(key: Any): String {
        return this.keyPrefix + key.toString()
    }
}