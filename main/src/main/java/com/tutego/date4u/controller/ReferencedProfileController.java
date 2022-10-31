package com.tutego.date4u.controller;

import com.tutego.date4u.core.config.CurrentUser;
import com.tutego.date4u.core.dto.LikesFormData;
import com.tutego.date4u.core.dto.ProfileFormData;
import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.profile.ProfileRepository;
import com.tutego.date4u.service.ProfileService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
                              Model model) {
        
        Optional<Profile> profile = profileService.getProfilesById(id);
        if (profile.isEmpty()) {
            return "redirect:/search?";
        }
        ProfileFormData temp = ProfileFormData.createPFD(profile.get());
        int likes = profileService.getMyLikes(profile.get());
        List<String> photos = temp.getPhotos();
        model.addAttribute("profilePhoto", temp.getProfilePhoto());
        model.addAttribute("myLikes" , likes);
        if (photos.isEmpty()) {
            
            model.addAttribute("photosList", temp.getProfilePhoto());
        } else {
            model.addAttribute("photosList", photos.subList(1, photos.size()));
        }
        
        model.addAttribute("profile", temp);
        return "reference_profile";
    }
    
    
}

