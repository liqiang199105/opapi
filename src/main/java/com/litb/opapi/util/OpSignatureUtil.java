package com.litb.opapi.util;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.StringUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class OpSignatureUtil {
	private static final String Algorithm = "DESede"; //定义 加密算法
	 
	/**
	 * 验证sha1签名，验证通过返回true，否则返回false
	 * @param signature
	 * @param nonce
	 * @param timestamp
	 * @param app_key
	 * @return
	 */
	public static boolean validateSHA(String signature, String nonce,
			String timestamp, String appKey) {
		if (signature == null || nonce == null || timestamp == null) {
			return false;
		}
		String sign = sha1(getSignContent(nonce, timestamp, appKey));
		if (!signature.equals(sign)) {
			return false;
		}
		return true;
	}

	/**
	 * 生成sha1签名
	 * @param nonce
	 * @param timestamp
	 * @param appKey
	 */
	public static String generateSignature(String nonce, String timestamp, String appKey){
		return sha1(getSignContent(nonce, timestamp, appKey));
	}
	
	/**
	 * 生产sha1签名
	 * @param strSrc
	 * @return
	 */	
	private static String sha1(String strSrc) {
		MessageDigest md = null;
		String strDes = null;

		byte[] bt = strSrc.getBytes();
		try {
			md = MessageDigest.getInstance("SHA-1");
			md.update(bt);
			strDes = bytes2Hex(md.digest()); // to HexString
		} catch (NoSuchAlgorithmException e) {
			//TODO
			e.printStackTrace();
		}
		return strDes;
	}

	private static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;

		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));

			if (tmp.length() == 1) {
				des += "0";
			}

			des += tmp;
		}

		return des;
	}

	/**
	 * 对非空参数按字典顺序升序构造签名串
	 * 
	 * @param params
	 * @return
	 */
	private static String getSignContent(String... params) {
		List<String> list = new ArrayList<String>(params.length);
		for(String temp : params){
			if(StringUtils.isNotBlank(temp)){
				list.add(temp);
			}	
		}
		Collections.sort(list);
		StringBuilder strBuilder = new StringBuilder();
		for (String element : list) {
			strBuilder.append(element);
		}
		return strBuilder.toString();
	}

	/**
	 * 加密
	 * 
	 * @param keybyte
	 * @param src
	 * @return
	 */
	private static String encryptMode(String key, String src) {
		try {
			Security.addProvider(new com.sun.crypto.provider.SunJCE());

	        byte[] keyByte = key.getBytes();
			
			SecretKey deskey = new SecretKeySpec(keyByte, Algorithm);
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			BASE64Encoder enc = new BASE64Encoder();
			String result = enc.encode(c1.doFinal(src.getBytes("UTF-8")));
			return result;
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param key
	 * @param src
	 * @return
	 */
	private static String decryptMode(String key, String src) {
		try {
			
			Security.addProvider(new com.sun.crypto.provider.SunJCE());

	        byte[] keyByte = key.getBytes();
			
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keyByte, Algorithm);

			// 解密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			BASE64Decoder dec = new BASE64Decoder();
			byte[] bytes = dec.decodeBuffer(src);
			String result = new String(c1.doFinal(bytes));
			return result;
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

    private static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            String hex;
            if ((hex = Integer.toHexString(bytes[i] & 255)).length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }

    private static byte[] hex2byte(String hexStr) {
    	byte[] b = hexStr.getBytes();
        if((b.length%2)!=0)
        	throw new IllegalArgumentException("长度不是偶数");

      	byte[] b2 = new byte[b.length/2];
      	for (int n = 0; n < b.length; n+=2) {
      		String item = new String(b,n,2);
      		b2[n/2] = (byte)Integer.parseInt(item,16);
      	}
        return b2;
    }

    private static byte[] encryptMD5(String data) throws IOException {
    	byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            bytes = md.digest(data.getBytes("UTF-8"));
        }
        catch (GeneralSecurityException gse) {
            throw new IOException(gse);
        }
        return bytes;
    }
    
    /** 
     * 可逆的加密算法 执行一次是加密，执行第两次是解密 
     */   
    public static String convertMD5(String inStr){  
        char[] a = inStr.toCharArray();  
        for (int i = 0; i < a.length; i++){  
            a[i] = (char) (a[i] ^ 't');  
        }  
        String s = new String(a);  
        return s;    
    }
    
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String signature = "90e4c22c90a58f26526c2dd5b6c56c8822edeaa1";
		String nonce = "57155157";
		String timestamp = "1397022061823";
		String appKey = "xyz123xyz";
		
		System.out.println(System.currentTimeMillis());
		
		boolean validateResult = OpSignatureUtil.validateSHA(signature, nonce, timestamp, appKey);
		System.out.println(validateResult);
		
		String key = "90e4c22c90a58f26526casda"; // "90e4c22c90a58f26526casda"; //"LITBU89D3FchIkhKyMma6FiI";
		String data = "中国1397022061823";
		String encoded = OpSignatureUtil.encryptMode(key, data);
		System.out.println("encoded to:" + encoded);

		String decoded = OpSignatureUtil.decryptMode(key, encoded);
		System.out.println("decoded to:" + decoded);
		
		String hexStr = byte2hex(data.getBytes());
		System.out.println("hexStr is:" + hexStr);
		byte[] bytes = hex2byte(hexStr);
		String newStr = new String(bytes);
		System.out.println("newStr is:" + newStr);
		
		String md5HexStr = byte2hex(encryptMD5(data));
		System.out.println("md5HexStr is:" + md5HexStr);
		
		String convertStr = convertMD5(md5HexStr);
		System.out.println("convertStr1 is:" + convertStr);
		convertStr = convertMD5(convertStr);
		System.out.println("convertStr2 is:" + convertStr);
		bytes = hex2byte(hexStr);
		System.out.println("newStr is:" + new String(bytes));
	}

}
