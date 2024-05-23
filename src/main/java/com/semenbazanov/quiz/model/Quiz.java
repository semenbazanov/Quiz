
package com.semenbazanov.quiz.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "response_code",
    "results"
})
public class Quiz {

    @JsonProperty("response_code")
    private int responseCode;
    @JsonProperty("results")
    private List<Result> results = new ArrayList<Result>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Quiz() {
    }

    /**
     * 
     * @param results
     * @param responseCode
     */
    public Quiz(int responseCode, List<Result> results) {
        super();
        this.responseCode = responseCode;
        this.results = results;
    }

    @JsonProperty("response_code")
    public int getResponseCode() {
        return responseCode;
    }

    @JsonProperty("response_code")
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    @JsonProperty("results")
    public List<Result> getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(List<Result> results) {
        this.results = results;
    }

}
