package com.hamzaerfan.devaccess.dto;

public class AnalysisRequest {

    private String url;

    public AnalysisRequest(){
    }

    public AnalysisRequest(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }
}
