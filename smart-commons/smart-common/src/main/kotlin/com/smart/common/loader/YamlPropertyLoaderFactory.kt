package com.smart.common.loader

import org.springframework.boot.env.YamlPropertySourceLoader
import org.springframework.core.env.PropertySource
import org.springframework.core.io.support.DefaultPropertySourceFactory
import org.springframework.core.io.support.EncodedResource



/**
 * 自定义yml加载类
 * @author ming
 * 2019/8/13 上午9:16
 */
class YamlPropertyLoaderFactory : DefaultPropertySourceFactory() {

    override fun createPropertySource(name: String?, resource: EncodedResource): PropertySource<*> {
        val propertySourceList = YamlPropertySourceLoader().load(resource.resource.filename, resource.resource)
        return if (propertySourceList != null && propertySourceList.size > 0) {
            propertySourceList[0]
        } else {
            super.createPropertySource(name, resource)
        }
    }
}