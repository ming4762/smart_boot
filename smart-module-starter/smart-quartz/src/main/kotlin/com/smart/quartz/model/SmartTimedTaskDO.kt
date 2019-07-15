package com.smart.quartz.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.smart.quartz.preset.PresetClass
import com.smart.starter.crud.model.BaseModel
import com.smart.starter.quartz.properties.QuartzMetaData

/**
 * 定时任务实体类
 * @author ming
 * 2019/7/4 下午4:47
 */
@TableName("smart_timed_task")
class SmartTimedTaskDO : BaseModel() {

    companion object {
        private const val serialVersionUID = -6735048176105225227L

        fun convertToMetaData(task: SmartTimedTaskDO): QuartzMetaData<SmartTimedTaskDO> {
            val quartzMetaData = QuartzMetaData<SmartTimedTaskDO>()
            quartzMetaData.id = task.taskId!!
            quartzMetaData.cron = task.cron!!
            quartzMetaData.name = task.taskName ?: ""
            quartzMetaData.data = task
            if (task.clazz == null && task.presetClass != null) {
                quartzMetaData.clazz = PresetClass.valueOf(task.presetClass!!).clazz
            } else {
                quartzMetaData.clazz = task.clazz!!
            }
            return quartzMetaData
        }
    }

    /** TASK_ID - 任务ID  */
    @TableId(type = IdType.UUID)
    var taskId: String? = null

    /** TASK_NAME - 任务名称  */
    var taskName: String? = null

    /** CLASS - 类限定名  */
    @TableField("class")
    var clazz: String? = null

    /** CRON - cron表达式  */
    var cron: String? = null

    /** Enable - 启用  */
    @TableField("used")
    var used: Boolean? = null

    /** REMARK - 备注  */
    var remark: String? = null

    var seq: Int? = null

    var presetClass: String? = null

    var taskParameter: String? = null

    var queueName: String? = null
}