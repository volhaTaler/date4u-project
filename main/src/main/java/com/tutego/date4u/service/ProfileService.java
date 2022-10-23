package com.tutego.date4u.service;

import com.tutego.date4u.core.dto.ProfileFormData;
import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.profile.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileService {
    @Autowired
    private final ProfileRepository profiles;
    private List<Profile> allProfiles;
    public ProfileService(ProfileRepository profiles) {
        this.profiles = profiles;
        //allProfiles = this.profiles.findAll();
    }
    
    public Page<Profile> findPaginated(Pageable pageable, String gender) {
        
        if(!gender.equals("egal")) {
        allProfiles = this.profiles.findAll();
        }else{
            allProfiles = this.profiles.findByGender(1);
        }
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Profile> list;
        
        if (allProfiles.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allProfiles.size());
            list = allProfiles.subList(startItem, toIndex);
        }
    
        return new PageImpl<Profile>(list, PageRequest.of(currentPage, pageSize), allProfiles.size());
    }
    
    public List<ProfileFormData> convertProfileToProfileDTO(List<Profile> profiles){
        
        return profiles.stream().map(ProfileFormData::createPFD).toList();
    }
}
