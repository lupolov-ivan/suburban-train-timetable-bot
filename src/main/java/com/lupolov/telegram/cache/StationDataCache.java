package com.lupolov.telegram.cache;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Getter
public class StationDataCache {

    private final Map<String, String> stationNameToId = new HashMap<>();

    public Optional<String> getStationName(String stationNameParam) {
        return stationNameToId.keySet().stream()
                .filter(s -> s.equals(stationNameParam))
                .findFirst();
    }

    public Optional<String> getStationCode(String stationName) {
        return Optional.ofNullable(stationNameToId.get(stationName));
    }

    public void addStationToCache(String stationName, String stationId) {
        stationNameToId.put(stationName, stationId);
    }
}
