package com.lupolov.telegram.service;

import com.lupolov.telegram.model.TimetableEntry;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TimetableService {

    @Value("https://swrailway.gov.ua/timetable/eltrain3-4/?sid1=%s&sid2=%s")
    private String requestTemplate;

    public List<TimetableEntry> getTimetableByStation(String from, String to) throws Exception {

        var url = String.format(requestTemplate, from, to);

        var doc = Jsoup.connect(url).get();

        var tableRows1 = doc.getElementsByClass("on");
        var tableRows2 = doc.getElementsByClass("onx");

        var timetable = Stream.concat(
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
            entry.setDistance(Double.parseDouble(extText(rows[8])));
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
