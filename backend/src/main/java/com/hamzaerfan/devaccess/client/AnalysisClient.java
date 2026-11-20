package com.hamzaerfan.devaccess.client;

import org.springframework.web.client.RestClientException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.hamzaerfan.devaccess.dto.AnalysisRequest;
import com.hamzaerfan.devaccess.dto.AnalysisResponse;
import com.hamzaerfan.devaccess.exception.AnalysisServiceException;
import com.hamzaerfan.devaccess.exception.ScanFailedException;

@Component
public class AnalysisClient {

    private final RestClient restClient;

    public AnalysisClient(RestClient analysisRestClient){
        this.restClient = analysisRestClient;
    }

    public AnalysisResponse scan(String url){

        AnalysisRequest request = new AnalysisRequest(url);

        try{
            return restClient.post()
            .uri("/scan")
            .body(request)
            .retrieve()
            .body(AnalysisResponse.class);
        } catch (HttpClientErrorException.UnprocessableContent exception){
            throw new ScanFailedException(
                "Unable to scan the target website",
                exception
            );
        } catch (RestClientException exception){
            throw new AnalysisServiceException("Unable to communicate with the analysis service", exception);
        }
    }
}
