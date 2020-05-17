package com.gc.common.base.io.support;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.List;

/**
 * yaml加载器
 * @author shizhongming
 * 2020/5/14 4:00 下午
 */
public class YamlPropertyLoaderFactory extends DefaultPropertySourceFactory {

    @Override
    @NonNull
    public PropertySource<?> createPropertySource(@Nullable String name, @NonNull EncodedResource resource) throws IOException {
        List<PropertySource<?>> propertySourceList =  new YamlPropertySourceLoader().load(resource.getResource().getFilename(), resource.getResource());
        if (propertySourceList != null && propertySourceList.size() > 0) {
            return propertySourceList.get(0);
        } else {
            return super.createPropertySource(name, resource);
        }
    }

}
