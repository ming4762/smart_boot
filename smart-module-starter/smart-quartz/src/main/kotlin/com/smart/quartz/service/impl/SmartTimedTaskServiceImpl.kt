package com.smart.quartz.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper
import com.smart.common.utils.UUIDGenerator
import com.smart.quartz.mapper.SmartTimedTaskMapper
import com.smart.quartz.model.SmartTimedTaskDO
import com.smart.quartz.service.SmartTimedTaskService
import com.smart.starter.crud.query.PageQueryParameter
import com.smart.starter.crud.service.impl.BaseServiceImpl
import com.smart.starter.quartz.service.QuartzService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 定时任务类
 * @author ming
 * 2019/7/4 下午4:56
 */
@Service
class SmartTimedTaskServiceImpl : BaseServiceImpl<SmartTimedTaskMapper, SmartTimedTaskDO>(), SmartTimedTaskService {

    @Autowired
    private lateinit var quartzService: QuartzService

    override fun list(queryWrapper: QueryWrapper<SmartTimedTaskDO>, parameter: PageQueryParameter, paging: Boolean): List<SmartTimedTaskDO> {
        return super<BaseServiceImpl>.list(queryWrapper, parameter, paging)
    }

    /**
     * 批量删除
     */
    @Transactional(value = "quartzTransactionManager", rollbackFor = [Exception::class])
    override fun batchDelete(modelList: List<SmartTimedTaskDO>): Int {
        // 移除定时任务
        this.quartzService.removeTask(modelList.mapNotNull { it.taskId })
        return super.batchDelete(modelList)
    }

    @Transactional(value = "quartzTransactionManager", rollbackFor = [Exception::class])
    override fun delete(model: SmartTimedTaskDO): Int {
        // 移除定时任务
        this.quartzService.removeTask(model.taskId!!)
        return super.delete(model)
    }

    @Transactional(value = "quartzTransactionManager", rollbackFor = [Exception::class])
    override fun saveOrUpdate(entity: SmartTimedTaskDO): Boolean {
        var isAdd = false
        if (entity.taskId == null) {
            isAdd = true
        } else if (this.getById(entity.taskId) == null) {
            isAdd = true
        }
        return if (isAdd) {
            this.save(entity)
        } else {
            this.updateById(entity)
        }
    }

    @Transactional(value = "quartzTransactionManager", rollbackFor = [Exception::class])
    override fun save(entity: SmartTimedTaskDO): Boolean {
        if (entity.taskId == null) entity.taskId = UUIDGenerator.getUUID()
        if (entity.used == true) {
            this.quartzService.addTask(SmartTimedTaskDO.convertToMetaData(entity))
        }
        return super.save(entity)
    }

    @Transactional(value = "quartzTransactionManager", rollbackFor = [Exception::class])
    override fun updateById(entity: SmartTimedTaskDO): Boolean {
        this.quartzService.removeTask(entity.taskId!!)
        if (entity.used == true) {
            this.quartzService.addTask(SmartTimedTaskDO.convertToMetaData(entity))
        }
        return super.updateById(entity)
    }

    /**
     * 关闭开始任务
     */
    @Transactional(value = "quartzTransactionManager", rollbackFor = [Exception::class])
    override fun openClose(taskIdList: List<String>, enable: Boolean) {
        // 更新对象
        val updateWrapper = KtUpdateWrapper(SmartTimedTaskDO :: class.java)
                .set(SmartTimedTaskDO :: used, enable)
                .`in`(SmartTimedTaskDO :: taskId, taskIdList)
        this.update(updateWrapper)
        // 处理任务
        taskIdList.forEach {
            if (enable) {
                // 判断任务是否存在
                val jobDetail = this.quartzService.getJob(it)
                if (jobDetail == null) {
                    val task = this.getById(it)
                    this.quartzService.addTask(SmartTimedTaskDO.convertToMetaData(task))
                } else {
                    this.quartzService.resumeTask(it)
                }
            } else {
                this.quartzService.pauseTask(it)
            }
        }
    }

    @Transactional(value = "quartzTransactionManager", rollbackFor = [Exception::class])
    override fun openClose(taskList: List<SmartTimedTaskDO>) {
        taskList.forEach {
            val updateWrapper = KtUpdateWrapper(SmartTimedTaskDO :: class.java)
                    .set(SmartTimedTaskDO :: used, it.used)
                    .eq(SmartTimedTaskDO :: taskId, it.taskId)
            this.update(updateWrapper)
            if (it.used == true) {
                // 判断任务是否存在
                val jobDetail = this.quartzService.getJob(it.taskId!!)
                if (jobDetail == null) {
                    // 查询任务
                    val taskQuery = this.getById(it.taskId)
                    this.quartzService.addTask(SmartTimedTaskDO.convertToMetaData(taskQuery))
                } else {
                    this.quartzService.resumeTask(it.taskId!!)
                }
            } else {
                this.quartzService.pauseTask(it.taskId!!)
            }
        }
    }


}