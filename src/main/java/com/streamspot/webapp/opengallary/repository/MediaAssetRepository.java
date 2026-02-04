package com.streamspot.webapp.opengallary.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.streamspot.webapp.opengallary.enums.MediaStatus;
import com.streamspot.webapp.opengallary.enums.SocialPlatform;
import com.streamspot.webapp.opengallary.model.MediaAsset;

@Repository
public interface MediaAssetRepository extends JpaRepository<MediaAsset, Long> {
	
    List<MediaAsset> findByStatus(MediaStatus status);
    List<MediaAsset> findByPlatformAndStatusOrderByCreatedAtDesc(SocialPlatform platform, MediaStatus status);
	List<MediaAsset> findByStatusOrderByCreatedAtDesc(MediaStatus published);

}
