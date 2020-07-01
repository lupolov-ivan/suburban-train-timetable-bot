package com.lupolov.telegram.service;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StationCodeService {

    @Value("https://swrailway.gov.ua/timetable/eltrain3-4/?geo2_list=1")
    private String stationCodeRequestUrl;

    public Map<String,String> processStationCodeRequest() throws IOException {
        var doc = Jsoup.connect(stationCodeRequestUrl).get();

        var body = doc.body();
        var tables = body.getElementsByTag("tbody");
        var listStation = tables.stream().skip(2).findFirst().get();
        var allLinks = listStation.getElementsByTag("a");

        var links = allLinks.stream()
                .filter(link -> link.attr("href").startsWith("?sid="))
                .collect(Collectors.toCollection(Elements::new));

        Map<String, String> nameToId = new HashMap<>();

        links.forEach(link -> {
            String id = link.attr("href").substring(5,9);
            String name = link.text();
            nameToId.put(name,id);
        });

        return nameToId;
    }
    
}
