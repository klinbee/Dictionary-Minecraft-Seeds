package com.klinbee.dictionaryseeds;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class SeedDictionaryWriter {

    public static void writeSeeds(String[] wordList, String outputFileName, boolean writePermutations) {
        Iterator<String> iterator = writePermutations ?
                new WordPermutationIterator(wordList) :
                new WordIterator(wordList);
        writeToFile(outputFileName, iterator, OutputFormat.SEEDS_ONLY);
        notifyFinishTask(outputFileName, writePermutations);
    }

    public static void writeWords(String[] wordList, String outputFileName, boolean writePermutations) {
        Iterator<String> iterator = writePermutations ?
                new WordPermutationIterator(wordList) :
                new WordIterator(wordList);
        writeToFile(outputFileName, iterator, OutputFormat.WORDS_ONLY);
        notifyFinishTask(outputFileName, writePermutations);
    }

    public static void writeWordsAndSeeds(String[] wordList, String outputFileName, boolean writePermutations) {
        Iterator<String> iterator = writePermutations ?
                new WordPermutationIterator(wordList) :
                new WordIterator(wordList);
        writeToFile(outputFileName, iterator, OutputFormat.CSV);
        notifyFinishTask(outputFileName, writePermutations);
    }

    private static void notifyFinishTask(String outputFileName, boolean writePermutations) {
        String permutationsMessage = writePermutations ?
                " with permutations" :
                " without permutations";
        System.out.println("Finished writing file " + outputFileName + permutationsMessage);
    }

    private static void writeToFile(String outputFileName, Iterator<String> wordIterator, OutputFormat format) {
        try {
            File file = new File(outputFileName);
            File parentDir = file.getParentFile();

            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(outputFileName);
            if (format == OutputFormat.CSV) {
                fileWriter.write("word,seed\n");
            }

            while (wordIterator.hasNext()) {
                String word = wordIterator.next();
                switch (format) {
                    case SEEDS_ONLY:
                        fileWriter.write(stringToSeed(word) + "\n");
                        break;
                    case WORDS_ONLY:
                        fileWriter.write(word + "\n");
                        break;
                    case CSV:
                        fileWriter.write(word + "," + stringToSeed(word) + "\n");
                        break;
                }
            }

            fileWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static long stringToSeed(String word) {
        // Optimized String.hashCode() calculation
        int h = 0;
        final int len = word.length();
        for (int i = 0; i < len; i++) {
            h = 31 * h + word.charAt(i);
        }
        return h;
    }

    private enum OutputFormat {
        SEEDS_ONLY, WORDS_ONLY, CSV
    }

    private static class WordIterator implements Iterator<String> {
        private final String[] wordList;
        private int wordIndex;

        public WordIterator(String[] wordList) {
            this.wordList = wordList;
            this.wordIndex = 0;
        }

        @Override
        public boolean hasNext() {
            return wordIndex < wordList.length;
        }

        @Override
        public String next() {
            return wordList[wordIndex++];
        }
    }

    private static class WordPermutationIterator implements Iterator<String> {
        private final String[] wordList;
        private int wordIndex;
        private int permutationIndex;
        private String currentWord;
        private String[] currentWordPermutations;

        public WordPermutationIterator(String[] wordList) {
            this.wordList = wordList;
            this.wordIndex = 0;
            this.permutationIndex = 0;
            this.currentWord = wordList[0];
            this.initializeCurrPermutations();
        }

        @Override
        public boolean hasNext() {
            // Before the last word
            if (wordIndex < wordList.length - 1) {
                return true;
            }
            // Special case: last word, check index
            if (wordIndex == wordList.length) {
                return permutationIndex < currentWordPermutations.length;
            }
            return false;
        }

        @Override
        public String next() {
            if (permutationIndex == currentWordPermutations.length) {
                nextWord();
                initializeCurrPermutations();
            }
            return currentWordPermutations[permutationIndex++];
        }

        private void initializeCurrPermutations() {
            int maxPermutations = 1 << currentWord.length();
            List<String> permutations = new ArrayList<>();
            for (int i = 0; i < maxPermutations; i++) {
                permutations.add(generatePermutation(currentWord, i));
            }
            currentWordPermutations = permutations.toArray(new String[0]);
            permutationIndex = 0;
        }

        private void nextWord() {
            if (hasNext()) {
                wordIndex++;
                currentWord = wordList[wordIndex];
            }
        }

        private static String generatePermutation(String word, int permutationIndex) {
            final int len = word.length();
            final char[] chars = new char[len];

            for (int i = 0; i < len; i++) {
                // All characters are lowercase by default
                char c = word.charAt(i);
                // Bit magic for permutations (max permutations is 2^length)
                // Each number in that range represents a permutation
                // E.g. for "cat" max permutations is 2^3 = 8
                // In binary, 000, 001, 010, 011... 111
                //            cat, caT, cAt, cAT... CAT
                // All permutations, 0 is lowercase, 1 is uppercase
                chars[i] = ((permutationIndex >> i) & 1) == 1 ?
                        (char) (c - 32) : // Lowercase -> uppercase via ASCII
                        c; // Leave as lowercase
            }

            return new String(chars);
        }
    }
}