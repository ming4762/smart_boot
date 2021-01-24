package com.gc.auth.extensions.saml2.metadata.provider;

import lombok.SneakyThrows;
import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.opensaml.saml2.metadata.provider.AbstractReloadingMetadataProvider;
import org.springframework.core.io.ClassPathResource;

/**
 * @author ShiZhongMing
 * 2021/1/15 14:13
 * @since 1.0
 */
public class JarClassPathMetadataProvider extends AbstractReloadingMetadataProvider {

    private final ClassPathResource classPathResource;

    public JarClassPathMetadataProvider(ClassPathResource classPathResource) {
        super();
        this.classPathResource = classPathResource;
    }


    @Override
    protected String getMetadataIdentifier() {
        return this.classPathResource.getPath();
    }

    @SneakyThrows
    @Override
    protected byte[] fetchMetadata() {
        DateTime metadataUpdateTime = new DateTime(this.classPathResource.lastModified(), ISOChronology.getInstanceUTC());
        if (getLastRefresh() == null || getLastUpdate() == null || metadataUpdateTime.isAfter(getLastRefresh())) {
            return inputstreamToByteArray(this.classPathResource.getInputStream());
        }
        return null;
    }
}
