package com.klinbee.dictionaryseeds;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DictionaryReader {

    public static String[] read(String filename, boolean upperCase) {
        List<String> words = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (upperCase) {
                    words.add(line.trim().toUpperCase());
                }
                else {
                    words.add(line.trim().toLowerCase());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return words.toArray(new String[0]);
    }

}
