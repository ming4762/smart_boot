package com.gc.common.jcraft.utils;

import com.gc.common.jcraft.properties.SmartJcraftProperties;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

/**
 * @author ShiZhongMing
 * 2020/12/7 15:26
 * @since 1.0
 */
public class JcraftUtils {

    /**
     * 创建session
     * @param properties 参数
     * @return session
     */
    public static Session createSession(SmartJcraftProperties properties) throws JSchException {
        final JSch jSch = new JSch();
        if (StringUtils.isNotBlank(properties.getPrivateKey())) {
            jSch.addIdentity(properties.getPrivateKey());
        }
        // 创建session
        final Session session = jSch.getSession(properties.getUsername(), properties.getHost(), properties.getPort());
        // todo: 用户信息
        if (StringUtils.isNotBlank(properties.getPassword())) {
            session.setPassword(properties.getPassword());
        }
        // 创建配置
        final Properties config = new Properties();
        // TODO: 待处理
        config.put("StrictHostKeyChecking", "no");
        config.put("PreferredAuthentications", "publickey,keyboard-interactive,password");

        session.setConfig(config);
        session.connect();
        return session;
    }
}
