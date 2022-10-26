package com.tutego.date4u.controller;


import com.tutego.date4u.core.config.CurrentUser;
import com.tutego.date4u.core.dto.ProfileFormData;
import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.profile.ProfileRepository;
import com.tutego.date4u.core.profile.UnicornRepository;
import com.tutego.date4u.service.UnicornService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Controller
public class ProfileController {
    
    @Autowired
    private final ProfileRepository profiles;
    
    
    @Autowired
    private UnicornService unicornService;
    
    
    private final Logger log = LoggerFactory.getLogger( getClass() );
    
    public ProfileController(final ProfileRepository profiles) {
        
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
            currentProfile.get().setLastseen(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            profiles.save(currentProfile.get());
            ProfileFormData pfd = ProfileFormData.createPFD(temp);
            List<String> photos = pfd.getPhotos();
            model.addAttribute("profilePhoto", pfd.getProfilePhoto());
            if(photos.isEmpty()){
                model.addAttribute("photosList", pfd.getProfilePhoto());
            }else{
                model.addAttribute("photosList", photos.subList(1, photos.size()));
            }
            model.addAttribute("profile", pfd);
           
            return "profile";
        }
        
        return "/login?logout";
    }
    @RequestMapping( "/profile/{id}" )
    public String profilePage(@PathVariable("id") long id,
                              Model model) {

        Optional<Profile> profile = profiles.findById(id);
        if(profile.isEmpty()){
            return "redirect:/search?";
        }
        ProfileFormData temp = ProfileFormData.createPFD(profile.get());
        
        List<String> photos = temp.getPhotos();
        model.addAttribute("profilePhoto", temp.getProfilePhoto());
        if( photos.isEmpty()){
            
            model.addAttribute("photosList", temp.getProfilePhoto());
        }else{
            model.addAttribute("photosList", photos.subList(1, photos.size()));
        }
    
        model.addAttribute("profile", temp);
        return "reference_profile";
    }
    
    @PostMapping( value = "/save" , params = "action=save")
    public String saveProfile( @Valid @ModelAttribute("profile") ProfileFormData profile,
                               BindingResult profileResult,
                               Model model) {
        log.info( "profile to string: " +profile.toString() );
        Optional<Profile> toUpdateProfile = profiles.findById(profile.getId());
        if(profileResult.hasErrors()){
            model.addAttribute("profile", profile);
            return "profile";
        }
        if(toUpdateProfile.isPresent()){
            Profile tmp = toUpdateProfile.get();
            profile.updateProfile(tmp);
            profiles.save(tmp);
            
        }else{
            model.addAttribute("message", "Something went wrong. This profile does not exist.");
            
        }
        
        return "redirect:/profile?success";
    }
   
    
    
}
