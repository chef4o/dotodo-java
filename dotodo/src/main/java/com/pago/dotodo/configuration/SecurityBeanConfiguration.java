package com.pago.dotodo.configuration;

import com.pago.dotodo.model.enums.Role;
import com.pago.dotodo.repository.UserRepository;
import com.pago.dotodo.service.AppUserDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class SecurityBeanConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/", "/home", "/contacts", "/news", "/about-us", "auth/login", "/auth/register").permitAll()
                        .requestMatchers("/admin-panel").hasRole(Role.ADMIN.name())
                        .requestMatchers("/notes", "/checklists", "/profile").authenticated()
                        .anyRequest().authenticated())
//                .formLogin(login -> login
//                        .loginPage("/auth/login")
//                        .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
//                        .passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
//                        .defaultSuccessUrl("/")
//                        .failureForwardUrl("/auth/login-error"))
//                .logout(logout -> logout
//                        .logoutUrl("/auth/logout")
//                        .invalidateHttpSession(true)
//                        .logoutSuccessUrl("/"))
//                .exceptionHandling(error -> error.accessDeniedPage("/unauthorized"))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(ModelMapper modelMapper, UserRepository userRepository) {
        return new AppUserDetailsService(modelMapper, userRepository);
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
