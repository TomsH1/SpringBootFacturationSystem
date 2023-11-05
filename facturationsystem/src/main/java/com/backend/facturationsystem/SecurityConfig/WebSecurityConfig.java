package com.backend.facturationsystem.SecurityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    //? Método interceptor de peticiones HTTP
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //* Aceptar cualquier tipo de petición de tipo GET perteneciente a esta ruta
                .antMatchers(HttpMethod.GET,"/Api/Clients", "/chat-websocket/**")
                .permitAll()
                //* Para las demás peticiones en otras rutas de la página exige autenticarse
                .anyRequest()
                .authenticated()
                .and()
                .cors()
                .and()
                //* Deshabilitar la protección contra CROSS SITE REQUEST FORGERY
                .csrf().disable()
                //* Habilitar la configuración sin estado y denegar el uso de sesiones HTTP desde el propio servidor
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
