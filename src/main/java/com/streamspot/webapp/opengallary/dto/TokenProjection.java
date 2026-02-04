package com.streamspot.webapp.opengallary.dto;

import java.time.LocalDateTime;

public interface TokenProjection {
    String getToken();
    LocalDateTime getTokenExpiry();
}
