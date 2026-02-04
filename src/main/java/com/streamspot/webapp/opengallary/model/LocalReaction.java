package com.streamspot.webapp.opengallary.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "local_reaction", schema="opengallery", uniqueConstraints = {
  @UniqueConstraint(columnNames = {"media_id", "reactor_fingerprint"})
})
public class LocalReaction {
  @Id 
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="media_id", nullable=false)
  private Long mediaId;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "media_asset_id", nullable = false)
  private MediaAsset mediaAsset;

  @Column(name="reactor_fingerprint", nullable = false, length = 128)
  private String reactorFingerprint; // hash(IP + UA + day) or cookie

  @Column(name="creation_time", nullable = false)
  private Instant createdAt = Instant.now();

  public Long getMediaAssetId() {
	  return mediaId;
  }
	
	public void setMediaAssetId(Long mediaAssetId) {
		this.mediaId = mediaAssetId;
	}
	
	public MediaAsset getMediaAsset() {
		return mediaAsset;
	}
	
	public void setMediaAsset(MediaAsset mediaAsset) {
		this.mediaAsset = mediaAsset;
	}
	
	public String getReactorFingerprint() {
		return reactorFingerprint;
	}
	
	public void setReactorFingerprint(String reactorFingerprint) {
		this.reactorFingerprint = reactorFingerprint;
	}
	
	public Instant getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
  
  
}
