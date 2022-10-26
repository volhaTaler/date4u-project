package com.tutego.date4u.service;

import com.tutego.date4u.core.dto.FilterFormData;
import com.tutego.date4u.core.dto.Gender;
import com.tutego.date4u.core.dto.ProfileFormData;
import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.profile.ProfileRepository;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfileService {
    @Autowired
    private final ProfileRepository profiles;
    private List<Profile> allProfiles;
    
    private final Logger log = LoggerFactory.getLogger( getClass() );
    public ProfileService(ProfileRepository profiles) {
        this.profiles = profiles;
        //allProfiles = this.profiles.findAll();
    }
    
    public Page<Profile> findPaginated(Pageable pageable, FilterFormData filter, Optional<Long> ownId) {
        Page<Profile> foundProfiles = null;
        LocalDate startDate = LocalDate.now().minusYears(filter.getMaxAge());
        LocalDate endDate = LocalDate.now().minusYears(filter.getMinAge());
        log.info("start date: " + startDate + " and endDate: " +endDate);
        // all genders, so gender is set at null
        if (Gender.ALL.getGender() == filter.getGender()) {
            if (filter.getMinHornlength() == filter.getMaxHornlength()) {
                if (filter.getMinAge() == filter.getMaxAge()) {
                    // select p FROM profile WHERE p.gender is ANY AND p.hornLength == filter.getMinHornlength() AND p.birthdate == startDate);
                    foundProfiles =
                            this.profiles.findProfileByBirthdateAndHornlengthAndIdNot(startDate, filter.getMinHornlength(), ownId.get(), pageable);
                } else {
                    // select p FROM profile WHERE p.gender is ANY AND p.hornLength == filter.getMinHornlength() AND p.birthdate BETWEEN startDate AND endDate);
                    foundProfiles =
                            this.profiles.findProfileByBirthdateBetweenAndHornlengthAndIdNot(startDate, endDate,
                                    filter.getMinHornlength(), ownId.get(), pageable);
                }
            
            } else {
                if (filter.getMinAge() == filter.getMaxAge()) {
                    // select p FROM profile WHERE p.gender is ANY
                    // AND p.hornLength BETWEEN filter.getMinHornlength() AND filter.getMinHornlength()
                    // AND p.birthdate == startDate);
                    foundProfiles =
                            this.profiles.findProfileByBirthdateAndHornlengthBetweenAndIdNot(startDate, filter.getMinHornlength(),
                                    filter.getMaxHornlength(), ownId.get(), pageable);
                } else {
                    // select p FROM profile WHERE p.gender is ANY
                    // AND p.hornLength BETWEEN filter.getMinHornlength() AND filter.getMinHornlength()
                    // AND p.birthdate BETWEEN startDate AND endDate);
                    foundProfiles =
                            this.profiles.findProfileByBirthdateBetweenAndHornlengthBetweenAndIdNot(startDate, endDate, filter.getMinHornlength(),
                                    filter.getMaxHornlength(), ownId.get(), pageable);
                }
            
            }
        }else{
        
            if (filter.getMinHornlength() == filter.getMaxHornlength()) {
                if (filter.getMinAge() == filter.getMaxAge()) {
                    // select p FROM profile WHERE p.gender== filter.getGender()
                    // AND p.hornLength == filter.getMinHornlength() AND p.birthdate == startDate);
                    foundProfiles = this.profiles.findProfileByGenderAndBirthdateAndHornlengthAndIdNot(filter.getGender(),
                            startDate, filter.getMinHornlength(), ownId.get(), pageable);
                } else {
                    // select p FROM profile WHERE p.gender p.gender== filter.getGender()
                    // AND p.hornLength == filter.getMinHornlength() AND p.birthdate BETWEEN startDate AND endDate);
                    foundProfiles =
                            this.profiles.findProfileByGenderAndBirthdateBetweenAndHornlengthAndIdNot(filter.getGender(),
                                    startDate, endDate, filter.getMinHornlength(), ownId.get(), pageable);
                }
            
            } else {
                if (filter.getMinAge() == filter.getMaxAge()) {
                    // select p FROM profile WHERE p.gender p.gender== filter.getGender()
                    // AND p.hornLength BETWEEN filter.getMinHornlength() AND filter.getMinHornlength()
                    // AND p.birthdate == startDate);
                    foundProfiles =
                            this.profiles.findProfileByGenderAndBirthdateAndHornlengthBetweenAndIdNot(filter.getGender(), startDate, filter.getMinHornlength(),
                                    filter.getMaxHornlength(), ownId.get(), pageable);
                } else {
                    // select p FROM profile WHERE p.gender p.gender== filter.getGender()
                    // AND p.hornLength BETWEEN filter.getMinHornlength() AND filter.getMinHornlength()
                    // AND p.birthdate BETWEEN startDate AND endDate);
                    foundProfiles =
                            this.profiles.findProfileByGenderAndBirthdateBetweenAndHornlengthBetweenAndIdNot(filter.getGender(),
                                    startDate, endDate, filter.getMinHornlength(),
                                    filter.getMaxHornlength(), ownId.get(), pageable);
                }
            
            }
        }
//        int pageSize = pageable.getPageSize();
//        int currentPage = pageable.getPageNumber();
//        int startItem = currentPage * pageSize;
//        List<Profile> list;
//
//        if (foundProfiles.getContent().size() < startItem) {
//            list = Collections.emptyList();
//        } else {
//            int toIndex = Math.min(startItem + pageSize, foundProfiles.getContent().size());
//            list = allProfiles.subList(startItem, toIndex);
//        }
//
//        return new PageImpl<Profile>(list, PageRequest.of(currentPage, pageSize), allProfiles.size());
        
            
            return foundProfiles;
        
    }
    
    public List<ProfileFormData> convertProfileToProfileDTO(List<Profile> profiles){
        
        return profiles.stream().map(ProfileFormData::createPFD).toList();
    }
    
}
