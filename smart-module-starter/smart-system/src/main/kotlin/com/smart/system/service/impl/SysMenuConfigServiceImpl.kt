package com.smart.system.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper
import com.smart.auth.common.utils.AuthUtils
import com.smart.common.utils.UUIDGenerator
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
        val result: Boolean
        var isAdd = false
        if (entity.configId == null) {
            isAdd = true
            entity.configId = UUIDGenerator.getUUID()
        } else if (this.getById(entity.configId) == null) {
            isAdd = true
        }
        // 激活菜单
        if (entity.status == true) {
            this.activeConfig(entity)
        }
        if (isAdd) {
            entity.createTime = Date()
            entity.createUserId = AuthUtils.getCurrentUserId()
            // 设置状态
            if (entity.status == null) {
                entity.status = false
            }
            // 执行保存操作
            result = this.save(entity)
        } else {
            // 执行保存操作
            val updateWrapper = KtUpdateWrapper(SysMenuConfigDO :: class.java)
            updateWrapper.set(SysMenuConfigDO :: configName, entity.configName)
                    .set(SysMenuConfigDO :: seq, entity.seq)
                    .eq(SysMenuConfigDO :: configId, entity.configId)
            result = this.update(updateWrapper)
        }
        // 修改状态
        if (entity.status == true) {
            this.activeConfig(entity)
        }
        return result
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
    override fun queryUserMenuConfig(userIdList: List<String>): Map<String, SysMenuConfigDO> {
        if(userIdList.isEmpty()) return mapOf()
        // 查询人员的菜单配置
        val userConfigWrapper = KtQueryWrapper(SysMenuConfigDO :: class.java)
                .eq(SysMenuConfigDO :: status, true)
        if (userIdList.size == 1) {
            userConfigWrapper.eq(SysMenuConfigDO :: userId, userIdList[0])
        } else {
            userConfigWrapper.`in`(SysMenuConfigDO :: userId, userIdList)
        }
        val userConfigMap = this.list(userConfigWrapper).map {
            it.userId to it
        }.toMap()
        // 默认的菜单配置
        var defaultConfig: SysMenuConfigDO? = null
        return userIdList.map {
            var config = userConfigMap[it]
            if (config == null) {
                if (defaultConfig == null) defaultConfig = this.queryDefaultConfig()
                config = defaultConfig
            }
            return@map it to config!!
        }.toMap()
    }

    /**
     * 查询系统默认菜单配置
     */
    private fun queryDefaultConfig(): SysMenuConfigDO {
        val configWrapper = KtQueryWrapper(SysMenuConfigDO :: class.java)
        configWrapper.eq(SysMenuConfigDO :: status, true)
                .isNull(SysMenuConfigDO :: userId)
        return this.baseMapper.selectOne(configWrapper)
    }
}