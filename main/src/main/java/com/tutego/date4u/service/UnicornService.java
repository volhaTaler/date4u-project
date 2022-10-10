package com.tutego.date4u.service;

import com.tutego.date4u.core.profile.Unicorn;
import com.tutego.date4u.core.profile.UnicornRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UnicornService {
    
    @Autowired
    private final UnicornRepository unicornRepository;
    
    
    public UnicornService(UnicornRepository unicornRepository) {
        this.unicornRepository = unicornRepository;
        
    }
    
    public Optional<Long> checkEmail(String userEmail){
        Optional<Unicorn> unicorn = this.unicornRepository.findUnicornByEmail(userEmail);
        return unicorn.map(Unicorn::getId);
    
    }
    public Optional<Long> checkLoginData(String userEmail, String password ){
        Optional<Unicorn> unicorn = this.unicornRepository.findUnicornByEmail(userEmail);
        if(unicorn.isPresent() && unicorn.get().getPassword().substring(6).equals(password)){
            return Optional.of(unicorn.get().getId());
        }
        return Optional.empty();
        
    }
}
