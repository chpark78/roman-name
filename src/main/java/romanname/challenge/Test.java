package romanname.challenge;

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
    Map<Character, LinkedCharSequence> dictionary = new SyllableRomanSequenceBuilder().build(syllableRomanPairs);

    KoreanRomanMatcher2 matcher = new KoreanRomanMatcher2(dictionary);

    // KoreanRomanMatcher matcher = new KoreanRomanMatcher(dictionary);

    List<String> testDataLines = Files.readAllLines(Paths.get(classLoader.getResource("hangle_roman_testset.tsv").toURI()));
    for (String testDataLine : testDataLines) {
      System.out.println(testDataLine);

      long startTimeMillis = System.currentTimeMillis();

      String[] testData = testDataLine.split("\t");
      String hangleName = testData[0];
      String romanLastName = testData[1];
      String romanFirstName = testData[2];
      boolean expected = testData[3].equals("O");

      boolean actual = matcher.matches(hangleName, romanLastName, romanFirstName);

      long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
      if (expected == actual) {
        // System.out.println("Expected: " + expected + ", Actual: " + actual + ", Latency: " + elapsedTimeMillis + " ms, " + testDataLine);
      } else {
        System.err.println("Expected: " + expected + ", Actual: " + actual + ", Latency: " + elapsedTimeMillis + " ms, " + testDataLine);
      }
    }
  }

}
