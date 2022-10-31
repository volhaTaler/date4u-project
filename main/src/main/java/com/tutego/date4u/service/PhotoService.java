package com.tutego.date4u.service;

import com.tutego.date4u.core.photo.Photo;
import com.tutego.date4u.core.photo.Thumbnail;
import com.tutego.date4u.interfaces.shell.FileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;

import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
@Validated
public class PhotoService {
    
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    private final FileSystem fs;
    private final Thumbnail thumbnail;
    
    public PhotoService(FileSystem fs, Thumbnail thumbnail) {
        this.fs = fs;
        this.thumbnail = thumbnail;
    }
    
    @Cacheable("date4u.filesystem.file")
    public Optional<byte[]> download(String name) {
        try {
            return Optional.of(fs.load(name + ".jpg"));
        } catch (UncheckedIOException e) {
            return Optional.empty();
        }
    }
    
    @Cacheable(cacheNames = "date4u.filesystem.file", key = "#photo.name")
    public Optional<byte[]> download(@Valid Photo photo) {
        return download(photo.getName());
    }
  
  /**
   *
   * @param imageBytes input image uploaded by user
   * @return proccessed image
   */
    public String upload(byte[] imageBytes) {
        Future<byte[]> thumbnailBytes = thumbnail.thumbnail(imageBytes);
        
        String imageName = UUID.randomUUID().toString();
        
        //    NewPhotoEvent newPhotoEvent = new NewPhotoEvent( imageName, OffsetDateTime.now() );
        //    publisher.publishEvent( newPhotoEvent );
        
        // First: store original image
        //  fs.store( imageName + ".jpg", imageBytes );
        
        // Second: store thumbnail
        try {
            log.info("upload");
            
            fs.store(imageName + ".jpg", thumbnailBytes.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException(e.getCause());
        }
        
        return imageName;
    }
    
    
}
