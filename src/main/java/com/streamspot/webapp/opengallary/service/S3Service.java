package com.streamspot.webapp.opengallary.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

	@Autowired
    AmazonS3 amazonS3;   // AWS SDK client, auto-configured with your credentials
	
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;	
    
    @Value("${cloud.aws.region.static}")
    private String region;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    // Upload file
    public String uploadFile(String key, File file) {
        amazonS3.putObject(new PutObjectRequest(bucketName, key, file));
        return getFileUrl(key);
    }

    // Generate file URL
    public String getFileUrl(String key) {
        URL url = amazonS3.getUrl(bucketName, key);
        return url.toString();
    }

    // Delete file
    public void deleteFile(String key) {
        amazonS3.deleteObject(bucketName, key);
    }
    
    //upload files to s3
    public String uploadFile(MultipartFile file, String prefix) {
        try {
            String key = prefix + UUID.randomUUID() + "-" + file.getOriginalFilename();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            amazonS3.putObject(new PutObjectRequest(bucketName, key, file.getInputStream(), metadata));

            // Return full URL (optional) or just the key
            return key;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }
    
    public String generateS3ThumbnailUrl(String s3Key) {
    	System.out.println("s3Key :"+ s3Key);
    	String publicUrl = String.format("https://s3.%s.amazonaws.com/%s/%s",
    			region, bucketName, s3Key);

        return publicUrl;
    }

}
