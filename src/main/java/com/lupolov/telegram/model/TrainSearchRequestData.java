package com.lupolov.telegram.model;

import lombok.Data;

@Data
public class TrainSearchRequestData {
    private String departureStationId;
    private String arrivalStationId;
}
