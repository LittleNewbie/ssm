package com.svili.model.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;
import com.svili.model.type.SexEnum;

/**
 * 用户信息
 * 
 * @author svili
 * @date 2016年8月10日
 *
 */
@Entity
@Table(name = "ssm_user")
public class User {

	@Id
	private String userId;

	/** 姓名 */
	private String userName;

	/** 昵称 */
	private String nickname;

	/** 性别 */
	@Enumerated(EnumType.STRING)
	private SexEnum sex;

	/** 密码 */
	@Column(name = "pwd")
	private String password;

	/** 手机号码 */
	private String mobilePhone;

	/** 固定电话/办公电话 */
	private String officePhone;

	/** 电子邮箱 */
	private String email;

	/** 身份证 */
	private String identityCard;

	/** 护照 */
	private String passport;

	/** 微信 */
	private String wechat;

	/** 新浪微博 */
	private String sinaBlog;

	/** QQ */
	private String tencent;

	/** 创建时间 */
	@Column(name = "gmt_create")
	private Date createTime;

	/** 更新时间 */
	@Column(name = "gmt_modify")
	private Date modifyTime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public SexEnum getSex() {
		return sex;
	}

	public void setSex(SexEnum sex) {
		this.sex = sex;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getOfficePhone() {
		return officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getSinaBlog() {
		return sinaBlog;
	}

	public void setSinaBlog(String sinaBlog) {
		this.sinaBlog = sinaBlog;
	}

	public String getTencent() {
		return tencent;
	}

	public void setTencent(String tencent) {
		this.tencent = tencent;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}