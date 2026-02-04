package com.streamspot.webapp.opengallary.dto;

import org.springframework.web.multipart.MultipartFile;

import com.streamspot.webapp.opengallary.enums.MediaType;
import com.streamspot.webapp.opengallary.enums.SocialPlatform;

public class ExternalMediaRequest {
    private MediaType type;
    private SocialPlatform platform;  
    private String title;
    private String description;
    private String externalUrl;
    private String thumbnailUrl;          // optional - manual override
    private Boolean useDefaultThumbnail;  // optional - fallback if true
 // optional uploaded thumbnail (multi-part)
    private MultipartFile thumbnailFile; 
    
	public MediaType getType() {
		return type;
	}
	public void setType(MediaType type) {
		this.type = type;
	}
	public SocialPlatform getPlatform() {
		return platform;
	}
	public void setPlatform(SocialPlatform platform) {
		this.platform = platform;
	}
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
	public String getExternalUrl() {
		return externalUrl;
	}
	public void setExternalUrl(String externalUrl) {
		this.externalUrl = externalUrl;
	}
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	public Boolean getUseDefaultThumbnail() {
		return useDefaultThumbnail;
	}
	public void setUseDefaultThumbnail(Boolean useDefaultThumbnail) {
		this.useDefaultThumbnail = useDefaultThumbnail;
	}
	public MultipartFile getThumbnailFile() {
		return thumbnailFile;
	}
	public void setThumbnailFile(MultipartFile thumbnailFile) {
		this.thumbnailFile = thumbnailFile;
	}
    
}

