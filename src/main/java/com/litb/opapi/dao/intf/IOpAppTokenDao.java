package com.litb.opapi.dao.intf;

import java.util.List;

import com.litb.opapi.dao.entity.OpAppInfo;
import com.litb.opapi.dao.entity.OpAppToken;

public interface IOpAppTokenDao {

	public OpAppToken insert(OpAppToken opAppToken);

	public void delete(OpAppToken opAppToken);

	public void update(OpAppToken opAppToken, String oldToken);

	public OpAppToken findAppToken(String appName, String appUserKey);

	public OpAppInfo insert(OpAppInfo opAppInfo);

	public List<OpAppInfo> getAllAppInfo();

	public OpAppToken findAppTokenByToken(String token);
}
