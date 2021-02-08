package com.gc.common.i18n.reader;

import java.util.Set;

/**
 * @author ShiZhongMing
 * 2021/2/7 14:13
 * @since 1.0
 */
public interface BasenameResourceReader extends ResourceReader {

    /**
     * 设置 basename
     * @param basenameList basename列表
     */
    void setBasename(Set<String> basenameList);

    /**
     * 添加 basename
     * @param basenameList basename列表
     */
    void addBasename(Set<String> basenameList);

    /**
     * 获取加载资源的ClassLoader
     * @return ClassLoader
     */
    ClassLoader getClassLoader();

    /**
     * 设置加载资源的 ClassLoader
     * @param classLoader ClassLoader
     */
    void setClassLoader(ClassLoader classLoader);
}
