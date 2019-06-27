package com.smart.system.service

import com.smart.common.model.Tree
import com.smart.starter.crud.service.BaseService
import com.smart.system.model.SysOrganDO

/**
 *
 * @author ming
 * 2019/6/26 下午4:00
 */
interface SysOrganService : BaseService<SysOrganDO> {


    fun listTree(topParentIdList: List<String>): Map<String, List<Tree<SysOrganDO>>?>

}