package com.litb.opapi.test.client;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.litb.opapi.client.LitbDefaultClient;
import com.litb.opapi.request.AppTokenRequest;
import com.litb.opapi.response.AppTokenResponse;

public class TokenClientTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String appToken = "VZDFJFGLVUITNJQRWCWZUKFHMDXIBARI";
		String appSecret = "VJUCIWDZQEEPCZUAPBIVKYBODBHPQBHI";
		String apiUrl = "http://127.0.0.1:8091/op/app/token.validate";
		
		LitbDefaultClient client = new LitbDefaultClient(apiUrl, appToken, appSecret);

		String appName = "sp";		// 应用名称
		String appUserKey = "1";	// 应用用户关键字
		AppTokenRequest requestMode = new AppTokenRequest(appName, appUserKey);
		AppTokenResponse apiResponse;
		try {
			apiResponse = client.execute(requestMode);
			System.out.println("apiResponse =" + apiResponse);
			if(apiResponse.isSuccess()){
				System.out.println("apiResponse.isSuccess():" + apiResponse.isSuccess());
			} else {
				System.out.println("errorCode is:" + apiResponse.getErrorCode());
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
