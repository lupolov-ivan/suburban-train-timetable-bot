package com.lupolov.telegram.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Parser {

    private String url;

    public Parser(String url) {
        this.url = url;
    }

    public Map<String,String> getAllStation(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();

        Element body = doc.body();
        Elements tables = body.getElementsByTag("tbody");

        Element listStation = tables.stream().skip(2).findFirst().get();

        Elements allLinks = listStation.getElementsByTag("a");

        Elements links = allLinks.stream()
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

    public Document getTimetableByStation(String from, String to) throws IOException {

        Map<String, String> nameToId = getAllStation(url);

        String sid1 = nameToId.get(from);
        String sid2 = nameToId.get(to);


        String request = "https://swrailway.gov.ua/timetable/eltrain3-4/?sid1="+ sid1 +"&sid2="+ sid2;

        return Jsoup.connect(request).get();
    }
}
