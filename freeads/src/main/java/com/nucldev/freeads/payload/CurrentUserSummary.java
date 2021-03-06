package com.nucldev.freeads.payload;

import java.util.Date;

public class CurrentUserSummary {

    private Long id;
    
    private String username;
    
    private String email;
    
    private Date registrationDate;
    
    private Long addsCount;
    
    private String profilePhotoUrl;
    
    
    public CurrentUserSummary(Long id, String username, String email, 
            Date registrationDate, Long addsCount, String profilePhotoUrl) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.registrationDate = registrationDate;
        this.addsCount = addsCount;
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public Long getAddsCount() {
        return addsCount;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    } 
    
}
