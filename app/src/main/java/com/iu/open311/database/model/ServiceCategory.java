package com.iu.open311.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity(tableName = "service_category")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceCategory {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "service_code")
    @JsonProperty("service_code")
    public Integer serviceCode;

    @NonNull
    @ColumnInfo(name = "service_name")
    @JsonProperty("service_name")
    public String serviceName;

    @NonNull
    @ColumnInfo(name = "group")
    @JsonProperty("group")
    public String group;

    @NonNull
    @ColumnInfo(name = "keywords")
    @JsonProperty("keywords")
    public String keywords;
}
