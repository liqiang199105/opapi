package com.litb.opapi.dao.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="op_app_token")
@SuppressWarnings("serial")
public class OpAppToken implements Serializable{

	private Integer tokenId;
	private String appName;
	private String appUserKey;
	private String appToken;
	private String appSecret;
	private Date createDate;
	private Date lastModified;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="token_id")
	public Integer getTokenId() {
		return tokenId;
	}
	public void setTokenId(Integer tokenId) {
		this.tokenId = tokenId;
	}

	@Column(name="app_name")
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	@Column(name="app_user_key")
	public String getAppUserKey() {
		return appUserKey;
	}
	public void setAppUserKey(String appUserKey) {
		this.appUserKey = appUserKey;
	}

	@Column(name="app_token")
	public String getAppToken() {
		return appToken;
	}
	public void setAppToken(String appToken) {
		this.appToken = appToken;
	}

	@Column(name="app_secret")
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	
	@Column(name="create_date")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name="last_modified")
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	@Override
	public String toString() {
		return "OpAppToken:{tokenId:" + this.tokenId
				+ ", appName: " + appName
				+ ", appUserKey: " + appUserKey
				+ ", appToken: " + appToken
				+ ", appSecret: " + appSecret + "}";
	}
}
