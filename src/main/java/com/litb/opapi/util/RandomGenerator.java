package com.litb.opapi.util;
import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;

public class RandomGenerator {

    public static String randomDevelperName() {
    	String dateStr = DateUtil.getStringDate(new Date(), "yyMMdd");
        return dateStr + "_" + RandomStringUtils.randomAlphanumeric(8);
    }
    
    public static String randomDevelperPassword() {
        return RandomStringUtils.randomAlphanumeric(8);
    }

    public static String randomAppKey() {
    	return RandomStringUtils.randomAlphabetic(32).toLowerCase();
    }

    public static String randomAppToken() {
        return RandomStringUtils.randomAlphabetic(32).toUpperCase();
    }

    public static String randomAppSecret() {
        return RandomStringUtils.randomAlphabetic(32).toUpperCase();
    }
}
