package com.litb.opapi.test.memcached;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

//public class MemCachedTest extends TestCaseBase {
public class MemCachedTest  {

	public static void main(String[] args) throws InterruptedException{
		Map<String, String> map = new HashMap<String, String>();
		map.put("key1", "杨阔");
		map.put("key2", "2杨2阔");
		
		MemcachedClient memcachedClient = MemCachedManagerTest.getInstance();
		memcachedClient.add("test", 5, map);
		
		System.out.println(memcachedClient.get("test"));
		Thread.sleep(2000);
		System.out.println(memcachedClient.get("test"));
		Thread.sleep(2000);
		System.out.println(memcachedClient.get("test"));
		Thread.sleep(2000);
		System.out.println(memcachedClient.get("test"));
		Thread.sleep(2000);
		System.out.println("finished");
	}
}
class MemCachedManagerTest{  
    private static MemcachedClient c;  
    public static synchronized MemcachedClient getInstance(){  
        if(c==null){  
            try {  
                c = new MemcachedClient(  
                        AddrUtil.getAddresses("192.168.66.103:11211"));  
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        }  
        return c;  
    }
}
