package com.gc.common.base.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gc.common.base.serialization.date.LocalDateTimeDeserializer;
import com.gc.common.base.serialization.date.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author shizhongming
 * 2020/9/13 5:38 下午
 */
@Configuration
public class JacksonConfig {

    @Value("${spring.jackson.serialization.write-dates-as-timestamps:false}")
    private Boolean datesAsTimestamps;

    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        if (Objects.equals(Boolean.TRUE, datesAsTimestamps)) {
            final JavaTimeModule javaTimeModule = new JavaTimeModule();
            javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
            javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
            objectMapper.registerModule(javaTimeModule);
        }
        return objectMapper;
    }
}
