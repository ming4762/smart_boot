package com.smart.system.mapper

import com.smart.starter.crud.mapper.CloudBaseMapper
import com.smart.system.model.SysMenuConfigDO
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Update

/**
 * 菜单配置映射
 */
interface SysMenuConfigMapper : CloudBaseMapper<SysMenuConfigDO> {

    /**
     * 激活配置
     */
    @Update(
            "update sys_menu_config set status = #{active} where config_id = #{configId}"
    )
    fun updateActiveStatus(@Param("configId") configId: String, @Param("active") active: Boolean)
}