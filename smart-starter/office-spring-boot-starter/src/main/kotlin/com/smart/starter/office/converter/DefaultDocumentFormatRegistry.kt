package com.smart.starter.office.converter

import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry
import com.artofsolving.jodconverter.DocumentFormat
import com.artofsolving.jodconverter.DocumentFormatRegistry

/**
 * DocumentFormat注册类
 * @author zhongming
 */
class DefaultDocumentFormatRegistry {

    companion object {

        private val defaultDocumentFormatRegistry = DefaultDocumentFormatRegistry()

        val PDF = getInstance().getFormatByFileExtension("pdf")
        val DOC = getInstance().getFormatByFileExtension("doc")

        /**
         * 通过扩展名获取
         */
        fun getFormatByFileExtension(extension: String): DocumentFormat {
            return getInstance().getFormatByFileExtension(extension)
        }

        private fun getInstance(): DocumentFormatRegistry {
            return defaultDocumentFormatRegistry
        }
    }

}