package com.business.inventra.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class KeyGenerator {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final Random random = new Random();
    
    public static String generateKey(String entityPrefix) {
        String timestamp = LocalDateTime.now().format(formatter);
        String randomId = generateRandomString(8); // 8 characters random string
        return String.format("%s_%s_%s", entityPrefix, timestamp, randomId);
    }
    
    private static String generateRandomString(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
    
    // Constants for entity prefixes
    public static final String USER_PREFIX = "usr";
    public static final String ORGANIZATION_PREFIX = "org";
    public static final String BRANCH_PREFIX = "brn";
    public static final String ROLE_PREFIX = "rol";
    public static final String ADDRESS_PREFIX = "adr";
    public static final String CONTACT_PREFIX = "cnt";
    public static final String PRODUCT_CATEGORY_PREFIX = "ctg";
    public static final String PRODUCT_PREFIX = "prd";
    public static final String UNIT_PREFIX = "unt";
    public static final String STOCK_PREFIX = "stk";
    public static final String AUDIT_PREFIX = "aud";
    public static final String SUBSCRIPTION_PREFIX = "sub"; 
    public static final String CUSTOMER_PREFIX = "cus";
    public static final String CUSTOMER_BRANCH_PREFIX="CBM";
	public static final String USER_BRANCH_PREFIX = "UBM";
    
} 