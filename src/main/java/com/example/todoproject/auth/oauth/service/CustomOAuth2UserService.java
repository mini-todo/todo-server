package com.example.todoproject.auth.oauth.service;

import com.example.todoproject.auth.oauth.CustomOAuth2User;
import com.example.todoproject.auth.oauth.OAuthAttributes;
import com.example.todoproject.auth.oauth.googleuser.GoogleOAuth2UserInfo;
import com.example.todoproject.user.domain.User;
import com.example.todoproject.user.repository.UserRepository;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService
        implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuthAttributes extractAttributes = new OAuthAttributes(userNameAttributeName, new GoogleOAuth2UserInfo(attributes));
        User createdUser = getUser(extractAttributes);
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdUser.getUserRole().name())),
                attributes,
                extractAttributes.nameAttributeKey(),
                createdUser.getEmail(),
                createdUser.getUserRole()
        );
    }

    private User getUser(OAuthAttributes attributes) {
        User findUser = userRepository.findBySocialId(attributes.oauth2UserInfo().getId()).orElse(null);

        if(findUser == null) {
            return saveUser(attributes);
        }
        return findUser;
    }

    private User saveUser(OAuthAttributes attributes) {
        User createdUser = attributes.toEntity(attributes.oauth2UserInfo());
        return userRepository.save(createdUser);
    }
}
