package com.smart.file.constant

/**
 * @author zhongming
 * @since 3.0
 * 2018/7/25上午11:07
 */
enum class FileTypeConstants private constructor(val value: String) {

    /**
     * 普通文件
     */
    NORMAL("normal"),
    /**
     * 测试文件
     */
    TEST("test"),

    TEMP("TEMP"),

    APP("AP")
}
