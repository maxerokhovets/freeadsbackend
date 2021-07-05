package com.nucldev.freeads.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nucldev.freeads.entities.Ad;
import com.nucldev.freeads.repositories.AdRepository;

@RestController
@CrossOrigin
@RequestMapping("/ads")
public class AdsController {
    
    @Autowired
    AdRepository adRepository;
    
   
    @GetMapping
    public List<Ad> getAds(){
        Ad ad = new Ad();
        ad.setId(1l);
        ad.setCaption("book");
        ad.setPrice(200);
        adRepository.save(ad);
        return adRepository.findAll();
    }
}
