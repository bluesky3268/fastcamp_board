package com.fastcapmus.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    /**
     * spring boot 2.7 부터는
     * 기존 WebSecurityConfigurerAdapter를 상속받아서 하는 방법이 아니라
     * Bean Component 방식으로 바뀜.
     * -> 빈으로 등록해서 사용해야 한다.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .formLogin().and()
                .build();
    }
}
