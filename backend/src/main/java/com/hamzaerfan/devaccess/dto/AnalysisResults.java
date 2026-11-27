
package com.hamzaerfan.devaccess.dto;

import java.util.List;

public class AnalysisResults {
    private List<AnalysisViolation> passes;
    private List<AnalysisViolation> violations;
    private List<AnalysisViolation> incomplete;

    public AnalysisResults(){
    }

    public List<AnalysisViolation> getPasses(){
        return passes;
    }

    public void setPasses(List<AnalysisViolation> passes){
        this.passes = passes;
    }

    public List<AnalysisViolation> getViolations(){
        return violations;
    }

    public void setViolations(List<AnalysisViolation> violations){
        this.violations = violations;
    }

    public List<AnalysisViolation> getIncomplete(){
        return incomplete;
    }

    public void setIncomplete(List<AnalysisViolation> incomplete){
        this.incomplete = incomplete;
    }
}
