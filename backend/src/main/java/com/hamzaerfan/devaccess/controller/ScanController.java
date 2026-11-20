package com.hamzaerfan.devaccess.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hamzaerfan.devaccess.dto.AnalysisResponse;
import com.hamzaerfan.devaccess.dto.ScanRequest;
import com.hamzaerfan.devaccess.service.ScanService;
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
}
