package com.gc.common.jcraft.constants;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import lombok.Getter;

/**
 * Channel 类型
 * @author ShiZhongMing
 * 2020/12/7 17:43
 * @since 1.0
 */
@Getter
public enum ChannelTypeConstants {

    sftp(ChannelSftp.class);

    private final Class<? extends Channel> channelClass;

    ChannelTypeConstants(Class<? extends Channel> channelClass) {
        this.channelClass = channelClass;
    }
}
