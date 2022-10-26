package com.tutego.date4u.core.dto;

import com.tutego.date4u.core.photo.Photo;
import com.tutego.date4u.core.profile.Profile;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ProfileFormData {
    
    public static final String DEFAULT_IMAGE_NAME = PhotoFormData.DEFAULT_IMAGE_NAME;
    private long id;
    
    @NotEmpty(message = "Nickname field cannot be empty")
    @Size(min=5, max=50, message="Your nickname must be between 5 and 50 symbols long")
    private String nickname;
    @NotNull(message = "Date of birth must be entered")
    @Past
    @DateTimeFormat( pattern = "yyyy-MM-dd" )
    private LocalDate birthdate;
    private int age;
    @PositiveOrZero(message = "Please enter correct length of your horn" )
    private int hornlength;
    private int gender;
    private Integer attractedToGender;
    @Size(min=0, max=250, message="Please write something about yourself. Keep it short and easy.")
    private String description;
    private LocalDateTime lastseen;
    private List<String> photos;
    private String profilePhoto;
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
        this.age = Period.between(this.birthdate, LocalDate.now()).getYears();
        this.profilePhoto = getProfilePhoto();
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
    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
    
    public String getProfilePhoto(){
        if(photos.isEmpty()){
            return DEFAULT_IMAGE_NAME;
        }
        return photos.get(0);
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
              if(this.getGender() <=2 && this.getGender() >=0) {
                  profile.setGender(this.getGender());
              }
              if(this.getAttractedToGender() <=2 && this.getAttractedToGender() >=0) {
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
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    
    
    private String getGenderString(int gender){
        return Gender.getGenderString(gender);
    }
    
    public String getGenderAsStr(){
        return getGenderString(this.gender);
    }
    public String getAttractGenderAsStr(){
        return getGenderString(this.attractedToGender);
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
