package com.smart.file.model

import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.smart.starter.crud.model.BaseModel

/**
 * 文件关联关系实体类
 */
@TableName("smart_file_relation")
class FileRelationDO : BaseModel() {

    @TableId
    var fileId: String? = null
    @TableId
    var relationFileId: String? = null
    @TableId
    var relationType: String? = null
}