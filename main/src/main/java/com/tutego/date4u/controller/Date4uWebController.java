package com.tutego.date4u.controller;

import com.tutego.date4u.core.config.CurrentUser;
import com.tutego.date4u.core.dto.ProfileFormData;
import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.profile.ProfileRepository;
import com.tutego.date4u.core.profile.UnicornRepository;
import com.tutego.date4u.service.ProfileService;
import com.tutego.date4u.service.UnicornService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class Date4uWebController {
    
    @Autowired
    private final ProfileRepository profiles;
    
    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private UnicornService unicornService;
    
    
    private final Logger log = LoggerFactory.getLogger( getClass() );
    
    public Date4uWebController(final ProfileRepository profiles, UnicornRepository unicorns) {
        this.profiles = profiles;
    }
    
    @RequestMapping( "/" )
    public String goToIndex() {
        
        return "index";
    }
    
    @GetMapping( value = "/home")
    public String home(Model model) {
//    CurrentUser unicorn = null;
//        if(auth != null) {
//            unicorn = (CurrentUser) auth.getPrincipal();
//            log.info(unicorn.getUsername() + ":  " + unicorn.getPassword());
//      //      return "redirect:/";
//        }
////        Authentication authentication = context.getAuthentication();
//        String useremail = unicorn.getUsername();
//        Optional<Profile> currentProfile= unicornService.getNicknameByEmail(useremail);
//
//        if(currentProfile.isPresent()){
//            Profile temp = currentProfile.get();
//            model.addAttribute("profile", ProfileFormData.createPFD(temp));
//            return "profile";
//
//        }
        
       return "redirect:/home";
    }
    
    @RequestMapping( "/profile/{id}" )
    public String profilePage(@PathVariable("id") long id,  Model model) {
        Optional<Profile> profile = profiles.findById(id);
        if(profile.isEmpty()){
            return "redirect:/";
        }
        Profile temp = profile.get();
        model.addAttribute("profile", ProfileFormData.createPFD(temp));
    
        return "profile/" + id; }
    
    @RequestMapping( "/search" )
    public String searchPage(Model model, @RequestParam(defaultValue="1") int page,
                             @RequestParam(defaultValue = "5") int size){
        Page<Profile> pagesOfProfiles =  profileService.findPaginated(PageRequest.of(page-1, 5));
        // profiles.findAll().stream().map(ProfileFormData::createPFD).toList()
        //model.addAttribute( "profiles", pagesOfProfiels);
        model.addAttribute("pageOfProfiles", pagesOfProfiles.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalProfilesNumber", pagesOfProfiles.getNumberOfElements());

        int totalPages = pagesOfProfiles.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
    
        return "search";
    }
    
    @PostMapping( "/save" )
    public String saveProfile( @ModelAttribute("profile") ProfileFormData profile ) {
        log.info( profile.toString() );
        Optional<Profile> toUpdateProfile = profiles.findById(profile.getId());
        if(toUpdateProfile.isPresent()){
            Profile tmp = toUpdateProfile.get();
            profile.updateProfile(tmp);
            profiles.save(tmp);
            
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This profile does not exist.");
           
        }
       
        return "redirect:/profile/" + profile.getId();
    }
    
    
   
}
