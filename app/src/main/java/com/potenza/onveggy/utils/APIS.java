package com.potenza.onveggy.utils;

import com.ciyashop.library.apicall.URLS;

public class APIS {

    //TODO:Copy and Paste URL and Key Below from Admin Panel.
    public final String APP_URL = "https://ciyashopapp.potenzaglobalsolutions.com/onveggie/";
    public final String WOO_MAIN_URL = APP_URL + "wp-json/wc/v2/";
    public final String MAIN_URL = APP_URL + "wp-json/pgs-woo-api/v1/";

    public static final String CONSUMERKEY = "BsNDjFDTh3dt";
    public static final String CONSUMERSECRET = "jd8Z5klnOtvPNg92EME3Eic2QkcOndzNx4zuzIOUnsxok0ef";
    public static final String OAUTH_TOKEN = "E1lIpJweKeGBt7POWKxJzRCF";
    public static final String OAUTH_TOKEN_SECRET = "5yL76krgptdn1INhUHeyBJjTuOUvyzWnW3pQIJWyqKEf2ciE";

    public static final String WOOCONSUMERKEY = "ck_dc6e1868441adeb37881ffdaaf56133732b13d50";
    public static final String WOOCONSUMERSECRET = "cs_2a6e6b0113c8d65f2fda1556795c9a1a93b2be6b";
    public static final String version = "4.3.0";
    public static final String purchasekey = "1b30c152-e4f3-42fa-bf76-6c2ffcd3fa08";


    public APIS() {
        URLS.APP_URL = APP_URL;
        URLS.NATIVE_API = APP_URL + "wp-json/wc/v3/";
        URLS.WOO_MAIN_URL = WOO_MAIN_URL;
        URLS.MAIN_URL = MAIN_URL;
        URLS.version = version;
        URLS.CONSUMERKEY = CONSUMERKEY;
        URLS.CONSUMERSECRET = CONSUMERSECRET;
        URLS.OAUTH_TOKEN = OAUTH_TOKEN;
        URLS.OAUTH_TOKEN_SECRET = OAUTH_TOKEN_SECRET;
        URLS.WOOCONSUMERKEY = WOOCONSUMERKEY;
        URLS.WOOCONSUMERSECRET = WOOCONSUMERSECRET;
        URLS.PURCHASE_KEY = purchasekey;
    }
}