package com.tutego.date4u.core.config;


import com.tutego.date4u.service.UnicornService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    
    
    @Autowired
    private UserDetailsServiceConfiguration unicornService;
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceConfiguration();
    }

    
    @Bean
    public PasswordEncoder passwordEncoder() {
        
        return new BCryptPasswordEncoder();
    }
    
//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration authConfig ) throws Exception {
//        return authConfig.getAuthenticationManager();
//    }
//
    @Bean
    public AuthenticationManager authManager(HttpSecurity http)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(unicornService)
                .and()
                .build();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService( userDetailsService() );
        return authProvider;
    }
    
   
    @Bean
    public SecurityFilterChain filterChain( HttpSecurity http ) throws Exception {
        http.authorizeRequests().antMatchers(
                      //  "/profile/*",
                        "/",
                        "/img/**",
                       // "/search",
                     //   "/home",
                      //  "/profile",
                        "/registration").authenticated()
                .anyRequest().anonymous()
                .and()
                .formLogin().loginPage("/login.html")
                .loginProcessingUrl("/login")
                .permitAll()
                .usernameParameter( "email" )
                .failureUrl( "/")
                .permitAll()
                .and()
                .logout().logoutSuccessUrl( "/" )
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
               // .clearAuthentification(true)
                .permitAll();
        http.authenticationProvider( authenticationProvider() );
        http.headers().frameOptions().sameOrigin();
        return http.build();
    }
    
//    @Bean
//    public SecurityFilterChain filterChain( HttpSecurity http ) throws Exception {
//
//        http.authorizeRequests()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .formLogin().loginPage("/login").permitAll();
//        return http.build();
  //  }
//        http.authorizeRequests().antMatchers( "/registration" ).authenticated()
//                .antMatchers( "/search" ).authenticated()
//                .antMatchers( "/home" ).authenticated()
//                .antMatchers( "/profile/*" ).authenticated()
//                .anyRequest().permitAll()
//                .and()
//                .formLogin().loginPage("/login")
//                .usernameParameter( "email" )
//                .defaultSuccessUrl( "/home", true )
//                .failureUrl("/")
//                .permitAll()
//                .and()
//                .logout().logoutSuccessUrl( "/" )
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID")
//                .permitAll();
//        http.authenticationProvider( authenticationProvider() );
//        http.headers().frameOptions().sameOrigin();
//        return http.build();
//    }
//
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return ( web ) -> web.ignoring().antMatchers( "/images/**", "/js/**", "/webjars/**" );
    }
    
}