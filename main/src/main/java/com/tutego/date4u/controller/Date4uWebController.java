package com.tutego.date4u.controller;

import com.tutego.date4u.core.config.CurrentUser;
import com.tutego.date4u.core.dto.FilterFormData;
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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@SessionAttributes("filter")
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
    
    
    @ModelAttribute("filter")
    public FilterFormData setFilter(){
        return new FilterFormData();
    }
    
    @GetMapping("/reset")
    public String resetParameters(Model model,
                                  @ModelAttribute("filter") FilterFormData filter){
        model.addAttribute("filter", new FilterFormData());
        return "search";
    }
    @GetMapping( "/search"  )
    public String searchPage(Model model,
                             @ModelAttribute("filter") FilterFormData filter,
                             @RequestParam(defaultValue="1") int page,
                             @RequestParam(defaultValue = "5") int size,
                             Authentication auth){

        log.info("number of page: " + page + " , Input parameters: " + filter.getGender() + " and: " + filter.getMaxAge() +  " and: " + filter.getMinAge() );
        log.info("filter id: " + filter);
        Page<Profile> pagesOfProfiles =  profileService.findPaginated(PageRequest.of(page-1, 5),  filter, getOwnId(auth));
        int totalPages = pagesOfProfiles.getTotalPages();
        if(pagesOfProfiles.getTotalPages()==0){
            //  attributes.addAttribute("msg", "No unicorn profiles were found based on the specified parameters.");
            model.addAttribute("filter", filter);
            model.addAttribute("message","No unicorn profiles were found based on the specified parameters." );
            return "search";
        }
        else if(page > totalPages){
            model.addAttribute("message","Invalid page number." );
            return "search";
        }
       
        List<ProfileFormData> listOfProfileDTO = profileService.convertProfileToProfileDTO(pagesOfProfiles.getContent());
        model.addAttribute("pageOfProfiles", listOfProfileDTO);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalProfilesNumber", pagesOfProfiles.getTotalElements());



        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        
        return "search";
    }
    
    
    private Optional<Long> getOwnId(Authentication auth){
        CurrentUser unicorn = null;
        
        if(auth != null) {
            unicorn = (CurrentUser) auth.getPrincipal();
            return Optional.of(unicorn.getProfile().getId());
        }
        
        return Optional.empty();
    }

    
   
    
    
   
}
