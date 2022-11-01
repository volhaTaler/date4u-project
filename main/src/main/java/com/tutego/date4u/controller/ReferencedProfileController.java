package com.tutego.date4u.controller;

import com.tutego.date4u.core.config.CurrentUser;
import com.tutego.date4u.core.dto.ProfileFormData;
import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.List;
import java.util.Optional;

@Controller
public class ReferencedProfileController {
    
    @Autowired
    private ProfileService profileService;
    
    
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    
    /** this the endpoint to visit profile pages of other unicorn-users.
     *
     * @param id the id of the profile the user want to go to
     * @param model mvc model that contains java object to render on the html page
     * @return the corresponding rendered html page
     */
    
    @RequestMapping("/profile/{id}")
    public String profilePage(@PathVariable("id") long id,
                              Authentication auth,
                              Model model) {
        
        Optional<Profile> profile = profileService.getProfilesById(id);
        if (profile.isEmpty()) {
            return "redirect:/search?";
        }
        ProfileFormData temp = ProfileFormData.createPFD(profile.get());
        int likes = profileService.getMyLikes(profile.get());
        List<String> photos = temp.getPhotos();
        boolean givenLike = profileService.isLikedByThisProfile( getOwnProfile(auth).get(), profile.get());
        log.info("given Like: " + givenLike);
        model.addAttribute("profilePhoto", temp.getProfilePhoto());
        model.addAttribute("givenLike", givenLike);
        model.addAttribute("myLikes" , likes);
        if (photos.isEmpty()) {
            
            model.addAttribute("photosList", temp.getProfilePhoto());
        } else {
            model.addAttribute("photosList", photos.subList(1, photos.size()));
        }
        
        model.addAttribute("profile", temp);
        return "reference_profile";
    }
    
    @PostMapping("/profile/{id}/like")
    public String profilePage(@PathVariable("id") long id,
                              @RequestParam("givenLike") boolean givenLike,
                              Authentication auth,
                              Model model) {
    log.info("in post method");
        Optional<Profile> profile = profileService.getProfilesById(id);
        if (profile.isEmpty()) {
            return "redirect:/search?";
        }
        
        if (getOwnProfile(auth).isEmpty()) {
            return "/error";
        }
        log.info("received like: "  + givenLike);
        profileService.updateLike(givenLike, getOwnProfile(auth).get(), profile.get());
        model.addAttribute("givenLike", givenLike);
        
        return "redirect:/profile/{id}";
    }
    
    
    private Optional<Profile> getOwnProfile(Authentication auth) {
        CurrentUser unicorn = null;
        
        if (auth != null) {
            unicorn = (CurrentUser) auth.getPrincipal();
            return Optional.of(unicorn.getProfile());
        }
        
        return Optional.empty();
    }
    
}

