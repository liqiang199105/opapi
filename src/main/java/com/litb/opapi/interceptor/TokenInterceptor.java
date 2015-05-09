package com.litb.opapi.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.litb.opapi.annotation.IgnoreTokenValidation;
import com.litb.opapi.controller.OpapiBaseController;
import com.litb.opapi.exception.ApiException;
import com.litb.opapi.internal.ErrorCode;

public class TokenInterceptor extends HandlerInterceptorAdapter {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean preHandle(HttpServletRequest request,
	HttpServletResponse response, Object handler) throws Exception {
		String actionName = request.getRequestURI();
		logger.info("---用户访问:" + actionName + "---");

		if(handler.getClass().equals(HandlerMethod.class)){
			if(((HandlerMethod)handler).getBean() instanceof OpapiBaseController){
				OpapiBaseController bc = (OpapiBaseController)((HandlerMethod)handler).getBean() ;
		        bc.setRequest(request);
		        bc.setResponse(response);
		        bc.setHttpSession(request.getSession());

		        HandlerMethod handlerMethod = (HandlerMethod) handler;  
		        if(handlerMethod.getMethodAnnotation(IgnoreTokenValidation.class)==null){
		        	// 所有没有@IgnoreTokenValidation的请求都需要在head里面添加appToken属性
					if(request.getHeader("appToken") == null){
						throw new ApiException(ErrorCode.HEADER_MISSING_APPTOKEN_EXCEPTION);
					} else {
						bc.setAppToken(request.getHeader("appToken"));
					}
		        }
			}
		}
				
        return super.preHandle(request, response, handler);
	}


}
