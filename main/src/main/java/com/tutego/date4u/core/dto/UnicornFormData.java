package com.tutego.date4u.core.dto;

import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.unicorn.Unicorn;
import jakarta.validation.constraints.NotEmpty;

import java.util.Objects;

public class UnicornFormData {
    
    @NotEmpty(message="Password cannot be empty")
    private String password;
    
    
    @NotEmpty(message="Email should not be empty")
    private String email;
    
    private Long profileId;
    
    public UnicornFormData(){}
    public UnicornFormData(String password, String email) {
        this.password = password;
        this.email = email;
    }
    
    @Override
    public String toString() {
        return "UnicornFromData{" +
                "password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    
    public Unicorn generateNewUnicorn(Profile profile){
        return new Unicorn(this.getEmail(), this.getPassword(), profile);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UnicornFormData that = (UnicornFormData) o;
        return password.equals(that.password) && email.equals(that.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(password, email);
    }
}
