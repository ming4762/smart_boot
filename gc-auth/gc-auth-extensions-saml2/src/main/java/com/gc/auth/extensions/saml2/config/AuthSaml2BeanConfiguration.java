package com.gc.auth.extensions.saml2.config;

import com.gc.auth.core.handler.AuthLogoutSuccessHandler;
import com.gc.auth.core.properties.AuthProperties;
import com.gc.auth.core.service.AuthUserService;
import com.gc.auth.extensions.saml2.service.DefaultSamlUserDetailsServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.httpclient.HttpClient;
import org.opensaml.saml2.metadata.provider.FilesystemMetadataProvider;
import org.opensaml.saml2.metadata.provider.HTTPMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.security.MetadataCredentialResolver;
import org.opensaml.xml.parse.ParserPool;
import org.opensaml.xml.parse.StaticBasicParserPool;
import org.opensaml.xml.security.x509.CertPathPKIXTrustEvaluator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.saml.SAMLAuthenticationProvider;
import org.springframework.security.saml.SAMLEntryPoint;
import org.springframework.security.saml.context.SAMLContextProvider;
import org.springframework.security.saml.context.SAMLContextProviderImpl;
import org.springframework.security.saml.key.JKSKeyManager;
import org.springframework.security.saml.key.KeyManager;
import org.springframework.security.saml.log.SAMLDefaultLogger;
import org.springframework.security.saml.log.SAMLLogger;
import org.springframework.security.saml.metadata.*;
import org.springframework.security.saml.processor.*;
import org.springframework.security.saml.trust.PKIXInformationResolver;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.security.saml.util.VelocityFactory;
import org.springframework.security.saml.websso.*;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * SAML相关bean 配置
 * @author ShiZhongMing
 * 2021/1/6 17:05
 * @since 1.0
 */
public class AuthSaml2BeanConfiguration implements InitializingBean {

    private final AuthProperties.Saml2 saml2Properties;

    public AuthSaml2BeanConfiguration(AuthProperties authProperties) {
        this.saml2Properties = authProperties.getSaml2();
    }

    /**
     * 创建 SAMLContextProvider
     * @return SAMLContextProvider
     */
    @Bean
    @ConditionalOnMissingBean(SAMLContextProvider.class)
    public SAMLContextProvider samlContextProvider(MetadataCredentialResolver resolver, PKIXInformationResolver pkixInformationResolver) {
        final SAMLContextProviderImpl samlContextProvider = new SAMLContextProviderImpl();
        samlContextProvider.setMetadataResolver(resolver);
        samlContextProvider.setPkixTrustEvaluator(new CertPathPKIXTrustEvaluator());
        samlContextProvider.setPkixResolver(pkixInformationResolver);
        return samlContextProvider;
    }

    @Bean@ConditionalOnMissingBean(PKIXInformationResolver.class)
    public PKIXInformationResolver pkixInformationResolver(MetadataCredentialResolver resolver, MetadataManager metadataManager, KeyManager keyManager) {
        return new PKIXInformationResolver(resolver, metadataManager, keyManager);
    }

    @Bean
    @ConditionalOnMissingBean(MetadataCredentialResolver.class)
    public MetadataCredentialResolver metadataCredentialResolver(MetadataManager metadataManager, KeyManager keyManager) {
        return new org.springframework.security.saml.trust.MetadataCredentialResolver(metadataManager, keyManager);
    }

    /**
     * Filter automatically generates default SP metadata
     * @param extendedMetadata extendedMetadata
     * @return MetadataGenerator
     */
    @Bean
    @ConditionalOnMissingBean(MetadataGenerator.class)
    public MetadataGenerator metadataGenerator(ExtendedMetadata extendedMetadata) {
        final MetadataGenerator metadataGenerator = new MetadataGenerator();
        metadataGenerator.setEntityId(this.saml2Properties.getEntityId());
        metadataGenerator.setIncludeDiscoveryExtension(false);
        metadataGenerator.setExtendedMetadata(extendedMetadata);
        return metadataGenerator;
    }

    /**
     * Entry point to initialize authentication, default values taken from
     * @param webSSOProfileOptions webSSOProfileOptions
     * @return Entry point
     */
    @Bean
    @ConditionalOnMissingBean(SAMLEntryPoint.class)
    public SAMLEntryPoint samlEntryPoint(WebSSOProfileOptions webSSOProfileOptions, WebSSOProfile webSSOProfile) {
        final SAMLEntryPoint samlEntryPoint = new SAMLEntryPoint();
        samlEntryPoint.setWebSSOprofile(webSSOProfile);
        samlEntryPoint.setDefaultProfileOptions(webSSOProfileOptions);
        return samlEntryPoint;
    }

