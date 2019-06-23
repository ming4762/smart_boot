package com.smart.system.model.vo

import com.smart.system.model.SysFunctionDO
import com.smart.system.model.SysMenuDO

/**
 * menuVo
 */
class SysMenuVO: SysMenuDO() {

//    init {
//        // 获取父类的属性
//        val menuFieldList = Menu :: class.java.declaredFields
//        val menuFieldMap = menuFieldList.toList().stream().collect(
//                Collectors.toMap(Field :: getName) { it }
//        )
//        // 获取子类的属性
//        val childFieldList = this :: class.java.declaredFields.toMutableList()
//        childFieldList.addAll(menuFieldList)
//        childFieldList.forEach {
//            if (!Modifier.isFinal(it.modifiers)) {
//                it.isAccessible = true
//                it.set(this, menuFieldMap[it.name]?.get(menu))
//            }
//        }
//    }
    /**
     * 菜单对应的功能
     */
    var function: SysFunctionDO? = null

    // 菜单地址
    var url: String? = null
}