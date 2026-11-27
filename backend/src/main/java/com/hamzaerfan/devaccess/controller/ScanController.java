package com.hamzaerfan.devaccess.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hamzaerfan.devaccess.dto.ScanDetailResponse;
import com.hamzaerfan.devaccess.dto.AnalysisResponse;
import com.hamzaerfan.devaccess.dto.ScanRequest;
import com.hamzaerfan.devaccess.service.ScanService;
import com.hamzaerfan.devaccess.dto.ScanHistoryResponse;
import jakarta.validation.Valid;

@RestController
public class ScanController {

	private final ScanService scanService;

	public ScanController(ScanService scanService) {
		this.scanService = scanService;
	}

	@PostMapping("/api/scans")
	public AnalysisResponse createScan(@Valid @RequestBody ScanRequest request) {
	    return scanService.createScan(request.getUrl());
	}

	@GetMapping("/api/scans")
	public List<ScanHistoryResponse> getScanHistory() {
		return scanService.getScanHistory();
	}

	@GetMapping("/api/scans/{id}")
	public ScanDetailResponse getScanById(@PathVariable Long id) {
		return scanService.getScanById(id);
	}
}
