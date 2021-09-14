package com.nucldev.freeads.controllers;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nucldev.freeads.entities.Ad;
import com.nucldev.freeads.entities.User;
import com.nucldev.freeads.payload.AdUpdateRequest;
import com.nucldev.freeads.payload.ApiResponse;
import com.nucldev.freeads.repositories.AdRepository;
import com.nucldev.freeads.repositories.UserRepository;
import com.nucldev.freeads.security.CurrentUser;
import com.nucldev.freeads.security.UserPrincipal;
import com.nucldev.freeads.service.AmazonClient;

@RestController
@CrossOrigin
@RequestMapping("/ads")
public class AdsController {
    
    @Autowired
    AdRepository adRepository;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    AmazonClient amazonClient;
    
   
    @GetMapping("/getads")
    public List<Ad> getAds(){

        return adRepository.findAll();
    }
    
    @PostMapping("/createad")
    public ResponseEntity<?> createAd(@RequestParam("category") String category, @RequestParam("title") String title,
            @RequestParam("description") String description, @RequestParam("price") Double price,
            @RequestParam("cover") MultipartFile cover, @CurrentUser UserPrincipal currentUser) {       
        
        if (title.equals("")) {
           return new ResponseEntity(new ApiResponse(false, "Для публикации объявления заполните обязательные поля."), HttpStatus.BAD_REQUEST);      
        }
        
        Ad ad = new Ad();
        ad.setCategory(category);
        ad.setTitle(title);
        ad.setDescription(description);
        ad.setPrice(price);
        ad.setCreationDate(new Date());
        ad.setActive(true);
        String amazonResponse = amazonClient.uploadFile(cover);
        if (!amazonResponse.equals("")) {
            ad.setAdCoverUrl(amazonResponse);
        }
        ad.setUsername(currentUser.getUsername());
        User user = userRepository.findById(currentUser.getId()).get();
        user.setAddsCount(user.getAddsCount()+1);
        userRepository.save(user);            
        adRepository.save(ad);  
            
        return new ResponseEntity(new ApiResponse(true, "Объявление успешно опубликовано."), HttpStatus.CREATED);
    }
    
    @PutMapping("/updatead/{id}")
    public ResponseEntity<?> updateAd(@RequestBody AdUpdateRequest updateRequest, @PathVariable Long id){
        
        Ad ad = adRepository.findById(id).get();
        
        if (!ad.getTitle().equals(updateRequest.getTitle())) {
            ad.setTitle(updateRequest.getTitle());
        }
        if (!ad.getDescription().equals(updateRequest.getDescription())) {
            ad.setDescription(updateRequest.getDescription());
        }
        if (!ad.getCategory().equals(updateRequest.getCategory())) {
            ad.setCategory(updateRequest.getCategory());
        }
        if (ad.getPrice()!=updateRequest.getPrice()) {
            ad.setPrice(updateRequest.getPrice());
        }
        if (!ad.getAdCoverUrl().equals(updateRequest.getAdCoverUrl())) {
            ad.setAdCoverUrl(updateRequest.getAdCoverUrl());
        }
        if (!ad.getItemPhotosUrls().equals(updateRequest.getItemPhotosUrls())) {
            ad.setItemPhotosUrls(updateRequest.getItemPhotosUrls());
        }
        
        if (updateRequest.isActive()) {
            ad.setActive(true);
        } else {
            ad.setActive(false);
        }
        
        
        adRepository.save(ad);
        
        
        return new ResponseEntity(ad, HttpStatus.OK);        
    }
    
}
