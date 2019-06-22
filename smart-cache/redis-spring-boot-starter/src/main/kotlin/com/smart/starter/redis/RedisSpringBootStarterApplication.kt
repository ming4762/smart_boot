package com.smart.starter.redis

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RedisSpringBootStarterApplication

fun main(args: Array<String>) {
    runApplication<RedisSpringBootStarterApplication>(*args)
}
