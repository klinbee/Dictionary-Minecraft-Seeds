package com.klinbee.dictionaryseeds;

public class Main {

    public static void main(String[] args) {
        String[] wordList = DictionaryReader.read("words.txt", false);
        SeedDictionaryWriter.writeSeeds(wordList, "word_seeds.txt", false);
        SeedDictionaryWriter.writeWordsAndSeeds(wordList, "words_and_seeds.txt", false);
        SeedDictionaryWriter.writeSeeds(wordList, "words_cased_seeds.txt", true);
        SeedDictionaryWriter.writeWordsAndSeeds(wordList, "words_cased_and_seeds.txt", true);
    }
}
