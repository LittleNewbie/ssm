package com.svili.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.svili.mapper.PermissionMapper;
import com.svili.mapper.RoleMapper;
import com.svili.mapper.UserMapper;
import com.svili.model.po.Permission;
import com.svili.model.po.Role;
import com.svili.model.po.User;

@Service
public class UserService {

	/** 手机号码正则 */
	public static final Pattern MOBILE_PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

	/** 邮箱正则 */
	public static final Pattern EMAIL_PATTERN = Pattern
			.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");

	@Resource
	private UserMapper userMapper;

	@Resource
	private RoleMapper roleMapper;

	@Resource
	private PermissionMapper permissionMapper;

	public User getByUserId(String userId) {
		if (StringUtils.isEmpty(userId)) {
			return null;
		}

		return userMapper.selectByUserId(userId);
	}

	public User getByLoginName(String loginName) {
		if (StringUtils.isEmpty(loginName)) {
			return null;
		}
		// 手机号
		if (MOBILE_PHONE_PATTERN.matcher(loginName).matches()) {
			return userMapper.selectByMobilePhone(loginName);
		}

		// 邮箱
		if (EMAIL_PATTERN.matcher(loginName).matches()) {
			return userMapper.selectByEmail(loginName);
		}

		// 昵称
		return userMapper.selectByNickName(loginName);
	}

	public List<Role> getRoles(String userId) {
		return roleMapper.selectByUserId(userId);
	}

	public Set<String> getShiroRoles(String userId) {
		List<Role> roles = getRoles(userId);
		if (roles == null || roles.isEmpty()) {
			return null;
		}
		Set<String> shiroRoles = new HashSet<>(roles.size());
		for (Role role : roles) {
			shiroRoles.add(role.getRoleCode());
		}
		return shiroRoles;
	}

	public List<Permission> getPermissions(String userId) {
		return permissionMapper.selectByUserId(userId);
	}

	public Set<String> getShiroPermissions(String userId) {
		List<Permission> permissions = getPermissions(userId);
		if (permissions == null || permissions.isEmpty()) {
			return null;
		}
		Set<String> shiroPermissions = new HashSet<>(permissions.size());
		for (Permission permission : permissions) {
			shiroPermissions.add(permission.getPermissionCode());
		}
		return shiroPermissions;
	}

}
