package com.example.todoproject.common.oauth;

import com.example.todoproject.common.oauth.googleuser.GoogleOAuth2UserInfo;
import com.example.todoproject.user.domain.User;
import com.example.todoproject.user.domain.UserRole;

public record OAuthAttributes(
        String nameAttributeKey,
        GoogleOAuth2UserInfo oauth2UserInfo
) {
    public User toEntity(GoogleOAuth2UserInfo oauth2UserInfo) {
        return User.builder()
                .email(oauth2UserInfo.getEmail())
                .socialId(oauth2UserInfo.getId())
                .name(oauth2UserInfo.getName())
                .userRole(UserRole.USER)
                .build();
    }
}
