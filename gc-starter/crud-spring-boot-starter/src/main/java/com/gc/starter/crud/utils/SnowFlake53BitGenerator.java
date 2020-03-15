package com.gc.starter.crud.utils;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.springframework.stereotype.Component;

/**
 * 自定义mybatis plus id生成器
 * 使用53bit雪花算法
 * @author jackson
 * 2020/3/14 10:24 下午
 */
@Component
public class SnowFlake53BitGenerator implements IdentifierGenerator {
    @Override
    public Number nextId(Object entity) {
        return IdGenerator.nextId();
    }
}
