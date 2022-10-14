package com.tutego.date4u.core.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CurrentUser extends User {
    
  
    public CurrentUser(Unicorn unicorn) {
        super(unicorn.getEmail(),unicorn.getPassword(),unicorn.getProfile());
    }
    
    @Override
    public String getPassword() {
        
        return super.getPassword().substring(6);
    }
    
    @Override
    public String getUsername() {
        return super.getEmail();
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_USER");
    }
    
}
