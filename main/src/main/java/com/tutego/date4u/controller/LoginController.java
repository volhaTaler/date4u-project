package com.tutego.date4u.controller;


import com.tutego.date4u.core.dto.ProfileFormData;
import com.tutego.date4u.core.dto.UnicornFormData;
import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.profile.ProfileRepository;
import com.tutego.date4u.core.profile.UnicornRepository;
import com.tutego.date4u.service.ProfileService;
import com.tutego.date4u.service.UnicornService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
;import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@Controller
public class LoginController {
    
    public static final int AGE_LIMIT = 18;
   
    @Autowired
    private UnicornService unicornService;
    
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    public LoginController() {
    
    }
    
//    @RequestMapping( "/" )
//    public String goToIndex() {
//
//        return "index";
//    }
    
    @GetMapping({"/login", "/"})
    public String login(){
        
        return "login";
    }
    
    @RequestMapping("/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }
    
    /**
     *
     * @param model stores properties of Java DTO objects to provide them to the frontend.
     * @return html page for registration
     */
    @GetMapping("/registration")
    public String register(Model model) {
        UnicornFormData user = new UnicornFormData();
        ProfileFormData userProfile = new ProfileFormData();
        model.addAttribute("user", user);
        model.addAttribute("userProfile", userProfile);
        model.addAttribute("ageLimit", LocalDate.now().minus(AGE_LIMIT, ChronoUnit.YEARS));
        return "registration";
    }
    
    @PostMapping("/registration/save")
    public String saveRegistration(@Valid @ModelAttribute("userProfile") ProfileFormData profile,
                                   BindingResult profileResult,
                                   @Valid @ModelAttribute("user") UnicornFormData user,
                                   BindingResult userResult, Model model) {
  
     
        if (unicornService.getByNickname(user.getEmail()).isPresent()) {
            userResult.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }
        if (userResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("userProfile", profile);
            userResult.reject(null, "email or password is incorrect.");
            return "/registration";
        }
        if(profileResult.hasErrors()){
            model.addAttribute("user", user);
            model.addAttribute("userProfile", profile);
            userResult.reject(null, "something in your profile is incorrect.");
            return "/registration";
        }
        // create accounts
        UnicornFormData userUnicorn = new UnicornFormData(user.getPassword(), user.getEmail());
        Profile userProfile = profile.generateNewProfile();
        return "redirect:/registration?success";
    }
}
