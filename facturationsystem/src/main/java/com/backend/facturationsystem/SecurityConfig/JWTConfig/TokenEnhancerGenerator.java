package com.backend.facturationsystem.SecurityConfig.JWTConfig;

import com.backend.facturationsystem.models.entities.User;
import com.backend.facturationsystem.models.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TokenEnhancerGenerator implements TokenEnhancer {
    @Autowired
    private UserServices userServices;
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        User user = userServices.findByUsername(authentication.getName());
        Map<String, Object> jwAdditionalInfo = new HashMap<>();
        jwAdditionalInfo.put("id", user.getId());
        jwAdditionalInfo.put("roles", user.getRoles());
        jwAdditionalInfo.put("nombre", user.getNombre());
        jwAdditionalInfo.put("apellido", user.getApellido());
        jwAdditionalInfo.put("email", user.getEmail());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(jwAdditionalInfo);

        return accessToken;
    }
}
