package com.svili.mapper;

import org.springframework.stereotype.Repository;

import com.svili.model.po.User;

@Repository
public interface UserMapper {
	
	User selectByNickName(String nickName);
	
	User selectByMobilePhone(String mobilePhone);
	
	User selectByEmail(String email);

}
