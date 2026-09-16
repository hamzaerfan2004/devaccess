package com.hamzaerfan.devaccess.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hamzaerfan.devaccess.dto.ScanRequest;
import com.hamzaerfan.devaccess.service.ScanService;

@RestController
public class ScanController {
	
	private final ScanService scanService;
	
	public ScanController(ScanService scanService) {
		this.scanService = scanService;
	}
	
	@PostMapping("/api/scans")
	public String createScan(@RequestBody ScanRequest request) {
	    return scanService.createScan(request.getUrl());
	}
}
