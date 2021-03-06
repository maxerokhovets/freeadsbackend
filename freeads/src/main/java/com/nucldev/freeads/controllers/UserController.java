package com.nucldev.freeads.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nucldev.freeads.entities.User;
import com.nucldev.freeads.payload.ApiResponse;
import com.nucldev.freeads.payload.CurrentUserSummary;
import com.nucldev.freeads.repositories.UserRepository;
import com.nucldev.freeads.security.CurrentUser;
import com.nucldev.freeads.security.UserPrincipal;
import com.nucldev.freeads.service.AmazonClient;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    AmazonClient amazonClient;
    
    @Autowired
    UserRepository userRepository;

    @GetMapping("/me")
    public CurrentUserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        
        CurrentUserSummary userSummary = new CurrentUserSummary(currentUser.getId(), currentUser.getUsername(), 
                currentUser.getEmail(), currentUser.getRegistrationDate(), 
                currentUser.getAddsCount(), currentUser.getProfilePhotoUrl());
        
        return userSummary;
    }
    
    @PostMapping("/setphoto")
    public ResponseEntity<?> setProfilePhoto(@RequestParam("file") MultipartFile file, @CurrentUser UserPrincipal currentUser) {
        
        ApiResponse response = new ApiResponse(null, null);
        String probeContentResponse = null;
        
        try {
            probeContentResponse = Files.probeContentType(Paths.get(amazonClient.convertMultiPartToFile(file).getAbsolutePath()));
        } catch (IOException e) {
            response.setSuccess(false);
            response.setMessage("???? ?????????????? ?????????????????? ????????.");
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        
        String amazonResponse = "";
        if (probeContentResponse!=null) {
            String[] probeContentResponseArray = probeContentResponse.split("/");
            if (probeContentResponseArray[0].equals("image")) {
                amazonResponse = amazonClient.uploadFile(file);
            }else {
                response.setSuccess(false);
                response.setMessage("???? ?????????????? ?????????????????? ????????. ???????? ???? ???????????????? ????????????????????????.");
                return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
            }
        }
        
        
        if (amazonResponse.equals("")) {
            response.setSuccess(false);
            response.setMessage("???? ?????????????? ?????????????????? ????????.");
        } else {
            response.setSuccess(true);
            response.setMessage(amazonResponse);
            User user = userRepository.findById(currentUser.getId()).get();
            user.setProfilePhotoUrl(amazonResponse);
            userRepository.save(user);
        }
        
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    @DeleteMapping("/deletephoto")
    public ResponseEntity<?> deleteProfilePhoto(@RequestParam("url") String fileUrl) {
        
        ApiResponse response = new ApiResponse(null, null);
        String amazonResponse = amazonClient.deleteFileFromS3Bucket(fileUrl);
        
        if(amazonResponse.equals("Successfully deleted")) {
            response.setSuccess(true);
            response.setMessage(amazonResponse);
        } else {
            response.setSuccess(false);
            response.setMessage("Unsuccess!");
        }
        
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
