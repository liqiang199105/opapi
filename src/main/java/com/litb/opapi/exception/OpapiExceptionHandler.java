package com.litb.opapi.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class OpapiExceptionHandler implements HandlerExceptionResolver{
	private final Logger logger = LoggerFactory
			.getLogger(OpapiExceptionHandler.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		// TODO Auto-generated method stub
		if (ex instanceof com.litb.opapi.exception.ApiException) {
			ApiException apiEx = (ApiException) ex;
			sendExceptionInfo(response, apiEx);
			// return new ModelAndView();
		} else {
			// other exceptions TBD
			sendExceptionInfo(response, ex);
		}
//		ex.printStackTrace();
		return null;
	}

	private void sendExceptionInfo(HttpServletResponse response,
			ApiException apiEx) {
		try {
			response.setContentType("application/json;charset=utf-8");
			String errorJsonData = String.format(
					"{\"success\":false, \"errorCode\":\"%s\",  \"msg\":\"%s\"}",
					apiEx.getErrCode(), apiEx.getErrMsg());

			logger.error("errorJsonData=" + errorJsonData + " \t"
					+ apiEx.getErrMsg());
			response.getWriter().write(errorJsonData);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendExceptionInfo(HttpServletResponse response, Exception ex) {
		try {
			response.setContentType("application/json;charset=utf-8");
			String errorJsonData = String.format(
					"{\"success\":false, \"msg\":\"UNKNOWN [%s]\"}",
					ex.getMessage());

			logger.error("errorJsonData=" + errorJsonData + " \t"
					+ ex.getMessage());
			response.getWriter().write(errorJsonData);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
