package com.tutego.date4u.controller;

import com.tutego.date4u.core.config.CurrentUser;
import com.tutego.date4u.core.dto.FilterFormData;
import com.tutego.date4u.core.dto.PageDTO;
import com.tutego.date4u.core.dto.ProfileFormData;
import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.service.ProfileService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This Controller is responsible for search of profiles in DB based on entered parameters.
 * If now parameters have been entered, then the default parameters are used.
 * SessionAttribute is required to store the parameters for search in DB.
 */
@Controller
@SessionAttributes("filter")
public class SearchController {
    
    @Autowired
    private ProfileService profileService;
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    /**
     * this function is used to create the default instance of FilterFormData class to start search by parameters.
     *
     * @return an instance of FilterFormData to store the search parameters
     */
    @ModelAttribute("filter")
    public FilterFormData setFilter() {
        return new FilterFormData();
    }
    
    /**
     * @param model
     * @param status is used to complete the current session
     * @return the function provides the name of the html file, which is used to render the Java data.
     */
    @GetMapping("/reset")
    public String reset(Model model,
                        SessionStatus status
    ) {
        model.addAttribute("filter", new FilterFormData());
        status.setComplete();
        return "search";
    }
    
    /**
     * The function  calls the search method to get a page of profiles,
     * converts them into DTO objects and stores the page as model attribute.
     * All values required for paginated representation of are computed in the function.
     *
     * @param model  mvc model used to store the updated Java object and transfer it to the frontend.
     * @param filter filter contains parameters for search in DB.
     * @param page   the number of current page.
     * @param auth   is required to filter out the logged-in user from the search result.
     * @return the function provides the name of the html file, which is used to render the Java data.
     */
    @GetMapping("/search")
    public String searchPage(Model model,
                             @ModelAttribute("filter") FilterFormData filter,
                              @RequestParam(defaultValue = "next") String page,
                             RedirectAttributes redirectAttributes,
                             Authentication auth) {
        
        if (getOwnId(auth).isEmpty()) {
            return "error";
        }
        PageDTO pageDTO =
                profileService.findFirstPage( filter, getOwnId(auth).get(), page);
        
        log.info("Found results: " + pageDTO.getTotalResults());
        log.info("next " +pageDTO.nextPageExist());
        log.info("previous: " + pageDTO.prevPageExist());
        List<ProfileFormData> listOfProfileDTO =
                profileService.convertProfileToProfileDTO(pageDTO.getItems());
        model.addAttribute("pageOfProfiles", listOfProfileDTO);
        // required for paginated representation of results
       // model.addAttribute("currentPage", page);
        model.addAttribute("hasPrev", pageDTO.prevPageExist());
        model.addAttribute("hasNext", pageDTO.nextPageExist());
        model.addAttribute("totalProfilesNumber", pageDTO.getTotalResults());
        
        
        return "search";
    }
    
    
    @GetMapping("/more")
    public String searchMore(Model model,
                             @ModelAttribute("filter") FilterFormData filter,
                             @RequestParam(defaultValue = "next") String page,
                             RedirectAttributes redirectAttributes,
                             Authentication auth) {
        
        if (getOwnId(auth).isEmpty()) {
            return "error";
        }
        PageDTO pageDTO =
                profileService.findFurtherPage( filter, getOwnId(auth).get(), page);
        
        if (!page.equals("prev") && !page.equals("next")) {
            redirectAttributes.addFlashAttribute("message", "Invalid page navigation." +
                    " Please use previous and next buttons.");
            return "redirect:/search";
        }
        
        List<ProfileFormData> listOfProfileDTO =
                profileService.convertProfileToProfileDTO(pageDTO.getItems());
        model.addAttribute("pageOfProfiles", listOfProfileDTO);
     log.info("next " +pageDTO.nextPageExist());
     log.info("previous: " + pageDTO.prevPageExist());
       // model.addAttribute("currentPage", page);
        model.addAttribute("hasPrev", pageDTO.prevPageExist());
        model.addAttribute("hasNext", pageDTO.nextPageExist());
        model.addAttribute("totalProfilesNumber", pageDTO.getTotalResults());
        
        return "search";
    }
    
    /**
     * @param auth required to filter out from the results a profile of the logged-in user.
     * @return an option of with an id of the logged-in user profile.
     */

    private Optional<Long> getOwnId(Authentication auth) {
        CurrentUser unicorn = null;
    
        if (auth != null) {
            unicorn = (CurrentUser) auth.getPrincipal();
            return Optional.of(unicorn.getProfile().getId());
        }
    
        return Optional.empty();
    }
    
}
