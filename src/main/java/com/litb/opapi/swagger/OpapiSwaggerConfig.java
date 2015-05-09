package com.litb.opapi.swagger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.paths.SwaggerPathProvider;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;

@EnableSwagger
public class OpapiSwaggerConfig {

	private SpringSwaggerConfig springSwaggerConfig;

	/**
	 * Required to autowire SpringSwaggerConfig
	 */
	@Autowired
	public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
		this.springSwaggerConfig = springSwaggerConfig;
	}

	/**
	 * Every SwaggerSpringMvcPlugin bean is picked up by the swagger-mvc
	 * framework - allowing for multiple swagger groups i.e. same code base
	 * multiple swagger resource listings.
	 */
	@Bean
	// Don't forget the @Bean annotation
	public SwaggerSpringMvcPlugin customImplementation() {
		return new SwaggerSpringMvcPlugin(this.springSwaggerConfig).
				pathProvider(new OpapiSwaggerPathProvider()).
				apiInfo(apiInfo()).
				includePatterns(".*/app/.*"); // 只扫描RequestMapping中包含"/app/token."的Rest API:   .*/app/token..*
	} 

	private ApiInfo apiInfo() {
		ApiInfo apiInfo = new ApiInfo("Token服务器API接口列表", "", "", "", "", "");
		// ApiInfo apiInfo = new ApiInfo("My Apps API Title",
		// "My Apps API Description", "My Apps API terms of service",
		// "My Apps API Contact Email", "My Apps API Licence Type",
		// "My Apps API License URL");
		return apiInfo;
	}

}

class OpapiSwaggerPathProvider extends SwaggerPathProvider{

	@Override
	protected String applicationPath() {
		// TODO Auto-generated method stub
		return "/op";
	}

	@Override
	protected String getDocumentationPath() {
		// TODO Auto-generated method stub
		return "";
	}
}	

