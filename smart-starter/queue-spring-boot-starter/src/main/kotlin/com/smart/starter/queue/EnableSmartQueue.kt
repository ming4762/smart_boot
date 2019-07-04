package com.smart.starter.queue

import org.springframework.context.annotation.Import

/**
 *
 * @author ming
 * 2019/7/4 上午9:59
 */
@Target(AnnotationTarget.CLASS)
@Retention
@MustBeDocumented
@Import(SmartQueueAutoConfiguration::class)
annotation class EnableSmartQueue {
}