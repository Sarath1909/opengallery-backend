package com.streamspot.webapp.opengallary.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.streamspot.webapp.opengallary.enums.MediaSource;
import com.streamspot.webapp.opengallary.enums.MediaStatus;
import com.streamspot.webapp.opengallary.enums.MediaType;
import com.streamspot.webapp.opengallary.enums.SocialPlatform;
import com.streamspot.webapp.opengallary.exception.MediaUploadException;
import com.streamspot.webapp.opengallary.model.MediaAsset;
import com.streamspot.webapp.opengallary.repository.MediaAssetRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MediaAssetServiceImpl implements MediaAssetService {
	
	@Autowired
    MediaAssetRepository mediaAssetRepository;
	@Autowired
    S3Service s3Service; // custom service to handle AWS S3 operations
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MediaAssetServiceImpl.class);

    @Transactional
    public MediaAsset uploadMedia(MultipartFile multipartFile, String title, MediaType type) {
        try {
        	// Generate unique key for S3 (e.g., timestamp + original filename)
            String key = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            
            // Convert MultipartFile → File
            File file = convertMultiPartToFile(multipartFile);

            // Upload to S3
            String s3Url = s3Service.uploadFile(key, file);

            // Cleanup local file
            file.delete();

            // Save entity in DB
            MediaAsset asset = new MediaAsset();
            asset.setTitle(title);
            asset.setType(type);
            asset.setSource(MediaSource.UPLOADED);
            asset.setExternalUrl(s3Url);
            asset.setStatus(MediaStatus.PUBLISHED);
            return mediaAssetRepository.save(asset);
        } catch (Exception e) {
            throw new MediaUploadException("Failed to upload media", e);
        }
    }
    
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }
    
    @Transactional
    public MediaAsset addExternalAsset(MediaAsset mediaAsset) {
        if (mediaAsset == null) {
            throw new IllegalArgumentException("Media asset cannot be null");
        }
        if (mediaAsset.getExternalUrl() == null || mediaAsset.getExternalUrl().isBlank()) {
            throw new IllegalArgumentException("Media asset URL must be provided");
        }

        mediaAsset.setSource(MediaSource.EXTERNAL_URL);
        mediaAsset.setStatus(MediaStatus.PUBLISHED);
        mediaAsset.setUpdatedAt(Instant.now());
        
        try {
            return mediaAssetRepository.save(mediaAsset);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add external media asset", e);
        }
    }

    /**
     * Helper to derive default thumbnail URL from known platforms
     */
    public String resolveDefaultThumbnail(SocialPlatform platform, String url) {
        if (platform == null || url == null) return null;

        switch (platform) {
	        case YOUTUBE:
	            String videoId = extractYouTubeId(url);
	            if (videoId == null) return null;
	            return "https://img.youtube.com/vi/" + videoId + "/0.jpg";

            case FACEBOOK:
                // Might need Graph API call, or fallback static placeholder
                return null;
            case INSTAGRAM:
            	return fetchInstagramThumbnail(url);
            default:
                return null;
        }
    }

    private String extractYouTubeId(String url) {
        if (url == null || url.isBlank()) return null;
        String s = url.trim();

        // 1) try to get v= param: ?v=VIDEOID or &v=VIDEOID
        Matcher m = Pattern.compile("[?&]v=([^&?#]+)").matcher(s);
        if (m.find()) {
            return m.group(1);
        }

        // 2) try common path patterns: youtu.be/ID  or /embed/ID or /v/ID
        m = Pattern.compile("(?:youtu\\.be/|youtube\\.com/(?:embed/|v/))([^?&/#]+)").matcher(s);
        if (m.find()) {
            return m.group(1);
        }

        // 3) fallback: take last path segment and strip query/hash if present
        try {
            String last = s.substring(s.lastIndexOf('/') + 1);
            // remove query/hash parts if any
            int qIdx = last.indexOf('?');
            if (qIdx != -1) last = last.substring(0, qIdx);
            int hashIdx = last.indexOf('#');
            if (hashIdx != -1) last = last.substring(0, hashIdx);
            int ampIdx = last.indexOf('&');
            if (ampIdx != -1) last = last.substring(0, ampIdx);
            // basic validation: YouTube ids are usually 11 chars alnum-like, but return anyway
            return last.isEmpty() ? null : last;
        } catch (Exception ex) {
            return null;
        }
    }

    private String fetchInstagramThumbnail(String postUrl) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String html = restTemplate.getForObject(postUrl, String.class);

            if (html != null) {
                // Regex to find og:image tag
                Pattern pattern = Pattern.compile("<meta property=\"og:image\" content=\"(.*?)\"");
                Matcher matcher = pattern.matcher(html);
                if (matcher.find()) {
                    return matcher.group(1);
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Failed to fetch Instagram thumbnail by scraping: {}", ex);
        }
        return null;
    }


    public List<MediaAsset> listPublishedMedia(String platform) {
        SocialPlatform platformName;

        if (platform == null || platform.isBlank()) {
            throw new IllegalArgumentException("Platform must not be null or empty");
        }

        try {
            platformName = SocialPlatform.valueOf(platform.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Cannot recognize SocialPlatform: " + platform);
        }

        try {
        	if(platformName == SocialPlatform.ALL)
        		return mediaAssetRepository.findByStatusOrderByCreatedAtDesc(MediaStatus.PUBLISHED);	
        	else
        		return mediaAssetRepository.findByPlatformAndStatusOrderByCreatedAtDesc(platformName, MediaStatus.PUBLISHED);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve assets for platform: " + platform, e);
        }
    }

}

