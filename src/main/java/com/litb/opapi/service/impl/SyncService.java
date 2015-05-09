package com.litb.opapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.litb.opapi.dao.entity.OpAppToken;
import com.litb.opapi.exception.ApiException;
import com.litb.opapi.request.AppTokenRequest;
import com.litb.opapi.service.intf.ITokenService;

@Service
public class SyncService {
	@Autowired ITokenService tokenService;

	public synchronized OpAppToken createAppToken(AppTokenRequest appTokenRequest) throws ApiException {
		// TODO Auto-generated method stub
		return tokenService.internalCreateAppToken(appTokenRequest);
	}

}
