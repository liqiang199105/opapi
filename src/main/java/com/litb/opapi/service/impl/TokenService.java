package com.litb.opapi.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.litb.opapi.dao.entity.OpAppInfo;
import com.litb.opapi.dao.entity.OpAppToken;
import com.litb.opapi.dao.intf.IOpAppTokenDao;
import com.litb.opapi.exception.ApiException;
import com.litb.opapi.internal.ErrorCode;
import com.litb.opapi.request.AppTokenRequest;
import com.litb.opapi.service.intf.ITokenService;
import com.litb.opapi.util.MemCachedManager;
import com.litb.opapi.util.RandomGenerator;

@Service
public class TokenService implements ITokenService{	
	private static Logger logger = LoggerFactory.getLogger(TokenService.class);

	@Autowired SyncService syncService;
	@Autowired IOpAppTokenDao appTokenDao;
	@Autowired MemCachedManager cacheManager;
	
	@Override
	@Transactional
	@CacheEvict(value="TokenCache", key="#appTokenRequest.getAppName().concat(#appTokenRequest.getAppUserKey())")
	public OpAppToken createAppToken(AppTokenRequest appTokenRequest) throws ApiException {
		// use synchronized method to create app token.
		logger.info(String.format("createAppToken for %s->%s.", 
				appTokenRequest.getAppName(), appTokenRequest.getAppUserKey()));
		
		OpAppToken appToken = syncService.createAppToken(appTokenRequest);
		return appToken;
	}

	@Override
	@Transactional
	public OpAppToken internalCreateAppToken(AppTokenRequest appTokenRequest) throws ApiException {
		// TODO Auto-generated method stub
		OpAppToken appToken = this.queryAppToken(appTokenRequest);
		if(appToken!=null){
			logger.error("appToken已存在!");
			throw new ApiException(ErrorCode.APPTOKEN_EXISTS_EXCEPTION);
		}
		
		appToken =new OpAppToken();
		appToken.setAppName(appTokenRequest.getAppName());
		appToken.setAppUserKey(appTokenRequest.getAppUserKey());
		appToken.setAppToken(RandomGenerator.randomAppToken());
		appToken.setAppSecret(RandomGenerator.randomAppSecret());		
		appToken.setCreateDate(new Date());
		
		appToken = this.appTokenDao.insert(appToken);

		logger.info(String.format("Create OpAppToken[%d] for tokenRequest[%s-> %s] with token[%s].", appToken.getTokenId(), 
				appToken.getAppName(), appToken.getAppUserKey(), appToken.getAppToken()));
		return appToken;
	}

	@Override
	@Transactional
	@CacheEvict(value="TokenCache", key="#appTokenRequest.getAppName().concat(#appTokenRequest.getAppUserKey())")
	public boolean deleteAppToken(AppTokenRequest appTokenRequest) throws ApiException {
		logger.info(String.format("deleteAppToken for %s->%s.", 
				appTokenRequest.getAppName(), appTokenRequest.getAppUserKey()));

		OpAppToken appToken = this.appTokenDao.findAppToken(appTokenRequest.getAppName(), 
				appTokenRequest.getAppUserKey());
		if(appToken==null){
			logger.warn("appToken不存在!");
			throw new ApiException(ErrorCode.APPTOKEN_NOT_EXISTS_EXCEPTION);
		}
		
		appTokenDao.delete(appToken);
		return true;
	}

	@Override
	@Transactional
	@CacheEvict(value="TokenCache", key="#appTokenRequest.getAppName().concat(#appTokenRequest.getAppUserKey())")
	public OpAppToken resetAppToken(AppTokenRequest appTokenRequest) throws ApiException {
		logger.info(String.format("resetAppToken for %s->%s.", 
				appTokenRequest.getAppName(), appTokenRequest.getAppUserKey()));

		OpAppToken appToken = this.appTokenDao.findAppToken(appTokenRequest.getAppName(), 
				appTokenRequest.getAppUserKey());
		if(appToken==null){
			logger.warn("appToken不存在!");
			throw new ApiException(ErrorCode.APPTOKEN_NOT_EXISTS_EXCEPTION);
		} else {
			// 重置appToken
			String oldToken = appToken.getAppToken();
			String newToken = RandomGenerator.randomAppToken();
			appToken.setAppToken(newToken);
			appToken.setAppSecret(RandomGenerator.randomAppSecret());	
			// 删除cache中 token对应的密钥
			appTokenDao.update(appToken, oldToken);
			logger.info(String.format("ResetAppToken[%d] for tokenRequest[%s-> %s] with new token[%s].", appToken.getTokenId(), 
					appToken.getAppName(), appToken.getAppUserKey(), appToken.getAppToken()));
			return appToken;
		}
		
	}

	@Override
	@Cacheable(value="TokenCache", key="#appTokenRequest.getAppName().concat(#appTokenRequest.getAppUserKey())")
	public OpAppToken queryAppToken(AppTokenRequest appTokenRequest) {
		logger.info("try to query token from database.");
		OpAppToken appToken = this.appTokenDao.findAppToken(appTokenRequest.getAppName(), 
				appTokenRequest.getAppUserKey());

		return appToken;
	}

	@Override
	@Transactional
	public boolean createAppInfo(String appName) {
		if(cacheManager.getAppInfoMap().containsKey(appName)==false){
			OpAppInfo appInfo = new OpAppInfo();
			appInfo.setAppName(appName);
			appInfo.setCreateDate(new Date());
			this.appTokenDao.insert(appInfo);
			logger.info("create new appInfo with name:" + appName);
		} 
		
		cacheManager.refreshAppInfoMap();
		
		return true;
	}
	
	@Override
	public OpAppToken queryAppTokenByToken(String token) {
		OpAppToken appToken = this.appTokenDao.findAppTokenByToken(token);
		return appToken;
	}

}
