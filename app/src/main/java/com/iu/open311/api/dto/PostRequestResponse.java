package com.iu.open311.api.dto;

import androidx.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostRequestResponse {

    @Nullable
    @JsonProperty("code")
    public Integer statusCode;

    @Nullable
    @JsonProperty("description")
    public String description;

    @Nullable
    @JsonProperty("service_request_id")
    public Integer serviceRequestId;
}
