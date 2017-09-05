package com.svili.mapper;

import org.springframework.stereotype.Repository;

import com.mybatis.jpa.annotation.MapperDefinition;
import com.mybatis.jpa.annotation.StatementDefinition;
import com.svili.model.po.User;

@Repository
@MapperDefinition(domainClass = User.class)
public interface UserMapper {

	@StatementDefinition
	int insert(User entity);

	@StatementDefinition
	User selectByUserId(String userId);

	@StatementDefinition
	User selectByNickname(String nickName);

	@StatementDefinition
	User selectByMobilePhone(String mobilePhone);

	@StatementDefinition
	User selectByEmail(String email);

}
