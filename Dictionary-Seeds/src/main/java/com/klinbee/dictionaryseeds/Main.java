package com.klinbee.dictionaryseeds;

public class Main {

    public static void main(String[] args) {
        String outputPath = "src/main/resources/output/";
        String[] wordList = DictionaryReader.read("src/main/resources/words.txt", false);
        SeedDictionaryWriter.writeSeeds(wordList, outputPath + "word_seeds.txt", false);
        SeedDictionaryWriter.writeWordsAndSeeds(wordList, outputPath + "words_and_seeds.txt", false);
        SeedDictionaryWriter.writeSeeds(wordList, outputPath + "words_cased_seeds.txt", true);
        SeedDictionaryWriter.writeWordsAndSeeds(wordList, outputPath + "words_cased_and_seeds.txt", true);
    }
}
