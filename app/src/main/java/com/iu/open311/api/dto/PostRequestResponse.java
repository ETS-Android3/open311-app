package com.iu.open311.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostRequestResponse {
    @JsonProperty("service_request_id")
    public Integer serviceRequestId;
}
