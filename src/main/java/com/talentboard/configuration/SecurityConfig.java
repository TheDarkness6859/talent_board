package com.talentboard.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Objects;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/css/**", "/js/**", "/img/**", "/webjars/**").permitAll()
                                .requestMatchers("/", "/auth/**", "/users/register").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/vacancies").authenticated()
                                .requestMatchers("/applications/**").authenticated()
                                .requestMatchers("/interviews/**").authenticated()
                                .requestMatchers("/users").hasRole("ADMIN")
                                .requestMatchers("/users/profile").authenticated()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/recruiter/**").hasRole("RECRUITER")
                                .requestMatchers("/user/**").hasRole("CANDIDATE")
                                .anyRequest().authenticated()

                )

                .formLogin(form ->
                        form.loginPage("/auth/login")
                                .loginProcessingUrl("/auth/login")
                                .successHandler(((request, response, authentication) -> {

                                    var roles =authentication.getAuthorities();
                                    boolean isAdmin = roles.stream().anyMatch(auth -> Objects.equals(auth.getAuthority(), "ROLE_ADMIN"));
                                    boolean isRecruiter = roles.stream().anyMatch(auth -> Objects.equals(auth.getAuthority(),"ROLE_RECRUITER"));
                                    boolean isCandidate = roles.stream().anyMatch(auth -> Objects.equals(auth.getAuthority(), "ROLE_CANDIDATE"));

                                    if(isAdmin) response.sendRedirect("/users");
                                    else if (isRecruiter) response.sendRedirect("/vacancies");
                                    else if (isCandidate) response.sendRedirect("/applications/my");
                                    else response.sendRedirect("/");

                                }
                                )).failureUrl("/auth/login?error=true")
                                .permitAll()

                        )

                .logout(logout ->
                        logout.logoutUrl("/auth/logout")
                                .logoutSuccessUrl("/auth/login?logout=true")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .permitAll()
                )

                .csrf(AbstractHttpConfigurer::disable
                )

                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                );

        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
