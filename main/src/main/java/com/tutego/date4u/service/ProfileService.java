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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    
    public Page<Profile> findPaginated(Pageable pageable, FilterFormData filter) {
        Page<Profile> foundProfiles = null;
        LocalDate startDate = LocalDate.now().minusYears(filter.getMaxAge());
        LocalDate endDate = LocalDate.now().minusYears(filter.getMinAge());
        log.info("start date: " + startDate + " and endDate: " +endDate);
        // all genders, so gender is set at null
        if(Gender.ALL.getGender() == filter.getGender()){
            if(filter.getMinHornlength() == filter.getMaxHornlength()){
                if(filter.getMinAge() == filter.getMaxAge()){
                    // select p FROM profile WHERE p.gender is ANY AND p.hornLength == filter.getMinHornlength() AND p.birthdate == startDate);
                    foundProfiles = this.profiles.findProfileByBirthdateAndHornlength(startDate, filter.getMinHornlength(), pageable);
                }else{
                    // select p FROM profile WHERE p.gender is ANY AND p.hornLength == filter.getMinHornlength() AND p.birthdate BETWEEN startDate AND endDate);
                    foundProfiles = this.profiles.findProfileByBirthdateBetweenAndHornlength(startDate, endDate, filter.getMinHornlength(), pageable);
                }
                
            }else{
                if(filter.getMinAge() == filter.getMaxAge()){
                    // select p FROM profile WHERE p.gender is ANY
                    // AND p.hornLength BETWEEN filter.getMinHornlength() AND filter.getMinHornlength()
                    // AND p.birthdate == startDate);
                    foundProfiles = this.profiles.findProfileByBirthdateAndHornlengthBetween(startDate, filter.getMinHornlength(),
                            filter.getMaxHornlength(), pageable);
                }else{
                    // select p FROM profile WHERE p.gender is ANY
                    // AND p.hornLength BETWEEN filter.getMinHornlength() AND filter.getMinHornlength()
                    // AND p.birthdate BETWEEN startDate AND endDate);
                    foundProfiles = this.profiles.findProfileByBirthdateBetweenAndHornlengthBetween(startDate, endDate, filter.getMinHornlength(),
                            filter.getMaxHornlength(), pageable);
                }
            
            }
////            for(long g : Gender.getAllGenders()) {
//                        foundProfiles = this.profiles.findProfileByBirthdateBetween(startDate, endDate, pageable);
////            }
//            log.info("list of found profiles for dates: " + startDate + " and the enddate: " + endDate);
//            log.info("number of found profiles --- " + foundProfiles.getTotalElements());
//            log.info("number of found pages: " + foundProfiles.getTotalPages());
////            log.info("number of found profiles --- " + allProfiles.size());
////            log.info("list of found profiles: " + allProfiles);
        }else{
//            foundProfiles = this.profiles.findProfileByBirthdateBetweenAndGender(startDate, endDate, filter.getGender(), pageable);
//            log.info("list of found profiles for dates: " + startDate + " and the enddate: " + endDate);
//            log.info("number of found profiles --- " + foundProfiles.getTotalElements());
//            log.info("number of found pages: " + foundProfiles.getTotalPages());
            
            if(filter.getMinHornlength() == filter.getMaxHornlength()){
                if(filter.getMinAge() == filter.getMaxAge()){
                    // select p FROM profile WHERE p.gender== filter.getGender()
                    // AND p.hornLength == filter.getMinHornlength() AND p.birthdate == startDate);
                    foundProfiles = this.profiles.findProfileByGenderAndBirthdateAndHornlength(filter.getGender(),
                            startDate, filter.getMinHornlength(), pageable);
                }else{
                    // select p FROM profile WHERE p.gender p.gender== filter.getGender()
                    // AND p.hornLength == filter.getMinHornlength() AND p.birthdate BETWEEN startDate AND endDate);
                    foundProfiles = this.profiles.findProfileByGenderAndBirthdateBetweenAndHornlength(filter.getGender(),
                            startDate, endDate, filter.getMinHornlength(), pageable);
                }
        
            }else{
                if(filter.getMinAge() == filter.getMaxAge()){
                    // select p FROM profile WHERE p.gender p.gender== filter.getGender()
                    // AND p.hornLength BETWEEN filter.getMinHornlength() AND filter.getMinHornlength()
                    // AND p.birthdate == startDate);
                    foundProfiles = this.profiles.findProfileByGenderAndBirthdateAndHornlengthBetween(filter.getGender(), startDate, filter.getMinHornlength(),
                            filter.getMaxHornlength(), pageable);
                }else{
                    // select p FROM profile WHERE p.gender p.gender== filter.getGender()
                    // AND p.hornLength BETWEEN filter.getMinHornlength() AND filter.getMinHornlength()
                    // AND p.birthdate BETWEEN startDate AND endDate);
                    foundProfiles = this.profiles.findProfileByGenderAndBirthdateBetweenAndHornlengthBetween(filter.getGender(),
                            startDate, endDate, filter.getMinHornlength(),
                            filter.getMaxHornlength(), pageable);
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
