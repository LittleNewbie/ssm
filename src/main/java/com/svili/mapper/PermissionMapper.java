package com.svili.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.svili.model.po.Permission;

@Repository
public interface PermissionMapper {

	@Select("select p.* from YKT_C_PERMISSION p left join YKT_C_ROLE_PERMISSION rp on p.PERMISSION_ID = up.PERMISSION_ID where ur.ROLE_ID = #{roleId} ")
	List<Permission> selectByRoleId(String roleId);

	@Select("select p.* from YKT_C_PERMISSION p left join YKT_C_ROLE_PERMISSION rp on p.PERMISSION_ID = up.PERMISSION_ID "
			+ "left join YKT_C_USER_ROLE ur on rp.ROLE_ID = ur.ROLE_ID where ur.USER_ID = #{userId} ")
	List<Permission> selectByUserId(String userId);
}
