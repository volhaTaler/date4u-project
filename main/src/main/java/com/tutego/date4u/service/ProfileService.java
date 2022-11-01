package com.tutego.date4u.service;

import com.tutego.date4u.core.config.CurrentUser;
import com.tutego.date4u.core.dto.FilterFormData;
import com.tutego.date4u.core.dto.Gender;
import com.tutego.date4u.core.dto.ProfileFormData;
import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.profile.ProfileRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class ProfileService {
    @Autowired
    private final ProfileRepository profiles;
    
    private final Logger log = LoggerFactory.getLogger( getClass() );
    public ProfileService(ProfileRepository profiles) {
        this.profiles = profiles;
        
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
