package org.skmnservice.boardapp;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // jwt 사용하기 때문에 csrf 보호 없앰.

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("api/posts/write", "api/posts/edit/**", "api/posts/delete/**").authenticated()
                        .anyRequest().permitAll() // 그 외 요청은 인증 필요
                )
                .formLogin(form -> form
                        .loginPage("/auth/login") // 사용자 정의 로그인 페이지 경로
                        .loginProcessingUrl("/auth/login") // 로그인 처리 URL
                        .defaultSuccessUrl("/api/posts", true) // 로그인 성공 시 리다이렉트 경로
                        .failureUrl("/auth/login?error=true") // 로그인 실패 시 경로
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/auth/logout") // 로그아웃 처리 URL
                        .logoutSuccessUrl("/api/posts") // 로그아웃 성공 시 경로
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 세션 쿠키 삭제
                );
        return http.build();
    }

    /**
     * 사용자 인증 처리
     * @param configuration 아이디, 패스워드를 가지고 인증
     * @return 인증된 Authentication 객체 반환
     * @throws "AuthenticationException" 발생
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration conf = new CorsConfiguration();
        conf.addAllowedOrigin("http://localhost:8080");
        conf.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE"));
        conf.setAllowedHeaders(Arrays.asList("Set-Cookie", "Content-Type"));
        conf.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", conf);
        return source;
    }
}
