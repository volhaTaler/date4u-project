package com.tutego.date4u.controller;

import com.tutego.date4u.core.config.CurrentUser;
import com.tutego.date4u.core.dto.ProfileFormData;
import com.tutego.date4u.core.dto.UnicornFormData;
import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.profile.ProfileRepository;
import com.tutego.date4u.core.profile.Unicorn;
import com.tutego.date4u.core.profile.UnicornRepository;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;



@Controller
public class LoginController {
    
    @Autowired
    private final UnicornRepository unicorns;
    
    @Autowired
    private final ProfileRepository profiles;
    
    @Autowired
    private ProfileService profileService;
    @Autowired
    private UnicornService unicornService;
    
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    public LoginController(UnicornRepository unicorns, ProfileRepository profiles) {
        this.unicorns = unicorns;
        this.profiles = profiles;
    }
    @GetMapping("/login")
    public String login(Authentication auth, Model model){
        CurrentUser unicorn = null;
        if(auth != null) {
            unicorn = (CurrentUser) auth.getPrincipal();
            log.info(unicorn.getUsername() + ":  " + unicorn.getPassword());
            //      return "redirect:/";
        }
//        Authentication authentication = context.getAuthentication();
        //String useremail = unicorn.getUsername();
        Optional<Profile> currentProfile= unicornService.getNicknameByEmail("fillmore.fat@wyman.co");
    
        if(currentProfile.isPresent()){
            model.addAttribute("profile", currentProfile.get());
            return "/profile";
        }
        return "/";
    }
//    @GetMapping("/login")
//    public String login(@ModelAttribute("password") String password,
//                        @ModelAttribute("useremail") String email){
//        Optional<Long> code = unicornService.checkLoginData(email, password);
//        if(code.isPresent()){
//            return "redirect:/profile/" + code.get();
//        }
//        return "home";
//    }
    
    @GetMapping("/registration")
    public String register(Model model) {
        UnicornFormData user = new UnicornFormData();
        ProfileFormData userProfile = new ProfileFormData();
        model.addAttribute("user", user);
        model.addAttribute("userProfile", userProfile);
        return "/registration";
    }
    
    @PostMapping("/registration/save")
    public String saveRegistration(@Valid @ModelAttribute("userProfile") ProfileFormData profile,
                                   @Valid @ModelAttribute("user") UnicornFormData user,
                                   BindingResult result, Model model) {
        
        if (unicornService.checkEmail(user.getEmail()).isPresent()) {
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
            
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("userProfile", profile);
            return "/registration";
        }
        // create accounts
        UnicornFormData userUnicorn = new UnicornFormData(user.getPassword(), user.getEmail());
        Profile userProfile = profile.generateNewProfile();
        Profile justCreated = profiles.save(userProfile);
        Unicorn unicornToCreate = userUnicorn.generateNewUnicorn(justCreated);
        unicorns.save(unicornToCreate);
        //return "redirect:/profile/" + justCreated.getId();
        return "redirect:/registration?success";
    }
}
