package com.streamspot.webapp.opengallary.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.streamspot.webapp.opengallary.model.MediaAsset;
import com.streamspot.webapp.opengallary.service.MediaAssetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {
	@Autowired
    MediaAssetService mediaAssetService;
    //private final ReactionService reactionService;

	@GetMapping("/media/getAsset/{platform}")
	public ResponseEntity<?> listMedia(@PathVariable String platform) {
	    try {
	        List<MediaAsset> assets = mediaAssetService.listPublishedMedia(platform);

	        if (assets == null || assets.isEmpty()) {
	            return ResponseEntity.ok(Collections.emptyList());
	        }

	        return ResponseEntity.ok(assets);

	    } catch (IllegalArgumentException e) {
	        // invalid platform value
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body(Map.of(
	                        "status", false,
	                        "error", "Invalid platform",
	                        "message", e.getMessage()
	                ));

	    } catch (RuntimeException e) {
	        // service/database failure
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of(
	                        "status", false,
	                        "error", "Server Error",
	                        "message", e.getMessage()
	                ));

	    } catch (Exception e) {
	        // unexpected/unhandled case
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of(
	                        "status", false,
	                        "error", "Unexpected Error",
	                        "message", "An unexpected error occurred. Please contact support."
	                ));
	    }
	}


//    @PostMapping("/media/{id}/like")
//    public ResponseEntity<Void> likeMedia(
//            @PathVariable Long id,
//            @RequestParam String userIdentifier) {
//        reactionService.addReaction(id, userIdentifier);
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping("/media/{id}/likes")
//    public ResponseEntity<Long> getLikes(@PathVariable Long id) {
//        return ResponseEntity.ok(reactionService.getReactionCount(id));
//    }
}
