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
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    
    
    //@Autowired
    private UserDetailsServiceConfiguration unicornService;
/*    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceConfiguration();
    }*/

    public SecurityConfig(UserDetailsServiceConfiguration unicornService) {
        this.unicornService = unicornService;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
    
 //   @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration authConfig ) throws Exception {
//        return authConfig.getAuthenticationManager();
//    }

/*    @Bean
    public AuthenticationManager authManager(HttpSecurity http)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(unicornService)
                .and()
                .build();
    }*/
/*    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService( userDetailsService() );
        return authProvider;
    }*/
    
    @Bean
    public AuthenticationManager authenticationProvider(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(unicornService)
                .and()
                .build();
    }
    
   
//    @Bean
//    public SecurityFilterChain filterChain( HttpSecurity http ) throws Exception {
//        http.authorizeRequests().antMatchers(
//                        "/registration",
//                        "/profile/*",
//
//                        "/img/**",
//                        "/search",
//                        "/home",
//                      //  "/profile",
//                        "/").authenticated()
//                .anyRequest().anonymous()
//                .and()
//                .formLogin().loginPage("/login.html")
//               // .loginProcessingUrl("/login")
//                .permitAll()
//                .usernameParameter( "email" )
//                .failureUrl( "/")
//                .permitAll()
//                .and()
//                .logout().logoutSuccessUrl( "/" )
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID")
//               // .clearAuthentification(true)
//                .permitAll();
//        http.authenticationProvider( authenticationProvider() );
//        http.headers().frameOptions().sameOrigin();
//        return http.build();
//    }
    
    @Bean
    public SecurityFilterChain filterChain( HttpSecurity http ) throws Exception {
        http.authorizeRequests((requests) -> requests.antMatchers("/registration","/")
                .permitAll().anyRequest().authenticated())
                .formLogin((form) -> form.loginPage("/login").defaultSuccessUrl( "/profile", true )
                        .permitAll())
                .logout((logout) -> logout.logoutSuccessUrl( "/" )
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID").permitAll());
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return ( web ) -> web.ignoring().antMatchers( "/images/**", "/js/**" );
    }
    
}