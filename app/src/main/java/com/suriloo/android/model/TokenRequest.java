package com.suriloo.android.model;

public class TokenRequest {
    private String idToken;
    public TokenRequest(String idToken){
        this.idToken=idToken;
    }
    public String getIdToken(){
        return idToken;
    }
}
