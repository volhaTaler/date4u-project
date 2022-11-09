package com.tutego.date4u.service;

import com.tutego.date4u.core.config.CurrentUser;
import com.tutego.date4u.core.dto.FilterFormData;
import com.tutego.date4u.core.dto.Gender;
import com.tutego.date4u.core.dto.PageDTO;
import com.tutego.date4u.core.dto.ProfileFormData;
import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.profile.ProfileRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;



@Service
public class ProfileService {
    @Autowired
    private final ProfileRepository profiles;
    
     private final PageDTO currDTOPage;
    
    
    
    private final Logger log = LoggerFactory.getLogger( getClass() );
    public ProfileService(ProfileRepository profiles) {
        this.profiles = profiles;
        this.currDTOPage = new PageDTO();
        
    }
    
    /**
     *
     * @param pageable provides number of page and number of items that should be placed on this page
     * @param filter provides search parameters. The range parameters should be increased by one,
     *                    because the upper range limit of search is not included by search.
     * @param ownId provides id of the profile that must be filtered out - logged-in user profile.
     * @return a page of found results or empty page if nothing was found
     */
    public Page<Profile> findPaginated(Pageable pageable, FilterFormData filter, long ownId) {
        
        LocalDate maxAgeDate = LocalDate.now().minusYears(filter.getMaxAge() +1);
        LocalDate minAgeDate = LocalDate.now().minusYears(filter.getMinAge() );
    
        if (Gender.ALL.getGender() == filter.getGender()) {
            log.info("in function with all genders");
            return this.profiles.findAllProfilesBySearchParams( ownId, null, filter.getMinHornlength(),
                    filter.getMaxHornlength(), minAgeDate, maxAgeDate, null, pageable);
        }else{
            log.info("in function with one genders");
            return this.profiles.findAllProfilesBySearchParams( ownId, null, filter.getMinHornlength(),
                    filter.getMaxHornlength(), minAgeDate, maxAgeDate, filter.getGender(), pageable);
        }
        
    }
    
    public PageDTO findFirstPage(FilterFormData filter, long ownId, String page) {
        currDTOPage.resetPrevIDList();
        LocalDate maxAgeDate = LocalDate.now().minusYears(filter.getMaxAge() +1L);
        LocalDate minAgeDate = LocalDate.now().minusYears(filter.getMinAge() );
        if (Gender.ALL.getGender() == filter.getGender()) {
                log.info("in function with all genders");
                this.currDTOPage.setTotalResults(profiles.countAllProfilesBySearchParams(ownId, null, filter.getMinHornlength(),
                        filter.getMaxHornlength(), minAgeDate, maxAgeDate, null));
                this.currDTOPage.setItems(profiles.findFirstFiveProfilesBySearchParams(ownId, null, filter.getMinHornlength(),
                        filter.getMaxHornlength(), minAgeDate, maxAgeDate, null));
        }else{
                log.info("in function with one genders");
              
                this.currDTOPage.setTotalResults(profiles.countAllProfilesBySearchParams(ownId, null, filter.getMinHornlength(),
                        filter.getMaxHornlength(), minAgeDate, maxAgeDate, filter.getGender()));
                    this.currDTOPage.setItems(profiles.findFirstFiveProfilesBySearchParams(ownId, null, filter.getMinHornlength(),
                            filter.getMaxHornlength(), minAgeDate, maxAgeDate, filter.getGender()));
                
        }
        this.currDTOPage.setCurrentSearchParams(filter);
        this.currDTOPage.updateIDList(page);
        this.currDTOPage.updateTrackId();
        return this.currDTOPage;
    }
    
    public PageDTO findFurtherPage(FilterFormData filter, long ownId, String page) {
        Long nextId = this.currDTOPage.getRequiredID(page);
        this.currDTOPage.updateIDList(page);
        LocalDate maxAgeDate = LocalDate.now().minusYears(filter.getMaxAge() +1L);
        LocalDate minAgeDate = LocalDate.now().minusYears(filter.getMinAge() );
        if (Gender.ALL.getGender() == filter.getGender()) {
                log.info("in function with all genders from further");
                this.currDTOPage.setItems(profiles.findProfilesBySearchParamsAndLimit(ownId, nextId,null, filter.getMinHornlength(),
                        filter.getMaxHornlength(), minAgeDate, maxAgeDate, null, this.currDTOPage.getResultsPerPage()));
                
        }else{
                log.info("in function with one genders from further");
                this.currDTOPage.setItems(profiles.findProfilesBySearchParamsAndLimit(ownId, nextId,null, filter.getMinHornlength(),
                        filter.getMaxHornlength(), minAgeDate, maxAgeDate, filter.getGender(), this.currDTOPage.getResultsPerPage()));
        }
      //  this.currDTOPage.updateDisplayedNextResults(page);
        this.currDTOPage.updateTrackId();
        return this.currDTOPage;
    }
    
    /**
     *
     * @param profiles a list of instances of class Profile
     * @return a list of DTO objects to provide them to the frontend
     */
    public List<ProfileFormData> convertProfileToProfileDTO(List<Profile> profiles){
        
        return profiles.stream().map(ProfileFormData::createPFD).toList();
    }
    
    /**
     *
     * @param id of the authorized user to filter this pofile from the search results.
     * @return optional of type Profile. If current user is not found that this is a problem. it must be always found.
     */
    public Optional<Profile> getProfilesById(long id) {
        return profiles.findById(id);
    }
    public int getMyLikes(Profile profile){
        
        return profile.getProfilesThatLikeMe().size();
        
    }
    
    public void saveProfile(Profile updatedProfile){
        
        profiles.save(updatedProfile);
    }
    
    @Transactional
    public boolean isLikedByThisProfile(Profile currentProfile, Profile referencedProfile){
        Profile refProfile = profiles.findByIdAndFetchLiker(referencedProfile.getId());
         return refProfile.getProfilesThatLikeMe().contains(currentProfile);
    }
    
    /** because of bidirectional relationship and cascade type persist, we may store likes only from one direction
     *
     * @param like parameter provides information whether the logged-in user has clicked on teh like button
     * @param ownProfile the profile of the logged-in user
     * @param referencedProfile the profile of the user that the logged-in user is currently visiting
     */
    @Transactional
    public void updateLike(boolean like, Profile ownProfile, Profile referencedProfile){
        Profile refProfile = profiles.findByIdAndFetchLiker(referencedProfile.getId());
        if(like && !refProfile.getProfilesThatLikeMe().contains(ownProfile)) {
            refProfile.getProfilesThatLikeMe().add(ownProfile);
            profiles.save(refProfile);
        }else if(!like && refProfile.getProfilesThatLikeMe().contains(ownProfile)){
            refProfile.getProfilesThatLikeMe().remove(ownProfile);
            profiles.save(refProfile);
        }
    }
    
    
    
    
}
