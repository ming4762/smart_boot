package com.smart.starter.kettle.service.impl

import com.smart.kettle.actuator.KettleActuator
import com.smart.starter.kettle.pool.KettleRepositoryProvider
import com.smart.starter.kettle.service.KettleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * kettle 服务类
 * @author ming
 * 2019/7/11 下午4:10
 */
@Service
class KettleServiceImpl : KettleService {

    /**
     * 资源库提供者
     */
    @Autowired
    private lateinit var kettleRepositoryProvider: KettleRepositoryProvider

    /**
     * 执行资源库转换
     */
    override fun excuteDBTransfer(transName: String, params: Array<String>) {
        val repository = kettleRepositoryProvider.getRepository()
        KettleActuator.excuteDBTransfer(repository, transName, params)
        // 必须归还对象，否则会造成线程阻塞
        kettleRepositoryProvider.returnRepository(repository)
    }
}