package com.streamspot.webapp.opengallary.model;

import java.time.Instant;

import com.streamspot.webapp.opengallary.enums.MediaSource;
import com.streamspot.webapp.opengallary.enums.MediaStatus;
import com.streamspot.webapp.opengallary.enums.MediaType;
import com.streamspot.webapp.opengallary.enums.SocialPlatform;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Index;


@Entity
@Table(name = "media_asset", schema="opengallery", indexes = {
  @Index(name = "idx_media_status", columnList = "status"),
  @Index(name = "idx_media_created_at", columnList = "creation_time") //use column name
})
public class MediaAsset {
  @Id 
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "media_asset_id", nullable=false)
  private Long id;

  @Enumerated(EnumType.STRING) 
  @Column(name="media_type", nullable = false, length = 20)
  private MediaType type;

  @Enumerated(EnumType.STRING) 
  @Column(name="media_source", nullable = false, length = 20)
  private MediaSource source;

  @Enumerated(EnumType.STRING) 
  @Column(name="platform", nullable = false, length = 20)
  private SocialPlatform platform = SocialPlatform.OTHERS; // YOUTUBE/FACEBOOK/INSTAGRAM/NONE

  // Common metadata
  @Column(name="title", length = 200)
  private String title;

  @Column(name="description", length = 2000)
  private String description;

  @Column(name="tags", length = 1000)
  private String tagsCsv; // simple MVP; or use a join table for tags

  @Enumerated(EnumType.STRING) 
  @Column(name="status", nullable = false, length = 20)
  private MediaStatus status = MediaStatus.PUBLISHED;

  // EXTERNAL_URL fields
  @Column(name="url", length = 1000)
  private String externalUrl;

  @Column(name="thumbnail", length = 2000)
  private String externalThumbnailUrl;

  // UPLOADED fields (S3 keys, not public URLs)
  @Column(name="s3_original", length = 512)
  private String s3KeyOriginal;

  @Column(name="s3_transcode", length = 512)
  private String s3KeyTranscoded; // optional

  @Column(name="s3_thumbnail", length = 2000)
  private String s3KeyThumbnail; // image or video thumbnail

  // Technicals
  private Integer width;
  private Integer height;
  private Long sizeBytes;
  private Integer durationSec; // for videos

  // Counters (local)
  @Column(name="likes", nullable = false)
  private long localLikeCount = 0L;

  @Column(name="creation_time", nullable = false, updatable = false)
  private Instant createdAt = Instant.now();

  @Column(name="updated_time", nullable = false, updatable = false)
  private Instant updatedAt;

  @PreUpdate
  void onUpdate() { this.updatedAt = Instant.now(); }

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public MediaType getType() {
		return type;
	}
	
	public void setType(MediaType type) {
		this.type = type;
	}
	
	public MediaSource getSource() {
		return source;
	}
	
	public void setSource(MediaSource source) {
		this.source = source;
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
	
	public String getTagsCsv() {
		return tagsCsv;
	}
	
	public void setTagsCsv(String tagsCsv) {
		this.tagsCsv = tagsCsv;
	}
	
	public MediaStatus getStatus() {
		return status;
	}
	
	public void setStatus(MediaStatus status) {
		this.status = status;
	}
	
	public String getExternalUrl() {
		return externalUrl;
	}
	
	public void setExternalUrl(String externalUrl) {
		this.externalUrl = externalUrl;
	}
	
	public String getExternalThumbnailUrl() {
		return externalThumbnailUrl;
	}
	
	public void setExternalThumbnailUrl(String externalThumbnailUrl) {
		this.externalThumbnailUrl = externalThumbnailUrl;
	}
	
	public String getS3KeyOriginal() {
		return s3KeyOriginal;
	}
	
	public void setS3KeyOriginal(String s3KeyOriginal) {
		this.s3KeyOriginal = s3KeyOriginal;
	}
	
	public String getS3KeyTranscoded() {
		return s3KeyTranscoded;
	}
	
	public void setS3KeyTranscoded(String s3KeyTranscoded) {
		this.s3KeyTranscoded = s3KeyTranscoded;
	}
	
	public String getS3KeyThumbnail() {
		return s3KeyThumbnail;
	}
	
	public void setS3KeyThumbnail(String s3KeyThumbnail) {
		this.s3KeyThumbnail = s3KeyThumbnail;
	}
	
	public Integer getWidth() {
		return width;
	}
	
	public void setWidth(Integer width) {
		this.width = width;
	}
	
	public Integer getHeight() {
		return height;
	}
	
	public void setHeight(Integer height) {
		this.height = height;
	}
	
	public Long getSizeBytes() {
		return sizeBytes;
	}
	
	public void setSizeBytes(Long sizeBytes) {
		this.sizeBytes = sizeBytes;
	}
	
	public Integer getDurationSec() {
		return durationSec;
	}
	
	public void setDurationSec(Integer durationSec) {
		this.durationSec = durationSec;
	}
	
	public long getLocalLikeCount() {
		return localLikeCount;
	}
	
	public void setLocalLikeCount(long localLikeCount) {
		this.localLikeCount = localLikeCount;
	}
	
	public Instant getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	
	public Instant getUpdatedAt() {
		return updatedAt;
	}
	
	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
	  
}
