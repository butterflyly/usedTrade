package com.example.usedTrade.Security.OAuth2;

public interface OAuth2UserInfo {

    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
    String getRole();

}