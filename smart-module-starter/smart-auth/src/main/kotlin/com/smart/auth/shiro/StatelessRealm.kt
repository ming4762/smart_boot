package com.smart.auth.shiro

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.smart.auth.common.constants.AuthConstants
import com.smart.auth.common.model.SysUserDO
import com.smart.auth.common.utils.AuthUtils
import com.smart.system.service.SysUserService
import org.apache.shiro.authc.*
import org.apache.shiro.authz.AuthorizationInfo
import org.apache.shiro.authz.SimpleAuthorizationInfo
import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.subject.PrincipalCollection
import org.springframework.beans.factory.annotation.Autowired

/**
 *
 * @author ming
 * 2019/6/21 下午1:29
 */
class StatelessRealm : AuthorizingRealm() {

    @Autowired
    private lateinit var sysUserService: SysUserService

    /**
     * 用户认证
     */
    override fun doGetAuthenticationInfo(token: AuthenticationToken): AuthenticationInfo {
        val username = token.principal as String
        // 查询用户信息
        val userList = this.sysUserService.list(KtQueryWrapper(SysUserDO ::class.java ).eq(SysUserDO :: username, username))
        if (userList.isEmpty()) {
            throw UnknownAccountException("账号或密码不正确")
        }
        val user = userList[0]
        val password = String(token.credentials as CharArray)
        // 判断密码是否一致
        if (user.password!! != password) {
            throw IncorrectCredentialsException("账号或密码不正确")
        }
        // 判断用户是否锁定
        if (user.status == "0") {
            throw LockedAccountException("账号已被锁定,请联系管理员")
        }
        return SimpleAuthenticationInfo(user, password, name)
    }


    /**
     * 用户授权
     */
    override fun doGetAuthorizationInfo(principalCollection: PrincipalCollection): AuthorizationInfo {
        val info = SimpleAuthorizationInfo()
        val user = getAvailablePrincipal(principalCollection) as SysUserDO?
        // 获取用户权限
        if (user != null) {
            val permissionList = AuthUtils.getSession()?.getAttribute(AuthConstants.PERMISSION) as Set<String>? ?: setOf()
            info.addStringPermissions(permissionList)
        }
        return info
    }

    /**
     * 获取缓存的key
     */
    override fun getAuthorizationCacheKey(principals: PrincipalCollection?): Any {
        val session = AuthUtils.getSession()
        if (session != null) {
            return "authorization_cache_key:" + session.id
        }
        return super.getAuthorizationCacheKey(principals)
    }
}