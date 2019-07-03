package com.smart.system.controller

import com.smart.common.message.Result
import com.smart.starter.crud.controller.BaseController
import com.smart.system.model.SysMenuDO
import com.smart.system.model.vo.SysMenuVO
import com.smart.system.service.SysMenuService
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @author ming
 * 2019/7/2 上午9:12
 */
@RestController
@RequestMapping("/sys/menu")
class SysMenuController : BaseController<SysMenuService, SysMenuDO>() {

    /**
     * 查询菜单列表带有功能
     */
    @RequiresPermissions("system:menu:query")
    @RequestMapping("/listWithFunction")
    fun listWithFunction (@RequestBody parameters: Map<String, Any?>): Result<List<SysMenuVO>?> {
        return try {
            Result.success(this.service.listWithFunction(parameters))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.message, null)
        }
    }
}