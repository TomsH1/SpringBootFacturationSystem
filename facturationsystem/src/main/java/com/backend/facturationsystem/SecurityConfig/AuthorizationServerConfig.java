package com.backend.facturationsystem.SecurityConfig;

import com.backend.facturationsystem.SecurityConfig.JWTConfig.TokenEnhancerGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{

    @Autowired
    private TokenEnhancerGenerator tokenEnhancer;
    private final Logger logger = LoggerFactory.getLogger(AuthorizationServerConfig.class);
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Value("${clientApp}")
    private String clientApp;
    @Value("${clientSecretpassword}")
    private String client_password;
    @Value("${RSA_private_key}")
    private String RSA_PRIVATE_KEY;
    @Value("${RSA_public_key}")
    private String RSA_PUBLIC_KEY;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    //*Método para configurar las propiedades de conexión con el cliente de Angular
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //* Otorgar permisos a todos los usuarios para autenticarse
        security.tokenKeyAccess("permitAll()")
        //* Validar el token que se está enviando
        .checkTokenAccess("isAuthenticated()");
    }

    @Override
    //*Método para configurar las propiedades de conexión con el cliente de Angular
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient(this.clientApp)
                .secret(this.passwordEncoder.encode(this.client_password))
                .scopes("read", "write")
                //*Establecer los granTypes de tipo password
                .authorizedGrantTypes("password", "refresh_tokens")
                .accessTokenValiditySeconds(3600 * 2)
                .refreshTokenValiditySeconds(3600 * 2);
        super.configure(clients);
    }

    @Override
    //*Método para configurar los endpoints en los cuales se requerirá autenticación
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        //*Agregar contenido extra al cuerpo JWT
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(this.tokenEnhancer, this.accessTokenConverter()));
        //*Agregar el administrador de autenticación
        endpoints
            .authenticationManager(this.authenticationManager)
            .accessTokenConverter(this.accessTokenConverter())
            .tokenEnhancer(tokenEnhancerChain);
    }
    @Bean
    public JwtAccessTokenConverter accessTokenConverter(){
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        //* agregar clave pública y privada en formato RSA256
        jwtAccessTokenConverter.setSigningKey(this.RSA_PRIVATE_KEY);
        jwtAccessTokenConverter.setVerifierKey(this.RSA_PUBLIC_KEY);
        return jwtAccessTokenConverter;
    }
}