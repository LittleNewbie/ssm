package com.svili.model.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/***
 * 角色</br>
 * 
 * @author lishiwei
 * @data 2017年6月19日
 *
 */
@Entity
@Table(name = "YKT_C_ROLE")
public class Role {

	@Id
	private String roleId;

	/** 角色代码 */
	private String roleCode;

	/** 角色名称 */
	private String roleName;

	/** 备注 */
	private String remark;

	/** 目录 */
	@Column(name = "IS_CATALOG")
	private Boolean catalog;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getCatalog() {
		return catalog;
	}

	public void setCatalog(Boolean catalog) {
		this.catalog = catalog;
	}

}
