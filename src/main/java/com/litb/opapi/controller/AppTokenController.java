package com.litb.opapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.litb.opapi.annotation.IgnoreTokenValidation;
import com.litb.opapi.client.HttpClientUtils;
import com.litb.opapi.dao.entity.OpAppToken;
import com.litb.opapi.exception.ApiException;
import com.litb.opapi.internal.ErrorCode;
import com.litb.opapi.request.AppTokenRequest;
import com.litb.opapi.response.AppTokenResponse;
import com.litb.opapi.response.SignValidateResponse;
import com.litb.opapi.service.intf.ITokenService;
import com.litb.opapi.util.MemCachedManager;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Api(value="Token", description="Token API 列表", position = 1)
@Controller
@RequestMapping(value = { "/app" })
public class AppTokenController extends OpapiBaseController {
	private static final Logger logger = LoggerFactory
			.getLogger(AppTokenController.class);

	@Autowired private ITokenService tokenService;
	@Autowired MemCachedManager cacheManager;

	@ApiOperation(value = "创建Token", notes = "token is created and saved into field 'appToken'.", produces=MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "token.create", method = RequestMethod.POST)
	@IgnoreTokenValidation
	@ResponseBody
	public AppTokenResponse createAppToken(@RequestBody AppTokenRequest apiRequest) throws Exception{
		checkRequestFiled(apiRequest);
		logger.info(String.format("createAppToken for [%s->%s]:", apiRequest.getAppName(), apiRequest.getAppUserKey()));
		OpAppToken appToken = tokenService.createAppToken(apiRequest);
		
		AppTokenResponse response = new AppTokenResponse();
		response.setAppToken(appToken.getAppToken());
		response.setAppSecret(appToken.getAppSecret());
		response.setSuccess(true);
		logger.info(String.format("createAppToken for [%s->%s] with token: %s:", apiRequest.getAppName(), apiRequest.getAppUserKey(), appToken));		
		return response;
	}

	@ApiOperation(value = "删除Token", notes = "token must be existing before delete.", produces=MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "token.delete", method = RequestMethod.POST)
	@ResponseBody
	public AppTokenResponse deleteAppToken(@RequestBody AppTokenRequest apiRequest) throws Exception{
		checkRequestFiled(apiRequest);
		logger.info(String.format("deleteAppToken for [%s->%s]:", apiRequest.getAppName(), apiRequest.getAppUserKey()));
		
		AppTokenResponse response = new AppTokenResponse();
		response.setSuccess(tokenService.deleteAppToken(apiRequest));
		logger.info(String.format("deleteAppToken for [%s->%s] with result: %b:", apiRequest.getAppName(), apiRequest.getAppUserKey(), response.isSuccess()));		
		return response;
	}

	@ApiOperation(value = "重置Token", notes = "token must be existing before reset; field 'appTokenNew' is reset result.", produces=MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "token.reset", method = RequestMethod.POST)
	@IgnoreTokenValidation
	@ResponseBody
	public AppTokenResponse resetAppToken(@RequestBody AppTokenRequest apiRequest) throws Exception{
		checkRequestFiled(apiRequest);
		logger.info(String.format("resetAppToken for [%s->%s]:", apiRequest.getAppName(), apiRequest.getAppUserKey()));
		
		OpAppToken appToken = tokenService.resetAppToken(apiRequest);
		AppTokenResponse response = new AppTokenResponse();
		response.setAppToken(appToken.getAppToken());
		response.setAppSecret(appToken.getAppSecret());
		response.setAppTokenNew(appToken.getAppToken());
		response.setAppSecretNew(appToken.getAppSecret());
		response.setSuccess(true);
		logger.info(String.format("resetAppToken for [%s->%s] with result: %b:", apiRequest.getAppName(), apiRequest.getAppUserKey(), response.isSuccess()));		
		return response;
	}
	
	@ApiOperation(value = "查询Token", notes = "token is saved into field 'appToken'.", produces=MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "token.query", method = RequestMethod.POST)
	@IgnoreTokenValidation
	@ResponseBody
	public AppTokenResponse queryAppToken(@RequestBody AppTokenRequest apiRequest) throws Exception{
		checkRequestFiled(apiRequest);
		logger.info(String.format("queryAppToken for [%s->%s]:", apiRequest.getAppName(), apiRequest.getAppUserKey()));
		
		OpAppToken appToken = tokenService.queryAppToken(apiRequest);
		AppTokenResponse response = new AppTokenResponse();
		if(appToken!=null){
			response.setAppToken(appToken.getAppToken());
			response.setAppSecret(appToken.getAppSecret());			
		} 
		response.setSuccess(true);
		logger.info(String.format("queryAppToken for [%s->%s] with result: %s:", apiRequest.getAppName(), apiRequest.getAppUserKey(), appToken));		
		return response;
	}

