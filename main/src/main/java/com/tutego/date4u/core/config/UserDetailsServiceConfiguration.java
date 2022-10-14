package com.tutego.date4u.core.config;

import com.tutego.date4u.core.profile.Unicorn;
import com.tutego.date4u.core.profile.UnicornRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@Configuration
public class UserDetailsServiceConfiguration implements UserDetailsService {
    @Autowired
    private UnicornRepository unicornRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username ) throws UsernameNotFoundException {
        Optional<Unicorn> unicorn = unicornRepository.findUnicornByEmail( username );
        if( unicorn == null ) {
            throw new UsernameNotFoundException( "User not found " + username );
        }
        return new CurrentUser(
                unicorn.get().getEmail(),
                unicorn.get().getPassword(),
                Collections.emptyList() );
    }
}
