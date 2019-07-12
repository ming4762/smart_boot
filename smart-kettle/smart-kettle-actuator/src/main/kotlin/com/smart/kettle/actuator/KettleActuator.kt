package com.smart.kettle.actuator

import com.smart.kettle.common.config.DatabaseMetaProperties
import com.smart.kettle.meta.CustomDatabaseMeta
import org.pentaho.di.core.KettleEnvironment
import org.pentaho.di.core.util.EnvUtil
import org.pentaho.di.job.Job
import org.pentaho.di.job.JobMeta
import org.pentaho.di.repository.kdr.KettleDatabaseRepository
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta
import org.pentaho.di.trans.Trans
import org.pentaho.di.trans.TransMeta

/**
 * kettle 执行器
 * @author ming
 * 2019/7/3 下午4:44
 */
object KettleActuator {

    /**
     * 执行转换文件
     */
    fun excuteTransfer(ktrPath: String, params: Array<String> = arrayOf()) {
        // 初始化kettle环境
        KettleEnvironment.init()
        EnvUtil.environmentInit()

        val transMeta = TransMeta(ktrPath)
        // 创建转换并执行
        val trans = Trans(transMeta)
        trans.execute(params)
        // 等待转换完成
        trans.waitUntilFinished()
        if (trans.errors > 0) {
            throw Exception("There are errors during transformation exception!(传输过程中发生异常)")
        }
    }

    /**
     * 执行job
     */
    fun excuteJob(jobPath: String, params: Map<String, String> = mapOf()) {
        // 初始化kettle环境
        KettleEnvironment.init()
        val jobMeta = JobMeta(jobPath, null)
        // 创建job
        val job = Job(null, jobMeta)
        // 设置参数
        params.forEach { key, value ->
            job.setVariable(key, value)
        }
        // 执行job
        job.start()
        job.waitUntilFinished()
        if (job.errors > 0) {
            throw Exception("There are errors during job exception!(执行job发生异常)")
        }
    }

    /**
     * 执行资源库转换
     * todo: connect 自动获取
     */
    fun excuteDBTransfer(databaseMetaProperties: DatabaseMetaProperties, transName: String, params: Array<String> = arrayOf()) {
        KettleEnvironment.init()
        // 创建资源库
        val repository = KettleDatabaseRepository()
        val databaseMeta = CustomDatabaseMeta(databaseMetaProperties.name, databaseMetaProperties.type,
                databaseMetaProperties.access, databaseMetaProperties.host, databaseMetaProperties.db,
                databaseMetaProperties.port, databaseMetaProperties.dbUser, databaseMetaProperties.dbPassword)
        val kettleDatabaseRepositoryMeta = KettleDatabaseRepositoryMeta("kettle","kettle","Transformation description",databaseMeta)
        repository.init(kettleDatabaseRepositoryMeta)
        //连接资源库
        repository.connect(databaseMetaProperties.resUser,databaseMetaProperties.resPassword)
        KettleActuator.excuteDBTransfer(repository, transName, params)
        repository.disconnect()
    }

    /**
     * 执行资源库转换
     */
    fun excuteDBTransfer(repository: KettleDatabaseRepository, transName: String, params: Array<String> = arrayOf()) {
        val directoryInterface = repository.loadRepositoryDirectoryTree()
        // 获取转换
        val transMeta = repository.loadTransformation(transName, directoryInterface,null,true,null)
        val trans = Trans(transMeta)
        trans.execute(params)
        trans.waitUntilFinished()
        if (trans.errors > 0) {
            throw Exception("There are errors during transformation exception!(传输过程中发生异常)")
        }
    }


    @JvmStatic
    fun main(args: Array<String>) {
//        KettleActuator.excuteTransfer("/Users/ming/Documents/temp/转换/监测断面水质监测阈值.ktr")
        val databaseMetaProperties = DatabaseMetaProperties()
        databaseMetaProperties.host = "192.168.1.91"
        databaseMetaProperties.db = "kettle_repository"
        databaseMetaProperties.dbUser = "root"
        databaseMetaProperties.dbPassword = "Charsming619"
        KettleActuator.excuteDBTransfer(databaseMetaProperties, "ceshi111")
    }
}