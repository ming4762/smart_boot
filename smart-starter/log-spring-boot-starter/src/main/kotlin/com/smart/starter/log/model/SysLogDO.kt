package com.smart.starter.log.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.smart.starter.crud.model.BaseModel
import java.util.*

/**
 * 日志实体类
 * @author ming
 * 2019/6/28 下午4:08
 */
@TableName("sys_log")
open class SysLogDO : BaseModel() {

    @TableId(type = IdType.UUID)
    var logId: String? = null

    var userId: String? = null

    var operation: String? = null
        set(operation) {
            field = operation?.trim { it <= ' ' }
        }

    var useTime: Int? = null

    var method: String? = null
        set(method) {
            field = method?.trim { it <= ' ' }
        }

    var params: String? = null
        set(params) {
            field = params?.trim { it <= ' ' }
        }

    var ip: String? = null
        set(ip) {
            field = ip?.trim { it <= ' ' }
        }

    var createTime: Date? = null

    var requestPath: String? = null

    var statusCode: Int = 200

    var errorMessage: String? = null

    companion object {

        private const val serialVersionUID = -6051958861759640801L
    }
}