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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nucldev.freeads.entities.Ad;
import com.nucldev.freeads.entities.User;
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
        
        ResponseEntity response = new ResponseEntity(null, null);
        
        if (title.equals("")) {
           response.ok(new ApiResponse(false, "Для публикации объявления заполните обязательные поля.")).status(HttpStatus.BAD_REQUEST);      
        } else {
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
            
            response.ok(new ApiResponse(true, "Объявление успешно опубликовано.")).status(HttpStatus.CREATED);
            adRepository.save(ad);  
        }
              
        return response;
    }
    
}
