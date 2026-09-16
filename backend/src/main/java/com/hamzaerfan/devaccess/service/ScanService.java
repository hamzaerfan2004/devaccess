package com.hamzaerfan.devaccess.service;

import org.springframework.stereotype.Service;

@Service
public class ScanService {
	public String createScan(String url) {
		return "Scan created for: " + url;
	}
}