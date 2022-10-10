package com.tutego.date4u.core.dto;

import com.tutego.date4u.core.photo.Photo;
import com.tutego.date4u.core.profile.Profile;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ProfileFormData {
    
    private long id;
    @NotEmpty
    private String nickname;
    @NotNull
    @DateTimeFormat( pattern = "yyyy-MM-dd" )
    private LocalDate birthdate;
   
    private int hornlength;
    @NotNull(message="1 - Male, 2 - Femael, 0 - Diverse")
    private int gender;
    private Integer attractedToGender;
    private String description;
    private LocalDateTime lastseen;
    
    private List<String> photos;
    public ProfileFormData() { }
    
    
    public ProfileFormData(long id, String nickname,
                           LocalDate birthdate, int hornlength, int gender,
                           Integer attractedToGender, String description, LocalDateTime lastseen, List<String> photos ) {
        this.id = id;
        this.nickname = nickname;
        this.birthdate = birthdate;
        this.hornlength = hornlength;
        this.gender = gender;
        this.attractedToGender = attractedToGender;
        this.description = description;
        this.lastseen = lastseen;
        this.photos = photos;
    }
    
    public static ProfileFormData createPFD(Profile temp){
        return new ProfileFormData(
                temp.getId(), temp.getNickname(), temp.getBirthdate(),
                temp.getHornlength(), temp.getGender(),
                temp.getAttractedToGender(), temp.getDescription(),
                temp.getLastseen(), temp.getPhotos().stream().map( Photo::getName ).toList());
    }
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public LocalDate getBirthdate() {
        return birthdate;
    }
    
    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }
    
    public int getHornlength() {
        return hornlength;
    }
    
    public void setHornlength(int hornlength) {
        this.hornlength = hornlength;
    }
    
    public int getGender() {
        return gender;
    }
    
    public void setGender(int gender) {
        this.gender = gender;
    }
    
    public Integer getAttractedToGender() {
        return attractedToGender;
    }
    
    public void setAttractedToGender(Integer attractedToGender) {
        this.attractedToGender = attractedToGender;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getLastseen() {
        return lastseen;
    }
    
    public void setLastseen(LocalDateTime lastseen) {
        this.lastseen = lastseen;
    }
    public List<String> getPhotos() {
        return photos;
    }
    
    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }
    
    public void updateProfile(Profile profile){
        
              if(this.getNickname() !=null) {
                  profile.setNickname(this.getNickname());
              }
              if(this.getBirthdate()!= null) {
                  profile.setBirthdate(this.getBirthdate());
              }
              if(this.getHornlength() >=0) {
                  profile.setHornlength(this.getHornlength());
              }
              if(this.getGender() <2 && this.getGender() >=0) {
                  profile.setGender(this.getGender());
              }
              if(this.getAttractedToGender() <2 && this.getAttractedToGender() >=0) {
              profile.setAttractedToGender(this.getAttractedToGender());
              }
              if(this.getDescription() != null) {
                  profile.setDescription(this.getDescription());
              }
    }
    public Profile generateNewProfile(){
        return new Profile(this.getNickname(), this.getBirthdate(), this.getHornlength(),
                this.getGender(), this.getAttractedToGender(), this.getDescription(),
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }
    
    
    @Override
    public String toString() {
        return "ProfileFormData{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", birthdate=" + birthdate +
                ", hornlength=" + hornlength +
                ", gender=" + gender +
                ", attractedToGender=" + attractedToGender +
                ", description='" + description + '\'' +
                ", lastseen=" + lastseen +
                '}';
    }
    
}
