package com.tutego.date4u.controllers;

import com.tutego.date4u.core.photo.PhotoService;
import com.tutego.date4u.core.profile.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PhotoRestController {
    
     final PhotoService photos;
    @Autowired
    private final ProfileRepository profileRepository;
    
    public PhotoRestController(PhotoService photos, ProfileRepository profileRepository) {
        this.photos = photos;
        this.profileRepository = profileRepository;
        
    }
    
    @GetMapping( path     = "/api/photos/{imagename}",
            produces = MediaType.IMAGE_JPEG_VALUE )
    public ResponseEntity<?> photo(@PathVariable("imagename") String imagename) {
        return ResponseEntity.of(photos.download( imagename ));
    }

    
    }
