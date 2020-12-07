package com.gc.starter.file.nfs.provider;

import com.gc.common.base.exception.BaseException;
import com.gc.common.jcraft.constants.ChannelTypeConstants;
import com.gc.common.jcraft.pool.ChannelPooledObjectFactory;
import com.gc.common.jcraft.pool.JcraftSessionPooledObjectFactory;
import com.gc.common.jcraft.properties.SmartJcraftProperties;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.InitializingBean;

/**
 * FTP通道提供者
 * @author ShiZhongMing
 * 2020/12/7 17:57
 * @since 1.0
 */
@Slf4j
public class FtpChannelProvider implements JcraftChannelProvider<ChannelSftp>, InitializingBean {

    private GenericObjectPool<ChannelSftp> objectPool;

    private final SmartJcraftProperties properties;

    public FtpChannelProvider(SmartJcraftProperties properties) {
        this.properties = properties;
    }

    @Override
    public ChannelSftp getChannel() {
        if (this.objectPool.getNumWaiters() > 0) {
            log.warn("线程池暂无空闲channel");
            return null;
        }
        try {
            return this.objectPool.borrowObject();
        } catch (Exception e) {
            log.error("从连接池获取channel发生错误", e);
            throw new BaseException("从连接池获取channel发生错误", e);
        }
    }

    @Override
    public void returnChannel(ChannelSftp channel) {
        this.objectPool.returnObject(channel);
    }

    @Override
    public void afterPropertiesSet() {
        // 创建session连接池
        final JcraftSessionPooledObjectFactory sessionPooledObjectFactory = new JcraftSessionPooledObjectFactory(this.properties);
        final GenericObjectPool<Session> sessionObjectPool = new GenericObjectPool<>(sessionPooledObjectFactory);
        // 创建channel连接池
        final ChannelPooledObjectFactory<ChannelSftp> channelPooledObjectFactory = new ChannelPooledObjectFactory<>(sessionObjectPool, ChannelTypeConstants.sftp);
        this.objectPool = new GenericObjectPool<>(channelPooledObjectFactory);
    }
}
