package com.streamspot.webapp.opengallary.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.streamspot.webapp.opengallary.enums.MediaType;
import com.streamspot.webapp.opengallary.enums.SocialPlatform;
import com.streamspot.webapp.opengallary.model.MediaAsset;

public interface MediaAssetService {
 
    public MediaAsset uploadMedia(MultipartFile multipartFile, String title, MediaType type);
    
    public MediaAsset addExternalAsset(MediaAsset mediaAsset);

    public List<MediaAsset> listPublishedMedia(String platform);
    
    public String resolveDefaultThumbnail(SocialPlatform platform, String url);
}