	@ApiOperation(value = "Token校验", notes = "校验数字签名和head中的appToken.", produces=MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "token.validate", method = RequestMethod.POST)
	@ResponseBody
	public AppTokenResponse validateAppToken(@RequestBody AppTokenRequest apiRequest, 
			@RequestParam(required=true) String timestamp, @RequestParam(required=true) String sign) throws Exception{
		checkRequestFiled(apiRequest);
		Long timeDiffer;
		try {
			Assert.notNull(timestamp);
			Assert.notNull(sign);
			Long time = Long.decode(timestamp);
			timeDiffer = System.currentTimeMillis() - time;
		} catch (Exception ex) {
			logger.error("apiRequest[" + apiRequest
					+ "] missing required parameter.");
			throw new ApiException(ErrorCode.MISSING_REQUIRED_ARGUMENTS);
		}
		if (Math.abs(timeDiffer) > 30 * 60 * 1000) {
			logger.error("timestamp is not correct for request:" + apiRequest);
			throw new ApiException(ErrorCode.TIMESTAMP_NOT_VALID);
		}
		
		logger.info(String.format("validateAppToken for [%s->%s]:", apiRequest.getAppName(), apiRequest.getAppUserKey()));

		// 第一步验证header中的token是否正确
		OpAppToken appToken = tokenService.queryAppToken(apiRequest);
		if(appToken==null){
			logger.warn("token is not existing, need to create token before validateAppToken.");
			throw new ApiException(ErrorCode.APPTOKEN_NOT_EXISTS_EXCEPTION);
		}
		String rightToken = appToken.getAppToken();
		boolean validateResult = rightToken.equals(this.appToken);

		// 第一步验证加密是否正确
		if(validateResult==true){
			String appSecret = appToken.getAppSecret();
	    	String rightSign = HttpClientUtils.md5(HttpClientUtils.getSignContent(appToken.getAppToken(), timestamp, appSecret));
	    	validateResult = rightSign.equals(sign);
		}
		
		AppTokenResponse response = new AppTokenResponse();		
		response.setSuccess(validateResult);
		logger.info(String.format("validateAppToken for [%s->%s] with result: %b:", apiRequest.getAppName(), apiRequest.getAppUserKey(), response.isSuccess()));		
		return response;
	}

	@ApiOperation(value = "数字签名校验", notes = "根据head中的appToken校验数字签名是否正确.", produces=MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "sign.validate", method = RequestMethod.GET)
	@ResponseBody
	public SignValidateResponse validateSign(@RequestParam(required=true) String timestamp, @RequestParam(required=true) String sign) throws Exception{
//		checkRequestFiled(apiRequest);
		Long timeDiffer;
		try {
			Assert.notNull(timestamp);
			Assert.notNull(sign);
			Long time = Long.decode(timestamp);
			timeDiffer = System.currentTimeMillis() - time;
		} catch (Exception ex) {
			logger.error("missing required parameter.");
			throw new ApiException(ErrorCode.MISSING_REQUIRED_ARGUMENTS);
		}
		if (Math.abs(timeDiffer) > 30 * 60 * 1000) {
			logger.error("timestamp is not correct for request timestamp:" + timestamp);
			throw new ApiException(ErrorCode.TIMESTAMP_NOT_VALID);
		}
		
		// 第一步验证header中的token是否正确
		OpAppToken appToken = tokenService.queryAppTokenByToken(this.appToken);
		if(appToken==null){
			logger.warn("token is not existing, need to create token before validateAppToken.");
			throw new ApiException(ErrorCode.APPTOKEN_NOT_EXISTS_EXCEPTION);
		}

		// 验证加密是否正确
		String appSecret = appToken.getAppSecret();
    	String rightSign = HttpClientUtils.md5(HttpClientUtils.getSignContent(appToken.getAppToken(), timestamp, appSecret));
    	boolean validateResult = rightSign.equals(sign);
		
    	SignValidateResponse response = new SignValidateResponse();		
		response.setSuccess(validateResult);
		response.setAppName(appToken.getAppName());
		response.setAppUserKey(appToken.getAppUserKey());
		logger.info(String.format("validateAppToken for [%s->%s] with result: %b:", this.appToken, sign, response.isSuccess()));		
		return response;
	}
	
//	@ApiIgnore
	@ApiOperation(value = "平台注册", notes = "注册新的应用平台", produces=MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/{appName}/appInfo.create", method = RequestMethod.GET)
	@IgnoreTokenValidation
	@ResponseBody
	public AppTokenResponse createAppInfo(@ApiParam( value = "应用平台的名称", required = true ) @PathVariable("appName") String appName) throws ApiException{
		Assert.hasLength(appName);
		logger.info("Goto createAppInfo with appName:" + appName);

		boolean result =  tokenService.createAppInfo(appName);
		AppTokenResponse response = new AppTokenResponse();		
		response.setSuccess(result);
		logger.info(String.format("createAppInfo with result: %b:", response.isSuccess()));		
		return response;
	}

	private void checkRequestFiled(AppTokenRequest apiRequest) throws Exception{
		try{
			Assert.hasLength(apiRequest.getAppName());
			Assert.hasLength(apiRequest.getAppUserKey());
		} catch(Exception ex){
			logger.error("apiRequest[" + apiRequest + "] missing required parameter.");
			throw new ApiException(ErrorCode.MISSING_REQUIRED_ARGUMENTS);
		}
		
		if(cacheManager.getAppInfoMap().containsKey(apiRequest.getAppName())==false){
			throw new ApiException(ErrorCode.APPNAME_NOT_EXISTS_EXCEPTION);
		}
	}
	
}
