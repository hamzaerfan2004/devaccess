package com.hamzaerfan.devaccess.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import com.hamzaerfan.devaccess.client.AnalysisClient;
import com.hamzaerfan.devaccess.dto.AnalysisResponse;
import com.hamzaerfan.devaccess.entity.Scan;
import com.hamzaerfan.devaccess.repository.ScanRepository;
import com.hamzaerfan.devaccess.dto.ScanHistoryResponse;
import com.hamzaerfan.devaccess.dto.ScanDetailResponse;

@Service
public class ScanService {

	private final AnalysisClient analysisClient;
	private final ScanRepository scanRepository;
	private final ObjectMapper objectMapper;

	public ScanService(
		AnalysisClient analysisClient,
		ScanRepository scanRepository,
		ObjectMapper objectMapper
	){
		this.analysisClient = analysisClient;
		this.scanRepository = scanRepository;
		this.objectMapper = objectMapper;
	}
	public AnalysisResponse createScan(String url) {
		AnalysisResponse response = analysisClient.scan(url);

		if (response.getResults() != null && "completed".equals(response.getStatus())){

			try {
				String resultsJson = objectMapper.writeValueAsString(response);

				Scan scan = new Scan();
				scan.setUrl(url);
				scan.setScannedAt(Instant.now());
				scan.setResultsJson(resultsJson);

				scanRepository.save(scan);
			} catch (JacksonException exception) {
				throw new IllegalStateException("Failed to store scan results", exception);
			}
		}

		return response;
	}

	public List<ScanHistoryResponse> getScanHistory() {
		return scanRepository.findAllByOrderByScannedAtDesc()
			.stream()
			.map(scan -> new ScanHistoryResponse(
				scan.getId(),
				scan.getUrl(),
				scan.getScannedAt()
			))
			.toList();
	}

	public ScanDetailResponse getScanById(Long id) {
		Scan scan = scanRepository.findById(id)
			.orElseThrow(() -> new java.util.NoSuchElementException(
				"Scan not found with id: " + id
			));

		try {
			AnalysisResponse scanResults = objectMapper.readValue(
				scan.getResultsJson(),
				AnalysisResponse.class
			);

			return new ScanDetailResponse(
				scan.getId(),
				scan.getUrl(),
				scan.getScannedAt(),
				scanResults
			);
		} catch (JacksonException exception) {
			throw new IllegalStateException("Failed to retrieve scan results", exception);
		}
	}
}
