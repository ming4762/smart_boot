package com.gc.common.i18n.reader;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ClassUtils;

import java.nio.charset.Charset;
import java.util.Set;

/**
 * @author ShiZhongMing
 * 2021/2/7 14:14
 * @since 1.0
 */
public abstract class AbstractBasenameResourceReader implements BasenameResourceReader {

    private final Set<String> basenameSet = Sets.newLinkedHashSet();


    private final ClassLoader defaultClassLoader = ClassUtils.getDefaultClassLoader();

    private ClassLoader classLoader;

    @Getter
    @Setter
    private Charset encoding;

    /**
     * 设置basename
     *
     * @param basenameList 路径
     */
    @Override
    public void setBasename(Set<String> basenameList) {
        basenameSet.clear();
        basenameSet.addAll(basenameList);
    }

    /**
     * 添加basename
     *
     * @param basenameList basename列表
     */
    @Override
    public void addBasename(Set<String> basenameList) {
        basenameSet.addAll(basenameList);
    }

    @Override
    public ClassLoader getClassLoader() {
        return this.classLoader != null ? this.classLoader : this.defaultClassLoader;
    }

    @Override
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    protected Set<String> getBasename() {
        return this.basenameSet;
    }


}
