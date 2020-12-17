package com.gc.starter.file.nfs.provider;

import com.jcraft.jsch.Channel;

/**
 * JcraftChannel 提供器
 * @author ShiZhongMing
 * 2020/12/7 17:55
 * @since 1.0
 */
public interface JcraftChannelProvider<T extends Channel> {

    /**
     * 获取Jcraft通道
     * @return Jcraft通道
     */
    T getChannel();

    /**
     * 归还通道
     * @param channel 通道
     */
    void returnChannel(T channel);
}
