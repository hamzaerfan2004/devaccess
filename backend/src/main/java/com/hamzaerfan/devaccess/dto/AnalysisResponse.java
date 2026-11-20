package com.hamzaerfan.devaccess.dto;

public class AnalysisResponse {

    private String status;
    private AnalysisResults results;
    private String error;

    public AnalysisResponse(){
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public AnalysisResults getResults() {
        return results;
    }

    public void setResults(AnalysisResults results){
        this.results = results;
    }

    public String getError(){
        return error;
    }

    public void setError(String error){
        this.error = error;
    }
}