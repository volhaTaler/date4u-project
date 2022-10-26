package com.tutego.date4u.controller;


import com.tutego.date4u.core.config.CurrentUser;
import com.tutego.date4u.core.dto.ProfileFormData;
import com.tutego.date4u.core.dto.PhotoFormData;
import com.tutego.date4u.core.dto.UnicornFormData;
import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.profile.ProfileRepository;
import com.tutego.date4u.core.profile.UnicornRepository;
import com.tutego.date4u.service.ProfileService;
import com.tutego.date4u.service.UnicornService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
public class ProfileController {
    
    private final String DEFAULT_IMAGE_NAME = PhotoFormData.DEFAULT_IMAGE_NAME;
    @Autowired
    private final ProfileRepository profiles;
    
    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private UnicornService unicornService;
    
    
    private final Logger log = LoggerFactory.getLogger( getClass() );
    
    public ProfileController(final ProfileRepository profiles, UnicornRepository unicorns) {
        this.profiles = profiles;
    }
    
    @RequestMapping( "/profile" )
    public String profilePage(Authentication auth,
                              Model model) {
        CurrentUser unicorn = null;
        if(auth != null) {
            unicorn = (CurrentUser) auth.getPrincipal();
            log.info(unicorn.getUsername() + ":  " + unicorn.getPassword());
            Optional<Profile> currentProfile= unicornService.getNicknameByEmail(unicorn.getUsername());
            Profile temp = currentProfile.get();
            //List<Photo> photos = temp.getPhotos();
            ProfileFormData pfd = ProfileFormData.createPFD(temp);
            List<String> photos = pfd.getPhotos();
            if(photos == null || photos.isEmpty()){
                model.addAttribute("profilePhoto", DEFAULT_IMAGE_NAME);
            }else{
                model.addAttribute("profilePhoto", photos.get(0));
            }
            
            model.addAttribute("profile", pfd);
            model.addAttribute("photosList", photos.subList(1, photos.size()));
            return "profile";
        }
        
        return "/login?logout";
    }
    @RequestMapping( "/profile/{id}" )
    public String profilePage(@PathVariable("id") long id,
                              Authentication auth,
                              Model model) {
        CurrentUser unicorn = null;
        
        if(auth != null) {
            unicorn = (CurrentUser) auth.getPrincipal();
        }else{
            model.addAttribute("message", "You are not authorized. Pleas log in.");
            return "redirect:/search?";
        }
        long ownId = unicorn.getProfile().getId();
        Optional<Profile> profile = profiles.findById(id);
        if(profile.isEmpty()){
            return "redirect:/search?";
        }
        Profile temp = profile.get();
        if(ownId==id){
            model.addAttribute("theOwner", true);
        }else{
            model.addAttribute("theOwner", false);
        }
        model.addAttribute("profile", ProfileFormData.createPFD(temp));
        
        return "/profile";
    }
    
    @PostMapping( value = "/save" , params = "action=save")
    public String saveProfile( @Valid @ModelAttribute("profile") ProfileFormData profile,
                               BindingResult profileResult,
                               Model model) {
        log.info( "profile to string: " +profile.toString() );
        Optional<Profile> toUpdateProfile = profiles.findById(profile.getId());
        if(profileResult.hasErrors()){
            model.addAttribute("profile", profile);
            //profileResult.reject(null, "Something is wrong!");
            return "profile";
        }
        if(toUpdateProfile.isPresent()){
            Profile tmp = toUpdateProfile.get();
            profile.updateProfile(tmp);
            profiles.save(tmp);
            
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This profile does not exist.");
            
        }
        
        return "redirect:/profile?success";
    }
    
    
}
