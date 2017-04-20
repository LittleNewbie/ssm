package com.svili.crud;

import org.springframework.stereotype.Repository;

import com.svili.portal.pojo.User;

@Repository("userDao")
public interface UserDao {
	
	User select(Object primaryValue);

	int insert(User user);
}
