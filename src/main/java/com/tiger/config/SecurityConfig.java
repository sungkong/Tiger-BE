package com.tiger.config;

import com.tiger.config.security.jwt.JwtFilter;
import com.tiger.config.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfig {

    private final TokenProvider tokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors();
        http.csrf().disable();
        http.headers().frameOptions().sameOrigin();

        http
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //
                .authorizeRequests()
                .antMatchers("/token/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/api/member/**").permitAll()
                .antMatchers("/user/**").permitAll()
                .antMatchers(HttpMethod.GET, "/profile","/health").permitAll()
                .antMatchers(HttpMethod.GET, "/api/vehicle/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/vehicle/search").permitAll()


                // 웹소캣 관련
                .antMatchers("/api/users/**", "/ws-stomp/**" ).permitAll()
                .antMatchers("/ws-stomp/**").permitAll()
                .antMatchers("/pub/**").permitAll()
                .antMatchers("/sub/**").permitAll()

                .antMatchers(HttpMethod.POST,"/chat/**").permitAll()
                .antMatchers(HttpMethod.GET,"/chat/**").permitAll()

                .antMatchers(HttpMethod.POST,"/room/**").permitAll()
                .antMatchers(HttpMethod.GET,"/room/**").permitAll()

                .antMatchers(HttpMethod.POST,"/pub/**").permitAll()
                .antMatchers(HttpMethod.GET,"/pub/**").permitAll()

                .antMatchers(HttpMethod.POST,"/sub/**").permitAll()
                .antMatchers(HttpMethod.GET,"/sub/**").permitAll()

                .antMatchers(HttpMethod.GET,"/ws-stomp/**").permitAll()
                .antMatchers(HttpMethod.POST,"/ws-stomp/**").permitAll()


                .antMatchers("/wss-stomp/**").permitAll() //

                .antMatchers("/ws/**").permitAll() //




                .anyRequest().authenticated();


        return http.build();
    }
}
