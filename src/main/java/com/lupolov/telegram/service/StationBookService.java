package com.lupolov.telegram.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lupolov.telegram.model.Station;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StationBookService {

    @Value("https://swrailway.gov.ua/timetable/eltrain3-4/?JSON=station&term={stationNamePart}")
    private String stationRequestTemplate;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public List<Station> getStationListByName(String stationNamePart) throws JsonProcessingException {

//        var url = "https://swrailway.gov.ua/timetable/eltrain3-4/?JSON=station&term=" + stationNamePart;
//
//        Document document = Jsoup.connect(url).get();
//
//        var html = document.outerHtml();

        ResponseEntity<String> response = restTemplate.getForEntity(
                stationRequestTemplate,
                String.class,
                stationNamePart
        );

        String stationsArray = response.getBody();

        if (stationsArray == null) {
            return Collections.emptyList();
        }

        return objectMapper.readValue(stationsArray, new TypeReference<List<Station>>() {});
    }
}
