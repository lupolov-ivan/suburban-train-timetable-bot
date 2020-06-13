package com.lupolov.telegram.uz.parser;

import java.io.IOException;

public class TestParse {
    public static void main(String[] args) throws IOException {

        Parser parser = new Parser();
        System.out.println();
        parser.getTimetableByStation("Одеса - Головна", "Роздільна 1").forEach(System.out::println);
    }
}
