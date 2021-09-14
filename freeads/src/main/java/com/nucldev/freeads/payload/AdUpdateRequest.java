package com.nucldev.freeads.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AdUpdateRequest {
    
    private String title;
    
    private String description;
    
    private String category;
    
    private Double price;
    
    private String adCoverUrl;
    
    private List<String> itemPhotosUrls;
    
    @JsonProperty("isActive")
    private boolean isActive;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    
    
}
