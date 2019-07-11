package com.smart.starter.kettle.pool

import org.pentaho.di.repository.kdr.KettleDatabaseRepository

/**
 *
 * @author ming
 * 2019/7/11 下午3:21
 */
interface KettleRepositoryProvider {

    fun getRepository(): KettleDatabaseRepository

    fun returnRepository(repository: KettleDatabaseRepository)
}