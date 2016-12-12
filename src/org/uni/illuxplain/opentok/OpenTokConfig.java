package org.uni.illuxplain.opentok;

public class OpenTokConfig {
	
    public static  String sessionId;
    public static String tokenKey;
    
    public static final String API_KEY = "45378592";
    public static final boolean SUBSCRIBE_TO_SELF = false;
    
    
    public OpenTokConfig(String sessionId, String tokenKey) {
    	this.sessionId = sessionId;
    	this.tokenKey = tokenKey;
    }
    
//    public void setSessionID(String newSessionId){
//    	this.sessionId = newSessionId;
//    }
//    
//    public void setTokenID(String newTokenKey){
//    	tokenKey = newTokenKey;
//    }
    
}
