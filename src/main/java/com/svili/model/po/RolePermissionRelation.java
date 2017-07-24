package com.svili.model.po;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/***
 * 角色-权限 关系表
 * 
 * @author lishiwei
 * @data 2017年7月24日
 *
 */
@Entity
@Table(name = "YKT_C_ROLE_PERMISSION")
public class RolePermissionRelation {

	@Id
	private String relationId;

	private String roleId;

	private String permissionId;

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}

}
