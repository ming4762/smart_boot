package com.gc.starter.file.nfs.provider;

import com.jcraft.jsch.Channel;

/**
 * JcraftChannel 提供器
 * @author ShiZhongMing
 * 2020/12/7 17:55
 * @since 1.0
 */
public interface JcraftChannelProvider<T extends Channel> {

    T getChannel();

    void returnChannel(T channel);
}
