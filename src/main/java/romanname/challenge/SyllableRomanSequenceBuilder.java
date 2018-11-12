package romanname.challenge;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyllableRomanSequenceBuilder {

  public Map<Character, LinkedCharSequence> build(Path syllableRomanFilePath) throws IOException {
    List<String> syllableRomanPairs = Files.readAllLines(syllableRomanFilePath);
    return build(syllableRomanPairs);
  }

  public Map<Character, LinkedCharSequence> build(List<String> syllableRomanPairs) {
    long startTimeMillis = System.currentTimeMillis();
    Map<Character, LinkedCharSequence> dictionary = new HashMap<>();
    for (String line : syllableRomanPairs) {
      if (line.trim().isEmpty()) {
        continue;
      }

      String[] parts = line.split(" ");
      Character syllable = parts[0].charAt(0);
      String roman = parts.length < 2 ? "" : parts[1];

      LinkedCharSequence node = dictionary.computeIfAbsent(syllable, k -> new LinkedCharSequence(syllable));
      for (int i = 0; i < roman.length(); i++) {
        char child = roman.charAt(i);
        LinkedCharSequence childNode = node.nextStartWith(child);
        if (childNode == null) {
          childNode = new LinkedCharSequence(child);
          node.link(childNode);
        }
        node = childNode;
      }
      if (node.nextStartWith(' ') == null) {
        node.link(new LinkedCharSequence(' '));
      }
    }

    long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
    System.out.println("Build dictionary: " + elapsedTimeMillis + " ms");
    return dictionary;
  }

}
