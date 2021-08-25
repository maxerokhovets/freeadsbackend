package com.nucldev.freeads.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;


import javax.persistence.Id;

@Entity
@Table(name = "ads")
public class Ad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "text")
    private String category;
    
    @Column(columnDefinition = "text")
    private String title;
    
    @Column(columnDefinition = "text")
    private String description;
    
    private Double price;
    
    private Date creationDate; 
    
    @Column(columnDefinition = "text")
    private String adCoverUrl;
    
    @ElementCollection
    private List<String> itemPhotosUrls;
    
    private boolean isActive;
    
    private String username;
    
    public Ad() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdCoverUrl() {
        return adCoverUrl;
    }

    public void setAdCoverUrl(String adCoverUrl) {
        this.adCoverUrl = adCoverUrl;
    }

    public List<String> getItemPhotosUrls() {
        return itemPhotosUrls;
    }

    public void setItemPhotosUrls(List<String> itemPhotosUrls) {
        this.itemPhotosUrls = itemPhotosUrls;
    }   
    
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
}
