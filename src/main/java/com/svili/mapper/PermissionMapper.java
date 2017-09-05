package com.svili.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.svili.model.po.Permission;

@Repository
public interface PermissionMapper {

	@Select("SELECT p.* FROM ssm_c_permission p INNER JOIN ssm_c_role_permission rp ON p.permission_id = up.permission_id WHERE ur.role_id = #{roleId} ")
	List<Permission> selectByRoleId(String roleId);

	@Select("SELECT p.* FROM ssm_c_permission p LEFT JOIN ssm_c_role_permission rp ON p.permission_id = up.permission_id "
			+ "LEFT JOIN ssm_c_user_role ur ON rp.role_id = ur.role_id WHERE ur.user_id = #{userId} ")
	List<Permission> selectByUserId(String userId);
}