    @Bean
    @ConditionalOnMissingBean(LogoutHandler.class)
    public LogoutHandler logoutHandler() {
        SecurityContextLogoutHandler logoutHandler =
                new SecurityContextLogoutHandler();
        logoutHandler.setInvalidateHttpSession(true);
        logoutHandler.setClearAuthentication(true);
        return logoutHandler;
    }

    @Bean
    @ConditionalOnMissingBean(SingleLogoutProfile.class)
    public SingleLogoutProfile singleLogoutProfile() {
        return new SingleLogoutProfileImpl();
    }

    @Bean
    @ConditionalOnMissingBean(LogoutSuccessHandler.class)
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new AuthLogoutSuccessHandler();
    }

    /**
     * 创建日志
     * @return SAMLLogger
     */
    @Bean
    @ConditionalOnMissingBean(SAMLLogger.class)
    public SAMLLogger samlLogger() {
        return new SAMLDefaultLogger();
    }

    @Bean
    @ConditionalOnMissingBean(WebSSOProfileOptions.class)
    public WebSSOProfileOptions webSSOProfileOptions() {
        WebSSOProfileOptions webSSOProfileOptions = new WebSSOProfileOptions();
        webSSOProfileOptions.setIncludeScoping(false);
        return webSSOProfileOptions;
    }

    @Bean(name = "webSSOprofile")
    @ConditionalOnMissingBean(WebSSOProfile.class)
    public WebSSOProfile webSSOProfile() {
        return new WebSSOProfileImpl();
    }

    @Bean
    @ConditionalOnMissingBean(SAMLProcessor.class)
    public SAMLProcessor samlProcessor(ParserPool parserPool) {
        Collection<SAMLBinding> bindings = new ArrayList<>();
        bindings.add(new HTTPRedirectDeflateBinding(parserPool));
        bindings.add(new HTTPPostBinding(parserPool, VelocityFactory.getEngine()));
        return new SAMLProcessorImpl(bindings);
    }

    @Bean
    @ConditionalOnMissingBean(SAMLUserDetailsService.class)
    public SAMLUserDetailsService samlUserDetailsService(AuthUserService authUserService) {
        return new DefaultSamlUserDetailsServiceImpl(authUserService);
    }

    /**
     * SAML 2.0 WebSSO Assertion Consumer
     * @return WebSSOProfileConsumer
     */
    @Bean("webSSOprofileConsumer")
    @ConditionalOnMissingBean(WebSSOProfileConsumer.class)
    public WebSSOProfileConsumer webSSOProfileConsumer() {
        return new WebSSOProfileConsumerImpl();
    }

    @Bean("hokWebSSOprofileConsumer")
    @ConditionalOnMissingBean(WebSSOProfileConsumerHoKImpl.class)
    public WebSSOProfileConsumerHoKImpl hokWebSSOprofileConsumer() {
        return new WebSSOProfileConsumerHoKImpl();
    }

    /**
     * SAML Authentication Provider responsible for validating of received SAML
     * @param samlUserDetailsService SAMLUserDetailsService
     * @return SAMLAuthenticationProvider
     */
    @Bean
    @ConditionalOnMissingBean(SAMLAuthenticationProvider.class)
    public SAMLAuthenticationProvider samlAuthenticationProvider(SAMLUserDetailsService samlUserDetailsService) {
        SAMLAuthenticationProvider samlAuthenticationProvider = new SAMLAuthenticationProvider();
        samlAuthenticationProvider.setUserDetails(samlUserDetailsService);
        samlAuthenticationProvider.setForcePrincipalAsString(false);
        return samlAuthenticationProvider;
    }

