package com.lupolov.telegram.parser;

import org.jsoup.nodes.Document;

import java.io.IOException;

public class TestParse {
    public static void main(String[] args) throws IOException {
        String url = "https://swrailway.gov.ua/timetable/eltrain3-4/?geo2_list=1";

        Parser parser = new Parser(url);

        Document document = parser.getTimetableByStation("Одеса - Головна", "Роздільна 1");
        System.out.println(document.body());
    }
}
