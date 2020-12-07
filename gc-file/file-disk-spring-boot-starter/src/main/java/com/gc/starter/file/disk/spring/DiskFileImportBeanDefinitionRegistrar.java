package com.gc.starter.file.disk.spring;

import com.gc.file.common.constants.ActualFileServiceConstants;
import com.gc.starter.file.disk.service.ActualFileDiskServiceImpl;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

/**
 * @author shizhongming
 * 2020/12/7 10:36 下午
 */
public class DiskFileImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        final BeanDefinition beanDefinition = new RootBeanDefinition(ActualFileDiskServiceImpl.class);
        registry.registerBeanDefinition(ActualFileServiceConstants.DISK.getServiceName(), beanDefinition);
    }
}
