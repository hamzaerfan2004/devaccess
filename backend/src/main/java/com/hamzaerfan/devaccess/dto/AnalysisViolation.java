package com.hamzaerfan.devaccess.dto;

import java.util.List;

public class AnalysisViolation {

    private String ruleId;
    private String impact;
    private String description;
    private String help;
    private String helpUrl;
    private List<AnalysisNode> nodes;

    public AnalysisViolation(){
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String getHelpUrl() {
        return helpUrl;
    }

    public void setHelpUrl(String helpUrl) {
        this.helpUrl = helpUrl;
    }

    public List<AnalysisNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<AnalysisNode> nodes) {
        this.nodes = nodes;
    }

}
