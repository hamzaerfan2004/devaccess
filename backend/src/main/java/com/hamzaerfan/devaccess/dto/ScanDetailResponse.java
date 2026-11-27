package com.hamzaerfan.devaccess.dto;

import java.time.Instant;

public class ScanDetailResponse {

    private Long id;
    private String url;
    private Instant scannedAt;
    private AnalysisResponse scanResults;

    public ScanDetailResponse(
        Long id,
        String url,
        Instant scannedAt,
        AnalysisResponse scanResults
    ) {
        this.id = id;
        this.url = url;
        this.scannedAt = scannedAt;
        this.scanResults = scanResults;
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

    public AnalysisResponse getScanResults() {
        return scanResults;
    }
}
