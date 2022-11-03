package com.tutego.date4u.controller;


import com.tutego.date4u.core.config.CurrentUser;
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
public class ProfileController {
    
//    @Autowired
//    private final ProfileRepository profiles;
    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private UnicornService unicornService;
    
    
    private final Logger log = LoggerFactory.getLogger(getClass());
    
//    public ProfileController(final ProfileRepository profiles) {
//
//        this.profiles = profiles;
//    }
    
    /**
     * this function calls a page of the logged-in user in order to update the data.
     *
     * @param auth  required to check whether the user with this id is authorized to edit the profile page.
     * @param model stores java obejcts and provides them to frontend.
     * @return html page with profile
     */
    @RequestMapping("/profile")
    public String profilePage(Authentication auth,
                              Model model) {
        CurrentUser unicorn = null;
        if (auth != null) {
            unicorn = (CurrentUser) auth.getPrincipal();
            Optional<Profile> currentProfile = unicornService.getNicknameByEmail(unicorn.getUsername());
            if (currentProfile.isEmpty()) {
                return "/login?logout";
            }
            Profile temp = currentProfile.get();
            currentProfile.get().setLastseen(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            profileService.saveProfile(currentProfile.get());
            ProfileFormData pfd = ProfileFormData.createPFD(temp);
            List<String> photos = pfd.getPhotos();
            model.addAttribute("givenLike", true);
            model.addAttribute("profilePhoto", pfd.getProfilePhoto());
            model.addAttribute("myLikes", profileService.getMyLikes(temp));
            if (photos.isEmpty()) {
                model.addAttribute("photosList", pfd.getProfilePhoto());
            } else {
                model.addAttribute("photosList", photos.subList(1, photos.size()));
            }
            model.addAttribute("profile", pfd);
            
            return "profile";
        }
        
        return "/login?logout";
    }
    
    
    @PostMapping(value = "/save", params = "action=save")
    public String saveProfile(@Valid @ModelAttribute("profile") ProfileFormData profile,
                              BindingResult profileResult,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        Optional<Profile> toUpdateProfile = profileService.getProfilesById(profile.getId());
        if (profileResult.hasErrors()) {
            model.addAttribute("profile", profile);
            redirectAttributes.addFlashAttribute("message", "Some input was incorrect.");
            return "redirect:/profile";
        }
        if (toUpdateProfile.isPresent()) {
            Profile tmp = toUpdateProfile.get();
            profile.updateProfile(tmp);
            profileService.saveProfile(tmp);
            
        } else {
            redirectAttributes.addFlashAttribute("message", "Something went wrong. This profile does not exist.");
            return "redirect:/profile";
        }
        redirectAttributes.addFlashAttribute("message", "Your profile just has been updated.");
        return "redirect:/profile?success";
    }
    
    
}
