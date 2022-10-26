package com.tutego.date4u.controller;

import com.tutego.date4u.core.dto.PhotoFormData;
import com.tutego.date4u.core.photo.Photo;
import com.tutego.date4u.core.photo.PhotoRepository;
import com.tutego.date4u.core.photo.PhotoService;
import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.profile.ProfileRepository;
import com.tutego.date4u.core.profile.UnicornRepository;
import com.tutego.date4u.service.UnicornService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Controller
public class PhotoController {
    
    
    @Autowired
    private final ProfileRepository profileRepository;
    
    @Autowired
    private final UnicornRepository unicornRepository;
    
    @Autowired
    private final PhotoRepository photoRepository;
    UnicornService unicornService;
    private final PhotoService photoService;
    
    private final Logger log = LoggerFactory.getLogger( getClass() );
    
    
    public PhotoController(PhotoService photos, ProfileRepository profileRepository, UnicornRepository unicornRepository, PhotoRepository photorepository) {
        this.photoService = photos;
        this.profileRepository = profileRepository;
        this.unicornRepository = unicornRepository;
        this.photoRepository = photorepository;
    }
    
    @GetMapping("/gallery")
    public String displayUploadForm(Model model, RedirectAttributes attributes){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        unicornService = new UnicornService(unicornRepository);
        Optional<Profile> profiletoUpdate = unicornService.getByNickname(currentPrincipalName);

        if(profiletoUpdate.isPresent()) {
            List<PhotoFormData> photosToEdit = PhotoFormData.createListOfPhotosPFD(profiletoUpdate.get().getPhotos());
            model.addAttribute("photosList", photosToEdit);
            return "gallery";
        }
        attributes.addFlashAttribute("message", "Your profile is not found.");
        return "redirect:/profile";
    }
    
    
    @GetMapping("/deletePhoto/{id}")
    public String deleteById(@PathVariable(value = "id") long id,
                             RedirectAttributes attributes) {
        Photo photo = photoRepository.getReferenceById(id);
        Profile profile = profileRepository.getReferenceById(photo.getProfile().getId());
        // delete from the list
       log.info("Number before delete: " +profile.getPhotos().size());
        profile.deletePhoto(photo);
        log.info("Number after delete: " +profile.getPhotos().size());
        profileRepository.save(profile);
        
        photoRepository.deleteById(id);
        attributes.addFlashAttribute("message", "One photo was deleted.");
        return "redirect:/profile";
        
    }
    
//    @PostMapping("/savePhoto")
//    public String saveEmployee(@ModelAttribute("photosList") List<Photo> photos, RedirectAttributes attributes) {
//        if(photos.isEmpty()){
//            attributes.addFlashAttribute("message", "The list of photos is empty: Nothing to save.");
//            return "profile";
//        }
//        Profile profile = profileRepository.getReferenceById(photos.get(0).getProfile().getId());
//        List<Photo> oldPhotos = profile.getPhotos();
//        for (int i=0; i< oldPhotos.size(); i++)
//        {
//            if(!oldPhotos.get(i).equals(photos.get(i))){
//                oldPhotos.remove(i);
//                profile.add(photos.get(i));
//                profileRepository.save(profile);
//                photoRepository.save(photos.get(i));
//            }
//
//        }
//        attributes.addFlashAttribute("message", "Photos are updated");
//        return "redirect:/profile";
//    }
     @ModelAttribute("photo")
     public PhotoFormData setModelAttribute() {
        return new PhotoFormData();
    }
    
    
    @PostMapping("/savePhoto")
    public String savePhoto(@ModelAttribute("photo") PhotoFormData photo,
                               RedirectAttributes attributes) {
        
        Photo toEditPhoto = photoRepository.getReferenceById(photo.getId());
        Photo editedPhoto = photo.generateNewPhoto();
        log.info("photo to edit: " + toEditPhoto.getCreated() + " photo to edit: "+toEditPhoto.getProfile());
        if(!toEditPhoto.isProfilePhoto() && photo.isProfilePhoto()){
            Profile profile = profileRepository.getReferenceById(toEditPhoto.getProfile().getId());
            List<Photo> photos = profile.getPhotos();
            if(editedPhoto.isProfilePhoto()){
                photos.get(0).setProfilePhoto(false);
            }
            photos.remove(toEditPhoto);
            toEditPhoto.setProfilePhoto(editedPhoto.isProfilePhoto());
            //toEditPhoto.setName(editedPhoto.getName());
            profile.add(toEditPhoto);
            profileRepository.save(profile);
            photoRepository.save(toEditPhoto);
            attributes.addFlashAttribute("message", "Profile photo is updated");
        }else{
            attributes.addFlashAttribute("message", "No changes occurred");
            return "redirect:gallery";
        }
        
        return "redirect:/profile";
    }
    
    @PostMapping(value="/upload")
    public String uploadImage(Model model,
                              @RequestParam("image") MultipartFile file,
                              RedirectAttributes attributes) throws IOException {
        if(file.isEmpty()){
            attributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:profile";
        }
        // add to the DB
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        unicornService = new UnicornService(unicornRepository);
        Optional<Profile> profiletoUpdate = unicornService.getByNickname(currentPrincipalName);
        StringBuilder fileNames = new StringBuilder();
        if(profiletoUpdate.isPresent()) {
            Profile profile = profiletoUpdate.get();
            String name = photoService.upload(file.getBytes());
            
            fileNames.append(file.getOriginalFilename());
            Photo newPhoto = new Photo(null, profile, name,
                    false, LocalDateTime.now().truncatedTo(TimeUnit.SECONDS.toChronoUnit()));
            
            profile.add(newPhoto);
    
            profileRepository.save(profile);
        }
        
        model.addAttribute("msg", "Uploaded images: " + fileNames.toString());
        attributes.addFlashAttribute("message", "You successfully uploaded file");
        return "redirect:profile?success";
    }
    
    
    
    
    
    @GetMapping( path     = "/images/photos/{imagename}",
            produces = MediaType.IMAGE_JPEG_VALUE )
    public ResponseEntity<?> photo(@PathVariable("imagename") String imagename) {
        return ResponseEntity.of(photoService.download( imagename ));
    }
   
   
    @PostMapping( "/profile/{id}/photos/" )
    public ResponseEntity<?> saveImage( @PathVariable long id,
                                        @RequestParam MultipartFile file) throws IOException {
        Optional<Profile> profiletoUpdate = profileRepository.findById(id);
        
        if(profiletoUpdate.isPresent()){
            Profile profile = profiletoUpdate.get();
            
            byte[] bytes = file.getBytes();
            
            String name = photoService.upload(bytes);
            
            Photo newPhoto = new Photo(null, profile,name,
                    false, LocalDateTime.now().truncatedTo(TimeUnit.SECONDS.toChronoUnit()));
            
            profile.add(newPhoto);
            
            profileRepository.save(profile);
            
            return ResponseEntity.ok().build();
            //created(URI.create("api/photos/" + name)).build();
        }
        
        return ResponseEntity.notFound().build();
    }

    
    }
