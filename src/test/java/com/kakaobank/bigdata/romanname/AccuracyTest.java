package com.kakaobank.bigdata.romanname;

public class AccuracyTest {

  public static void main(String[] args) throws Exception {
    // Path syllableFilePath = Paths.get("data/syllables.txt");
    // Path dictionaryFilePath = Paths.get("data/hangle_roman_dict.txt");
    //
    // Collection<String> syllables = new SyllableFileLoader().load(syllableFilePath);
    // List<String> syllableRomanPairs = new SyllableRomanPairBuilder().build(syllables, dictionaryFilePath);
    // Map<String, Set<String>> nameDictionary = new DictionaryBuilder().build(dictionaryFilePath);
    // Map<Character, LinkedCharSequence> syllableRomanSequenceMap = new SyllableRomanSequenceBuilder().build(syllableRomanPairs);

    // --------------------------------------------------------------------------------------------------------------------------------------

    // HangleRomanMatcher matcher = new DpfastMatchingStrategy(nameDictionary, syllableRomanSequenceMap);
    //
    // List<String> testDataLines = Files.readAllLines(Paths.get("data/testset.txt"));
    // for (int i = 0; i < testDataLines.size(); i++) {
    //   String testDataLine = testDataLines.get(i);
    //
    //   long startTimeMillis = System.currentTimeMillis();
    //
    //   String[] testData = testDataLine.split("\t");
    //   String hangleName = testData[0];
    //   String romanLastName = testData[1];
    //   String romanFirstName = testData[2];
    //   boolean expected = testData[3].equals("O");
    //
    //   MatchedResult actual;
    //   try {
    //     actual = matcher.matching(hangleName, romanFirstName, romanLastName);
    //   } catch (Exception e) {
    //     System.err.println(i + ": " + testDataLine);
    //     e.printStackTrace();
    //     continue;
    //   }
    //
    //   long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
    //   if (expected == actual.isMatched()) {
    ////     System.out.println("Expected: " + expected + ", Actual: " + actual.isMatched() + ", Latency: " + elapsedTimeMillis + " ms, " + testDataLine);
    ////     System.out.println(actual);
    // } else {
    //   System.err.println(i + " Expected: " + expected + ", Actual: " + actual.isMatched() + ", Latency: " + elapsedTimeMillis + " ms, " + testDataLine);
    //   System.err.println(actual);
    // }
    // }
  }

}
