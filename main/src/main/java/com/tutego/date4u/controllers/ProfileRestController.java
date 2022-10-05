package com.tutego.date4u.controllers;

import com.tutego.date4u.core.photo.Photo;
import com.tutego.date4u.core.photo.PhotoService;
import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.profile.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping( "/api/profiles/"
)
public class ProfileRestController {
    
    @Autowired
    final ProfileRepository profiles;
    final PhotoService photos;
    
    public ProfileRestController( ProfileRepository profiles, PhotoService photos ) {
        this.profiles = profiles;
        this.photos = photos;
    }
    
    @PostMapping( "{id}/photos/" )
    public ResponseEntity<?> saveImage( @PathVariable long id,
                                        @RequestParam MultipartFile file ) throws IOException {
        Optional<Profile> maybeProfile = profiles.findById( id );
        if ( maybeProfile.isEmpty() )
            return new ResponseEntity( HttpStatus.NOT_FOUND );
        
        Profile profile = maybeProfile.get();
        String imageName = photos.upload( file.getBytes() );
        Photo photo = new Photo( null, profile, imageName, false,
                LocalDateTime.now().truncatedTo( TimeUnit.SECONDS.toChronoUnit() ) );
        profile.add( photo );
        profiles.save( profile );
        return ResponseEntity.created( URI.create( "/api/photos/" + imageName ) ).build();
    }
    
    @DeleteMapping ( "{id}/photos/" )
    public ResponseEntity<?> deleteImage( @PathVariable long id,
                                        @RequestParam int number ) {
        Optional<Profile> maybeProfile = profiles.findById( id );
        if ( maybeProfile.isEmpty() )
            return new ResponseEntity( HttpStatus.NOT_FOUND );
        
        Profile profile = maybeProfile.get();
        List<Photo> allPhotos = profile.getPhotos();
        allPhotos.sort((p1, p2) -> {
            return  (p1.getCreated().isBefore(p2.getCreated()))? 1: -1;
           });
        if(allPhotos.get(0).isProfilePhoto()){
            return new ResponseEntity("The photo is a profile one. Cannot be deleted", HttpStatus.FORBIDDEN);
        }
        // delete photoes which were added earlier
        profile.deletePhoto(allPhotos.get(0));
        profiles.save( profile );
        return ResponseEntity.ok().build();
    }
}

