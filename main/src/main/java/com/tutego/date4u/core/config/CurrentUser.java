package com.tutego.date4u.core.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CurrentUser extends User {
    
    
    
    public CurrentUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
    
    @Override
    public String getUsername() {
        return super.getUsername();
    }
    
    @Override
    public String getPassword() {
        return super.getPassword();
    }
    
}
