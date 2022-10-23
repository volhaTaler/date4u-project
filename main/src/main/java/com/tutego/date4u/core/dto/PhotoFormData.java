package com.tutego.date4u.core.dto;

import com.tutego.date4u.core.photo.Photo;
import com.tutego.date4u.core.profile.Profile;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public class PhotoFormData {
    
    public static final String DEFAULT_IMAGE_NAME = "default";
    
    private Long id;
    
    private Profile profile;
    
    @NotEmpty
    private String name;
    private boolean isProfilePhoto;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;
    
    public PhotoFormData(){}
    
    public PhotoFormData(long id, Profile profile, String name, boolean isProfilePhoto, LocalDateTime created) {
        this.id = id;
        this.profile = profile;
        this.name = name;
        this.isProfilePhoto = isProfilePhoto;
        this.created = created;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Profile getProfile() {
        return profile;
    }
    
    public void setProfile(Profile profile) {
        this.profile = profile;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public boolean isProfilePhoto() {
        return isProfilePhoto;
    }
    
    public void setProfilePhoto(boolean profilePhoto) {
        isProfilePhoto = profilePhoto;
    }
    
    public LocalDateTime getCreated() {
        return created;
    }
    
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
    
    
    public Photo generateNewPhoto(){
        return new Photo(this.id, this.profile, this.name, this.isProfilePhoto, this.created);
    }
    
    public void setAsProfilePhoto(Photo photo){
        
        if(this.isProfilePhoto()) {
            photo.setProfilePhoto(true);
        }else{
            photo.setProfilePhoto(false);
        }
    }
    
    public static List<PhotoFormData> createListOfPhotosPFD(List<Photo> photos){
        List<PhotoFormData> resultList =  new ArrayList<>();
        for (Photo p: photos) {
            resultList.add(new PhotoFormData(p.getId(), p.getProfile(), p.getName(), p.isProfilePhoto(), p.getCreated()));
        }
        return resultList;
    }
   
}
