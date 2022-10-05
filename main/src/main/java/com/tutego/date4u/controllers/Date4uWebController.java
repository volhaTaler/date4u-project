package com.tutego.date4u.controllers;

import com.tutego.date4u.core.format.ProfileFormData;
import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.profile.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
public class Date4uWebController {
    
    @Autowired
    private  final ProfileRepository profiles;
    private final Logger log = LoggerFactory.getLogger( getClass() );
    
    public Date4uWebController(ProfileRepository profiles) {
        this.profiles = profiles;
    }
    
    @RequestMapping( "/**" )
    public String indexPage(Model model) {
        model.addAttribute( "totalProfiles", profiles.count() );
        return "index"; }
    
    @RequestMapping( "/profile/{id}" )
    public String profilePage(@PathVariable("id") long id,  Model model) {
        Optional<Profile> profile = profiles.findById(id);
        if(profile.isEmpty()){
            return "redirect:/";
        }
        Profile temp = profile.get();
        model.addAttribute("profile", new ProfileFormData(
                temp.getId(), temp.getNickname(), temp.getBirthdate(),
                temp.getHornlength(), temp.getGender(),
                temp.getAttractedToGender(), temp.getDescription(),
                temp.getLastseen()));
    
        return "profile"; }
    
    @RequestMapping( "/search" )
    public String searchPage(Model model) {
        model.addAttribute( "profiles", profiles.findAll() );
        return "search";
    }
    
   
    
    @PostMapping( "/save" )
    public String saveProfile( @ModelAttribute ProfileFormData profile ) {
        log.info( profile.toString() );
        // change so that updates are stored to the database
        return "redirect:/profile/" + profile.getId();
    }
   
}
