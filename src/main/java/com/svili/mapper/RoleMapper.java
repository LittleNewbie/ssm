package com.svili.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.svili.model.po.Role;

@Repository
public interface RoleMapper {

	@Select("SELECT r.* FROM ssm_c_role r INNER JOIN ssm_c_user_role ur ON r.role_id = ur.role_id WHERE ur.user_id = #{userId} ")
	List<Role> selectByUserId(String userId);

}
