package romanname.challenge;

import romanname.*;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

public class Test2 {

  public static void main(String[] args) throws Exception {
    ClassLoader classLoader = SyllableRomanSequenceBuilder.class.getClassLoader();
    URI syllableRomanFileUri = classLoader.getResource("hangle_roman_dict.txt").toURI();
    Path syllableRomanFilePath = Paths.get(syllableRomanFileUri);

    Dictionary dictionary = new DefaultDictionary(new FileDictionaryLoader(syllableRomanFilePath));
    HangleRomanMatcher matcher = new DefaultHangleRomanMatcher(dictionary);

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

      long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
      if (actual.isMatched()) {
        // System.out.println("Expected: " + expected + ", Actual: " + actual + ", Latency: " + elapsedTimeMillis + " ms, " + testDataLine);
      } else {
        System.err.println(actual);
        System.err.println(i + " Expected: " + expected + ", Actual: " + actual.isMatched() + ", Latency: " + elapsedTimeMillis + " ms, " + testDataLine);
      }
    }
  }

}
