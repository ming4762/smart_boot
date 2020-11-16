package com.gc.starter.ehcache;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;

/**
 * @author shizhongming
 * 2020/7/1 6:09 下午
 */
public class Test {

    public void abc () {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build();
    }
}
