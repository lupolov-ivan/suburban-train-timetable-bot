package com.lupolov.telegram.uz.parser;

import com.lupolov.telegram.model.TimetableEntry;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {

    private final String template = "https://swrailway.gov.ua/timetable/eltrain3-4/?";

    public Map<String,String> getAllStation() throws IOException {

        var paramForListStation = "geo2_list=1";
        var url = template + paramForListStation;

        var doc = Jsoup.connect(url).get();

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

    public List<TimetableEntry> getTimetableByStation(String from, String to) throws IOException {

        Map<String, String> nameToId = getAllStation();

        var sid1 = "sid1="+ nameToId.get(from);
        var sid2 = "&sid2="+ nameToId.get(to);

        System.out.println(from + " ============> "+ to);
        System.out.println(sid1 + " ============> "+ sid2);

        var url = template + sid1 + sid2;

        Document doc = Jsoup.connect(url).get();

        Elements tableRows1 = doc.getElementsByClass("on");
        Elements tableRows2 = doc.getElementsByClass("onx");

        Elements timetable = Stream.concat(
                                        tableRows1.stream(),
                                        tableRows2.stream())
                                            .filter(element -> element.attr("style").contains("CURSOR:pointer"))
                                            .collect(Collectors.toCollection(Elements::new));

        List<TimetableEntry> entries = new ArrayList<>();

        timetable.forEach(elem -> {
            var rows = elem.getElementsByTag("td").toArray();
            var entry = new TimetableEntry();

            entry.setTrainNum(Integer.parseInt(extText(rows[0])));
            entry.setSchedule(extText(rows[1]));
            entry.setRoute(extText(rows[2]));
            entry.setDeparture(LocalTime.parse(extText(rows[4])));
            entry.setArrival(LocalTime.parse(extText(rows[5])));
            entry.setDistance(Integer.parseInt(extText(rows[8])));
            entry.setFromRelevance(LocalDate.parse(extText(rows[9])));
            entry.setToRelevance(LocalDate.parse(extText(rows[10])));

            entries.add(entry);
        });

        return entries;
    }
    private String extText(Object object) {
        return ((Element) object).text();
    }
}
