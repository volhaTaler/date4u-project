package com.tutego.date4u.core.config;


import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    
    
    //@Autowired
    private UserDetailsServiceConfiguration unicornService;

    public SecurityConfig(UserDetailsServiceConfiguration unicornService) {
        this.unicornService = unicornService;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    
    
    @Bean
    public AuthenticationManager authenticationProvider(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(unicornService)
                .and()
                .build();
    }
    
    
    @Bean
    public SecurityFilterChain filterChain( HttpSecurity http ) throws Exception {
        http.authorizeRequests((requests) -> requests.antMatchers("/registration","/")
                .permitAll().anyRequest().authenticated())
                .formLogin((form) -> form.loginPage("/login").defaultSuccessUrl( "/profile", true )
                        //.failureUrl("/error")
                        .permitAll())
                .logout((logout) -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        //logoutSuccessUrl( "/" )
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID").permitAll());
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return ( web ) -> web.ignoring().antMatchers( "/images/**", "/js/**" );
    }
    
}