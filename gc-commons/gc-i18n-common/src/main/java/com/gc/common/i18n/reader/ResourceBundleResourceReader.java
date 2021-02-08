package com.gc.common.i18n.reader;

import com.google.common.collect.Maps;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 资源加载器
 * @author ShiZhongMing
 * 2021/2/5 18:21
 * @since 1.0
 */

public class ResourceBundleResourceReader extends AbstractBasenameResourceReader {

    @Override
    public Map<String, String> read(Locale locale) {
        Map<String, String> result = Maps.newHashMap();
        this.getBasename().forEach(basename -> result.putAll(this.resourceBundle2Map(this.doGetBundle(basename, locale))));
        return result;
    }

    /**
     * ResourceBundle 转为Map
     * @param resourceBundle ResourceBundle
     * @return map
     */
    protected Map<String, String> resourceBundle2Map(ResourceBundle resourceBundle) {
        final Map<String, String> result = Maps.newHashMap();
        resourceBundle.keySet().forEach(key -> result.put(key, resourceBundle.getString(key)));
        return result;
    }

    /**
     * 获取 ResourceBundle
     * @param basename basename
     * @param locale locale
     * @return ResourceBundle
     */
    protected ResourceBundle doGetBundle(String basename, Locale locale) {
        final ClassLoader classLoader = this.getClassLoader();
        return ResourceBundle.getBundle(basename, locale, classLoader);
    }

}
