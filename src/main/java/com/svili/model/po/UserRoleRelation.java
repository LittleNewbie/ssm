package com.svili.model.po;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/***
 * 用户-角色 关系表
 * 
 * @author lishiwei
 * @data 2017年6月19日
 *
 */
@Entity
@Table(name = "YKT_C_USER_ROLE")
public class UserRoleRelation {

	@Id
	private String relationId;

	private String userId;

	private String roleId;

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}
