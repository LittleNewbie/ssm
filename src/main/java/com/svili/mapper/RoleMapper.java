package com.svili.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.svili.model.po.Role;

@Repository
public interface RoleMapper {

	@Select("select r.* from YKT_C_ROLE r left join YKT_C_USER_ROLE ur on r.ROLE_ID = ur.ROLE_ID where ur.USER_ID = #{userId} ")
	List<Role> selectByUserId(String userId);

}
