package com.svili.mapper;

import org.springframework.stereotype.Repository;

import com.mybatis.jpa.annotation.MapperDefinition;
import com.mybatis.jpa.annotation.StatementDefinition;
import com.svili.model.po.User;

@Repository
@MapperDefinition(domainClass = User.class)
public interface UserMapper {

	@StatementDefinition
	User selectByUserId(String userId);

	@StatementDefinition
	User selectByNickName(String nickName);

	@StatementDefinition
	User selectByMobilePhone(String mobilePhone);

	@StatementDefinition
	User selectByEmail(String email);

}
