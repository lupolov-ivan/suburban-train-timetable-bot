package com.lupolov.telegram.bot.handlers.trainsearch;

import lombok.Data;

@Data
public class TrainSearchRequestData {
    private String departureStationId;
    private String arrivalStationId;
}
