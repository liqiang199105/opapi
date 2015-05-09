package com.litb.opapi.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.litb.opapi.dao.entity.OpAppInfo;
import com.litb.opapi.dao.intf.IOpAppTokenDao;

public class MemCachedManager implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(MemCachedManager.class);
	
    private static MemcachedClient memcachedClient = null; 
    
    private boolean enabled;
    
    private String addresses;

	private Map<String, String> appInfoMap;
	
	@Autowired IOpAppTokenDao appTokenDao;
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		logger.info("Set MemCachedManager.enabled to: " + this.enabled);
	}

    public String getAddresses() {
		return addresses;
	}

	public void setAddresses(String addresses) {
		this.addresses = addresses;
		logger.info("Set MemCachedManager.addresses to: " + addresses);
	}

	public static MemcachedClient getMemcachedClient(){ 
        return memcachedClient;  
    }

	@Override
	public void afterPropertiesSet() throws Exception {
		if(enabled==true){
			// 初始化memcachedClient
	        try {  
	        	memcachedClient = new MemcachedClient(  
	                    AddrUtil.getAddresses(getAddresses()));  
	        } catch (IOException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
		}
        
        // 初始化appInfo
		refreshAppInfoMap();
	}

	public void refreshAppInfoMap() {
        appInfoMap = new HashMap<String, String>();
        List<OpAppInfo> list = appTokenDao.getAllAppInfo();
        for(OpAppInfo appInfo: list){
        	appInfoMap.put(appInfo.getAppName(), appInfo.getAppName());
        }
		logger.info("Refresh appInfoMap to: " + appInfoMap);
	}

	public Map<String, String> getAppInfoMap() {
		return appInfoMap;
	}

	public void setAppInfoMap(Map<String, String> map) {
		appInfoMap = map;
		logger.info("Set appInfoMap to: " + appInfoMap);
	}
}
