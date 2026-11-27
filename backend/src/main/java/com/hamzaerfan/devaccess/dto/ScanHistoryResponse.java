package com.hamzaerfan.devaccess.dto;

import java.time.Instant;

public class ScanHistoryResponse {

    private Long id;
    private String url;
    private Instant scannedAt;

    public ScanHistoryResponse(Long id, String url, Instant scannedAt) {
        this.id = id;
        this.url = url;
        this.scannedAt = scannedAt;
    }

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public Instant getScannedAt() {
        return scannedAt;
    }
}
