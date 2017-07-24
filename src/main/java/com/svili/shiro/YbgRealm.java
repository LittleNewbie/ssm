package com.svili.shiro;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import com.svili.model.po.User;
import com.svili.model.type.UserStatusEnum;
import com.svili.service.UserService;

/**
 * shiro认证实体
 * 
 * @author lishiwei
 * @data 2017年5月17日
 *
 */
@Component
public class YbgRealm extends AuthorizingRealm {

	@Resource
	private UserService userService;

	/**
	 * 获取授权信息:角色和权限
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// userId: primaryPrincipal
		String userId = (String) super.getAvailablePrincipal(principals);

		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

		authorizationInfo.setRoles(userService.getShiroRoles(userId));
		authorizationInfo.setStringPermissions(userService.getShiroPermissions(userId));
		return authorizationInfo;
	}

	/***
	 * 获取身份验证信息:用户名,密码,salt
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// nickName or mobilePhone or email
		String loginName = String.valueOf(token.getPrincipal());
		String password = String.valueOf(token.getCredentials());

		User user = userService.getByLoginName(loginName);

		if (user == null) {
			// 用户不存在
			throw new UnknownAccountException();
		}

		if (!password.equals(user.getPassword())) {
			// 密码错误
			throw new IncorrectCredentialsException();
		}

		if (UserStatusEnum.FROZEN.equals(user.getStatus())) {
			// 帐号冻结
			throw new LockedAccountException();
		}

		// 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user.getUserId(), user.getPassword(),
				getName());
		return authenticationInfo;
	}

}
