package com.streamspot.webapp.opengallary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.streamspot.webapp.opengallary.dto.ExternalMediaRequest;
import com.streamspot.webapp.opengallary.dto.ExternalMediaResponse;
import com.streamspot.webapp.opengallary.enums.MediaType;
import com.streamspot.webapp.opengallary.model.MediaAsset;
import com.streamspot.webapp.opengallary.service.MediaAssetService;
import com.streamspot.webapp.opengallary.service.S3Service;

@RestController
@RequestMapping("/api/media-assets")
public class MediaAssetController {

	@Autowired
    MediaAssetService mediaAssetService;
	@Autowired
	S3Service s3Service;
	
	public static final String MULTIPART_FORM_DATA_VALUE = "multipart/form-data";

	@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("/upload")
    public ResponseEntity<MediaAsset> uploadMedia(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("type") MediaType type) {
        return ResponseEntity.ok(mediaAssetService.uploadMedia(file, title, type));
    }
	
	@PostMapping(value = "/external-resource", consumes = MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
	public ResponseEntity<ExternalMediaResponse> addExternalAsset(
	        @RequestPart("request") ExternalMediaRequest request,
	        @RequestPart(value = "thumbnailFile", required = false) MultipartFile thumbnailFile) {

	    ExternalMediaResponse response = new ExternalMediaResponse();

	    try {
	        MediaAsset mediaAsset = new MediaAsset();
	        mediaAsset.setExternalUrl(request.getExternalUrl());
	        mediaAsset.setTitle(request.getTitle());
	        mediaAsset.setDescription(request.getDescription());
	        mediaAsset.setType(request.getType());
	        mediaAsset.setPlatform(request.getPlatform());

	        // Thumbnail logic
	        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
	            String s3Key = s3Service.uploadFile(thumbnailFile, "thumbnails/");
	            mediaAsset.setS3KeyThumbnail(s3Key); // private key
	            // Construct public URL
	            String publicUrl = s3Service.generateS3ThumbnailUrl(s3Key);
	            mediaAsset.setExternalThumbnailUrl(publicUrl);
	        } else if (request.getThumbnailUrl() != null && !request.getThumbnailUrl().isBlank()) {
	            mediaAsset.setExternalThumbnailUrl(request.getThumbnailUrl());
	        } else if (Boolean.TRUE.equals(request.getUseDefaultThumbnail())) {
	            String defaultThumb = mediaAssetService.resolveDefaultThumbnail(
	                    request.getPlatform(),
	                    request.getExternalUrl()
	            );
	            mediaAsset.setExternalThumbnailUrl(defaultThumb);
	        }

	        MediaAsset saved = mediaAssetService.addExternalAsset(mediaAsset);

	        if (saved != null && saved.getId() != null) {
	            response.setStatus(true);
	            return ResponseEntity.status(HttpStatus.CREATED).body(response);
	        } else {
	            response.setStatus(false);
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	        }

	    } catch (IllegalArgumentException e) {
	        response.setStatus(false);
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    } catch (Exception e) {
	        response.setStatus(false);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}


}