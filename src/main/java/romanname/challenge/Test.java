package romanname.challenge;

import romanname.MatchedResult;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

public class Test {

  public static void main(String[] args) throws Exception {
    ClassLoader classLoader = SyllableRomanSequenceBuilder.class.getClassLoader();
    URI syllableFileUri = classLoader.getResource("syllables.dat").toURI();
    Path syllableFilePath = Paths.get(syllableFileUri);
    URI syllableRomanFileUri = classLoader.getResource("hangle_roman_dict.txt").toURI();
    Path syllableRomanFilePath = Paths.get(syllableRomanFileUri);

    List<String> syllableRomanPairs = new SyllableRomanBuilder().build(syllableRomanFilePath, syllableFilePath);
    Files.write(Paths.get(classLoader.getResource("syllable-roman.map").toURI()), syllableRomanPairs, StandardOpenOption.TRUNCATE_EXISTING);
    Map<String, String> nameDictionary = new NameDictionaryBuilder().build(syllableRomanFilePath);
    Map<Character, LinkedCharSequence> syllableRomanSequenceMap = new SyllableRomanSequenceBuilder().build(syllableRomanPairs);

    GazuaHangleRomanMatcher matcher = new GazuaHangleRomanMatcher(nameDictionary, syllableRomanSequenceMap);

    List<String> testDataLines = Files.readAllLines(Paths.get(classLoader.getResource("hangle_roman_testset.tsv").toURI()));
    for (int i = 0; i < testDataLines.size(); i++) {
      String testDataLine = testDataLines.get(i);

      long startTimeMillis = System.currentTimeMillis();

      String[] testData = testDataLine.split("\t");
      String hangleName = testData[0];
      String romanLastName = testData[1];
      String romanFirstName = testData[2];
      boolean expected = testData[3].equals("O");

      MatchedResult actual = matcher.matching(hangleName, romanFirstName, romanLastName);
      System.out.println(actual);

      long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
      if (expected == actual.isMatched()) {
        // System.out.println("Expected: " + expected + ", Actual: " + actual + ", Latency: " + elapsedTimeMillis + " ms, " + testDataLine);
      } else {
        System.err.println(i + " Expected: " + expected + ", Actual: " + actual + ", Latency: " + elapsedTimeMillis + " ms, " + testDataLine);
      }
    }
  }

}
