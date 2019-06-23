package com.smart.system.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.smart.auth.common.utils.AuthUtils
import com.smart.starter.crud.service.impl.BaseServiceImpl
import com.smart.system.mapper.SysMenuConfigMapper
import com.smart.system.mapper.SysMenuMapper
import com.smart.system.model.SysMenuConfigDO
import com.smart.system.model.SysMenuDO
import com.smart.system.service.SysMenuConfigService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * 菜单配置服务层
 */
@Service
class SysMenuConfigServiceImpl : BaseServiceImpl<SysMenuConfigMapper, SysMenuConfigDO>(), SysMenuConfigService {

    @Autowired
    private lateinit var menuMapper: SysMenuMapper

    /**
     * 重写保存更新方法
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun saveOrUpdate(entity: SysMenuConfigDO): Boolean {
        entity.createTime = Date()
        entity.createUserId = AuthUtils.getCurrentUserId()
        return super.saveOrUpdate(entity)
    }

    /**
     * 重写批量删除方法
     * 删除配置对应的菜单信息
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun batchDelete(tList: List<SysMenuConfigDO>): Int {
        // 删除配置对应的菜单信息
        // 构建删除条件
        val wrapper = QueryWrapper<SysMenuDO>()
        val configIdList = tList.map {
            it.configId
        }
        wrapper.`in`("menu_config_id", configIdList)
        // 执行删除
        return this.menuMapper.delete(wrapper) + super.batchDelete(tList)
    }

    /**
     * 激活配置
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun activeConfig(menuConfig: SysMenuConfigDO): Boolean {
        // 查询当前激活的菜单
        val wrapper = KtQueryWrapper(SysMenuConfigDO :: class.java)
        wrapper.eq(SysMenuConfigDO :: status, true)
        val activeList = this.baseMapper.selectList(wrapper)
        if (activeList != null) {
            // 将激活的菜单设为不激活
            activeList.forEach {
                this.baseMapper.updateActiveStatus(it.configId!!, false)
            }
        }
        // 激活选中的菜单
        this.baseMapper.updateActiveStatus(menuConfig.configId!!, true)
        return true
    }

    /**
     * 查询人员的菜单配置信息
     */
    override fun queryUserMenuConfig(userId: String): SysMenuConfigDO? {
        // 查询人员的菜单配置
        val userConfigWrapper = KtQueryWrapper(SysMenuConfigDO :: class.java)
        userConfigWrapper.eq(SysMenuConfigDO :: userId, userId)
                .eq(SysMenuConfigDO :: status, true)
        val userConfig = this.baseMapper.selectOne(userConfigWrapper)
        if (userConfig != null) {
            return userConfig
        } else {
            // 如果人员的菜单配置不存在，查询系统的菜单配置
            val configWrapper = KtQueryWrapper(SysMenuConfigDO :: class.java)
            configWrapper.eq(SysMenuConfigDO :: status, true)
                    .isNull(SysMenuConfigDO :: userId)
            return this.baseMapper.selectOne(configWrapper)
        }
    }
}