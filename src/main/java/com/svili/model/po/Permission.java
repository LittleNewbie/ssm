package com.svili.model.po;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/***
 * 权限
 * 
 * @author lishiwei
 * @data 2017年7月24日
 *
 */
@Entity
@Table(name = "YKT_C_PERMISSION")
public class Permission {

	@Id
	private String permissionId;

	/** 权限代码 */
	private String permissionCode;

	/** 权限名称 */
	private String permissionName;

	/** 备注 */
	private String remark;

	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}

	public String getPermissionCode() {
		return permissionCode;
	}

	public void setPermissionCode(String permissionCode) {
		this.permissionCode = permissionCode;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
