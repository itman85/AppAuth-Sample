package com.google.codelabs.appauth;

import net.openid.appauth.AuthorizationRequest;

public class EndSessionRequestWrapper {

    private AuthorizationRequest authorizationRequest;

    private String tokenId;

    public EndSessionRequestWrapper(AuthorizationRequest authorizationRequest, String tokenId) {
        this.authorizationRequest = authorizationRequest;
        this.tokenId = tokenId;
    }

    public AuthorizationRequest getAuthorizationRequest() {
        return authorizationRequest;
    }

    public void setAuthorizationRequest(AuthorizationRequest authorizationRequest) {
        this.authorizationRequest = authorizationRequest;
    }

    public CustomAuthorizationServiceConfiguration getConfiguration(){
        if(authorizationRequest.configuration instanceof CustomAuthorizationServiceConfiguration)
            return (CustomAuthorizationServiceConfiguration) authorizationRequest.configuration;
        return null;
    }

    public String getTokenId() {
        return tokenId;
    }
}
