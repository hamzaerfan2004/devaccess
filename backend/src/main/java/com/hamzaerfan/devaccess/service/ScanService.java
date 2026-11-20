package com.hamzaerfan.devaccess.service;

import org.springframework.stereotype.Service;

import com.hamzaerfan.devaccess.client.AnalysisClient;
import com.hamzaerfan.devaccess.dto.AnalysisResponse;

@Service
public class ScanService {

	private final AnalysisClient analysisClient;

	public ScanService(AnalysisClient analysisClient){
		this.analysisClient = analysisClient;
	}
	public AnalysisResponse createScan(String url) {
		return analysisClient.scan(url);
	}
}