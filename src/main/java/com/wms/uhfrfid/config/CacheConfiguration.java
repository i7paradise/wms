package com.wms.uhfrfid.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.wms.uhfrfid.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.wms.uhfrfid.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.wms.uhfrfid.domain.User.class.getName());
            createCache(cm, com.wms.uhfrfid.domain.Authority.class.getName());
            createCache(cm, com.wms.uhfrfid.domain.User.class.getName() + ".authorities");
            createCache(cm, com.wms.uhfrfid.domain.Company.class.getName());
            createCache(cm, com.wms.uhfrfid.domain.CompanyUser.class.getName());
            createCache(cm, com.wms.uhfrfid.domain.Container.class.getName());
            createCache(cm, com.wms.uhfrfid.domain.CompanyContainer.class.getName());
            createCache(cm, com.wms.uhfrfid.domain.UHFRFIDReader.class.getName());
            createCache(cm, com.wms.uhfrfid.domain.UHFRFIDAntenna.class.getName());
            createCache(cm, com.wms.uhfrfid.domain.Warehouse.class.getName());
            createCache(cm, com.wms.uhfrfid.domain.Area.class.getName());
            createCache(cm, com.wms.uhfrfid.domain.Door.class.getName());
            createCache(cm, com.wms.uhfrfid.domain.Location.class.getName());
            createCache(cm, com.wms.uhfrfid.domain.WHRow.class.getName());
            createCache(cm, com.wms.uhfrfid.domain.Bay.class.getName());
            createCache(cm, com.wms.uhfrfid.domain.WHLevel.class.getName());
            createCache(cm, com.wms.uhfrfid.domain.Position.class.getName());
            createCache(cm, com.wms.uhfrfid.domain.Product.class.getName());
            createCache(cm, com.wms.uhfrfid.domain.ProductCategory.class.getName());
            createCache(cm, com.wms.uhfrfid.domain.CompanyProduct.class.getName());
            createCache(cm, com.wms.uhfrfid.domain.Customer.class.getName());
            createCache(cm, com.wms.uhfrfid.domain.DeliveryOrder.class.getName());
            createCache(cm, com.wms.uhfrfid.domain.DeliveryOrderItem.class.getName());
            createCache(cm, com.wms.uhfrfid.domain.DeliveryContainer.class.getName());
            createCache(cm, com.wms.uhfrfid.domain.DeliveryItemProduct.class.getName());
            createCache(cm, com.wms.uhfrfid.domain.DoorAntenna.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
