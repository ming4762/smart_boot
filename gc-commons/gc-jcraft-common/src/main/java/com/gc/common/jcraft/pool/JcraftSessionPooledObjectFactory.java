package com.gc.common.jcraft.pool;


import com.gc.common.jcraft.properties.SmartJcraftProperties;
import com.gc.common.jcraft.utils.JcraftUtils;
import com.jcraft.jsch.Session;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.Objects;
import java.util.Optional;

/**
 * Jcraft Session连接池
 * @author ShiZhongMing
 * 2020/12/7 15:36
 * @since 1.0
 */
public class JcraftSessionPooledObjectFactory implements PooledObjectFactory<Session> {

    private final SmartJcraftProperties properties;

    public JcraftSessionPooledObjectFactory(SmartJcraftProperties properties) {
        this.properties = properties;
    }

    @Override
    public PooledObject<Session> makeObject() throws Exception {
        final Session session = JcraftUtils.createSession(this.properties);
        return new DefaultPooledObject<>(session);
    }

    @Override
    public void destroyObject(PooledObject<Session> pooledObject) throws Exception {
        Optional.ofNullable(pooledObject.getObject())
                .ifPresent(Session::disconnect);
    }

    @Override
    public boolean validateObject(PooledObject<Session> pooledObject) {
        final Session session = pooledObject.getObject();
        return Objects.nonNull(session) && session.isConnected();
    }

    @Override
    public void activateObject(PooledObject<Session> pooledObject) throws Exception {
        Session object = pooledObject.getObject();
        if (Objects.nonNull(object) && !object.isConnected()) {
            object.connect();
        }
    }

    @Override
    public void passivateObject(PooledObject<Session> pooledObject) throws Exception {
        Session object = pooledObject.getObject();
        if (Objects.nonNull(object) && object.isConnected()) {
            object.disconnect();
        }
    }
}
