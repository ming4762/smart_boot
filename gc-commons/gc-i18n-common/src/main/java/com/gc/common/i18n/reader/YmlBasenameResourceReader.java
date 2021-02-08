package com.gc.common.i18n.reader;

import com.gc.commons.io.utils.YamlUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * YML ResourceReader
 * @author ShiZhongMing
 * 2021/2/7 13:24
 * @since 1.0
 */
@Slf4j
public class YmlBasenameResourceReader extends AbstractBasenameResourceReader {

    private static final List<String> EXTENSIONS = Lists.newArrayList(".yml", ".yaml");


    @Override
    public Map<String, String> read(Locale locale) throws IOException {
        final Map<String, String> result = Maps.newLinkedHashMap();
        for (String basename : this.getBasename()) {
            result.putAll(this.readByBasename(basename, locale));
        }
        return result;
    }

    /**
     * 读取basename读取资源
     * @param basename basename
     * @param locale locale
     * @return 资源信息
     * @throws IOException IOException
     */
    protected Map<String, String> readByBasename(String basename, Locale locale) throws IOException {
        final Map<String, String> result = Maps.newLinkedHashMap();
        // 读取主文件
        for (String path: EXTENSIONS.stream().map(item -> basename + item).collect(Collectors.toList())) {
            result.putAll(this.doRead(path));
        }
        // 获取路径
        final String basePath = this.getPath(basename, locale);
        for (String path : EXTENSIONS.stream().map(item -> basePath + item).collect(Collectors.toList())) {
            result.putAll(this.doRead(path));
        }
        return result;
    }


    /**
     * 读取内容
     * @param path
     * @throws IOException
     */
    protected Map<String, String> doRead(String path) throws IOException {
        final Map<String, String> result = Maps.newLinkedHashMap();
        // 读取所有资源
        final Enumeration<URL> urls = this.getClassLoader().getResources(path);
        while (urls.hasMoreElements()) {
            final URL url = urls.nextElement();
            if (Objects.isNull(url)) {
                log.warn("not found resource，path:{}", path);
                continue;
            }
            try (final InputStreamReader inputStreamReader = new InputStreamReader(url.openStream(), this.getEncoding());) {
                // 读取内容
                final Map<String, Object> data = YamlUtils.readInOneLayer(inputStreamReader);
                data.forEach((key, value) -> result.put(key, value.toString()));
            }
        }
        return result;
    }

    /**
     * 获取资源路径
     * @param basename basename
     * @param locale Locale
     * @return 资源路径
     */
    private String getPath(String basename, Locale locale) {
        return String.join("_", basename, locale.toString());
    }
}
