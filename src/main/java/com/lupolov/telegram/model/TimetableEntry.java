package com.lupolov.telegram.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TimetableEntry {

    private Integer trainNum;
    private String schedule;
    private String route;
    private LocalTime departure;
    private LocalTime arrival;
    private LocalTime travelTime;
    private Integer distance;
    private LocalDate fromRelevance;
    private LocalDate toRelevance;

    public LocalTime getTravelTime() {
        return arrival.minusHours(departure.getHour()).minusMinutes(departure.getMinute());
    }
}