//    @Bean
//    @ConditionalOnMissingBean(ExtendedMetadataDelegate.class)
    public ExtendedMetadataDelegate extendedMetadataDelegate(MetadataProvider metadataProvider, ExtendedMetadata extendedMetadata) {
        ExtendedMetadataDelegate extendedMetadataDelegate = new ExtendedMetadataDelegate(metadataProvider, extendedMetadata);
        extendedMetadataDelegate.setMetadataTrustCheck(false);
        extendedMetadataDelegate.setMetadataRequireSignature(false);
        return extendedMetadataDelegate;
    }

    @Bean
    @ConditionalOnMissingBean(MetadataProvider.class)
    public MetadataProvider metadataProvider(ParserPool parserPool) throws MetadataProviderException, IOException {
        if (this.saml2Properties.getIdentity().getMetadataFilePath().startsWith("http")) {
            return this.createHttpMetadataProvider(parserPool);
        } else {
            return this.createFilesystemMetadataProvider(parserPool);
        }
    }

    /**
     * 创建HTTP MetadataProvider
     * @return HTTP MetadataProvider
     * @throws MetadataProviderException MetadataProviderException
     */
    private MetadataProvider createHttpMetadataProvider(ParserPool parserPool) throws MetadataProviderException {
        final HTTPMetadataProvider metadataProvider = new HTTPMetadataProvider(new Timer(), new HttpClient(), this.saml2Properties.getIdentity().getMetadataFilePath());
        metadataProvider.setParserPool(parserPool);
        return metadataProvider;
    }

    private MetadataProvider createFilesystemMetadataProvider(ParserPool parserPool) throws IOException, MetadataProviderException {
        final DefaultResourceLoader loader = new DefaultResourceLoader();
        final Resource metadataResource = loader.getResource(this.saml2Properties.getIdentity().getMetadataFilePath());
        final File samlMetadata = metadataResource.getFile();
        final FilesystemMetadataProvider metadataProvider = new FilesystemMetadataProvider(samlMetadata);
        metadataProvider.setParserPool(parserPool);
        return metadataProvider;
    }

    /**
     * XML parser pool needed for OpenSAML parsing
     * @return StaticBasicParserPool
     */
    @Bean(initMethod = "initialize")
    @ConditionalOnMissingBean(ParserPool.class)
    public StaticBasicParserPool parserPool() {
        return new StaticBasicParserPool();
    }


    @Bean
    @ConditionalOnMissingBean(MetadataManager.class)
    public MetadataManager metadataManager(MetadataProvider metadataProvider, ExtendedMetadata extendedMetadata) throws MetadataProviderException {
        ExtendedMetadataDelegate extendedMetadataDelegate = new ExtendedMetadataDelegate(metadataProvider, extendedMetadata);
        extendedMetadataDelegate.setMetadataTrustCheck(false);
        extendedMetadataDelegate.setMetadataRequireSignature(false);
        final List<MetadataProvider> providers = Lists.newArrayList(extendedMetadataDelegate);
        return new CachingMetadataManager(providers);
    }

    /**
     * Setup advanced info about metadata
     * @return ExtendedMetadata
     */
    @Bean
    @ConditionalOnMissingBean(ExtendedMetadata.class)
    public ExtendedMetadata extendedMetadata() {
        final ExtendedMetadata extendedMetadata = new ExtendedMetadata();
        extendedMetadata.setIdpDiscoveryEnabled(this.saml2Properties.getIdentity().getDiscoveryEnabled());
        extendedMetadata.setSignMetadata(true);
        return extendedMetadata;
    }

    /**
     * 创建 KeyManager
     * @return KeyManager
     */
    @Bean
    @ConditionalOnMissingBean(KeyManager.class)
    public KeyManager samlKeyManager() {
        final AuthProperties.Saml2.KeyStore keyStore = this.saml2Properties.getKey();
        final DefaultResourceLoader loader = new DefaultResourceLoader();
        Resource storeFile = loader.getResource(keyStore.getFilePath());
        if (keyStore.getFilePath().startsWith("file://")) {
            try {
                storeFile = new FileSystemResource(storeFile.getFile());
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Cannot load file system resource: " + keyStore.getFilePath(), e);
            }
        }
        Map<String, String> passwords = new HashMap<>();
        passwords.put(keyStore.getName(), keyStore.getPassword());
        return new JKSKeyManager(storeFile, keyStore.getPassword(), passwords, keyStore.getName());
    }


    @Override
    public void afterPropertiesSet() {
        Assert.notNull(this.saml2Properties.getEntityId(), "entityId is null, please init it, [gc.auth.saml2.entity-id]");
        Assert.notNull(this.saml2Properties.getIdentity().getMetadataFilePath(), "MetadataFilePath is null, please inti it, [gc.auth.saml2.identity.metadataFilePath]");
        Assert.notNull(this.saml2Properties.getKey().getFilePath(), "key file path is null, please init it, [gc.auth.saml2.key.filePath]");
        Assert.notNull(this.saml2Properties.getKey().getName(), "key name is null, please init it, [gc.auth.saml2.key.name]");
        Assert.notNull(this.saml2Properties.getKey().getPassword(), "key password is null, please init it, [gc.auth.saml2.key.password]");
    }
}
