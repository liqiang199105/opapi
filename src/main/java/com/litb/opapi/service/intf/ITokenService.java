package com.litb.opapi.service.intf;

import com.litb.opapi.dao.entity.OpAppToken;
import com.litb.opapi.exception.ApiException;
import com.litb.opapi.request.AppTokenRequest;

public interface ITokenService {

	public OpAppToken createAppToken(AppTokenRequest appTokenRequest) throws ApiException;

	public boolean deleteAppToken(AppTokenRequest appTokenRequest) throws ApiException;

	public OpAppToken resetAppToken(AppTokenRequest appTokenRequest) throws ApiException;

	public OpAppToken queryAppToken(AppTokenRequest appTokenRequest);

	public OpAppToken internalCreateAppToken(AppTokenRequest appTokenRequest) throws ApiException;

	public boolean createAppInfo(String appName) throws ApiException;

	public OpAppToken queryAppTokenByToken(String appToken);

}
