package com.litb.opapi.controller;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.litb.opapi.internal.ErrorCode;

public class OpapiBaseController {
	private static final Logger logger = LoggerFactory
			.getLogger(OpapiBaseController.class);

	protected HttpSession httpSession;
	protected HttpServletRequest request ; 
	protected HttpServletResponse response ;
	protected String appToken;
	
	public String setAppToken(String appToken) {
		return this.appToken = appToken;
	}
	public HttpSession getHttpSession() {
		return httpSession;
	}
	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	protected void logSuccessMessage(String successMessage){
		request.setAttribute("successMessage", successMessage);
	}
	
	protected void logInfoMessage(String infoMessage){
		request.setAttribute("infoMessage", infoMessage);
	}
	
	protected void logWarningMessage(String warningMessage){
		request.setAttribute("warningMessage", warningMessage);
	}
	
	protected void logErrorMessage(String errorMessage){
		request.setAttribute("errorMessage", errorMessage);
	}
	
	protected String getResultMessage(List<Object> resultMessages){
		if(resultMessages == null || resultMessages.size() == 0){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for(Object resultMessage : resultMessages){
			sb.append(resultMessage.toString() + "<br/>");
		}
		return sb.toString();
	}

	protected <T> T getErrorResponse(ErrorCode errorCode, Class<T> classType) throws Exception {
		T t = classType.newInstance();
		Method setErrorCodeMethod = classType.getMethod("setErrorCode", new Class[]{String.class});
		setErrorCodeMethod.invoke(t, errorCode.getCode());

		Method setMsgMethod = classType.getMethod("setMsg", new Class[]{String.class});
		setMsgMethod.invoke(t, errorCode.getMsg());

		Method setSuccessMethod = classType.getMethod("setSuccess", new Class[]{boolean.class});
		setSuccessMethod.invoke(t, false);
		
		return t;
	}

//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	protected <T> T forwardRequest(String requestUrl, LitbBaseRequest apiRequest) throws IOException {
//    	logger.info("Forward request to:" + requestUrl);
//		if(apiRequest.getTimestamp()==null){
//			apiRequest.setTimestamp(System.currentTimeMillis());
//		}
//		JSONObject jsonReq = JSONObject.fromObject(apiRequest);
//		String jsonResult = this.postByHttpClient(requestUrl, jsonReq.toString(), appToken);
//		
//		ObjectMapper objectMapper = new ObjectMapper();
//		Class<T> valueType = apiRequest.getResponseClass();
//		T result = objectMapper.readValue(jsonResult, valueType);
//		logger.info(String.format("Result to forwardRequest with appKey[%s] is:[%s]", appToken, result));
//		return result;
//	}
//
//	protected String postByHttpClient(String url, String data, String appToken) throws IOException {
//        HttpClient httpClient = HttpClientUtils.createHttpClient();
//        return HttpClientUtils.postJsonRequest(httpClient, url, data, appToken);
//    }
//
//	protected LitbBaseResponse getErrorResponse(ErrorCode errorCode) throws Exception {
//		LitbBaseResponse apiResponse = new LitbBaseResponse();
//		apiResponse.setErrorCode(errorCode.code());
//		apiResponse.setMsg(errorCode.msg());
//		apiResponse.setSuccess(false);
//		return apiResponse;
//	}
//	protected JSONObject getJsonObjectFromJdata(JData jdata) throws Exception {
//		ObjectMapper objectMapper = new ObjectMapper();
//		return objectMapper.readValue(jdata.toString(), JSONObject.class);
//	}
//	protected JSONObject forwardRequest(String requestUrl,
//			Object requestModel, String appKey) throws IOException {
//    	logger.info("Forward request to:" + requestUrl);
//		JSONObject jsonReq = JSONObject.fromObject(requestModel);
//		String jsonResult = this.postByHttpClient(requestUrl, jsonReq.toString(), appKey);
////		jsonResult = jsonResult.replace("null", "\"\"");
////      JSONObject result = JSONObject.fromObject(jsonResult);
//		
//		ObjectMapper objectMapper = new ObjectMapper();
//		JSONObject result = objectMapper.readValue(jsonResult, JSONObject.class);
//		logger.info(String.format("Result to updateVirtualStock with appKey[%s] is:[%s]", appKey, result));
//		return result;
//	}
//	protected LitbBaseResponse getReseponseFromJdata(JData jdata) throws Exception {
//		ObjectMapper objectMapper = new ObjectMapper();
//		return objectMapper.readValue(jdata.toString(), LitbBaseResponse.class);
//	}
   
}
