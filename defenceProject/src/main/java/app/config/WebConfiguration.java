package app.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableMethodSecurity
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(matcher -> matcher
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .requestMatchers("/", "/register").permitAll()
//                        .requestMatchers("/admin-panel").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin( formLogin -> formLogin
                                .loginPage("/login")
//                        .usernameParameter("username")
//                        .passwordParameter("password")
                                .defaultSuccessUrl("/user-home", true)
                                .failureUrl("/login?error")
                                .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")           // съвпада с th:action="@{/logout}"
                        .logoutSuccessUrl("/index")     // след logout ще пренасочи тук
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                );

        return httpSecurity.build();
    }
}