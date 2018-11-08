package romanname.challenge;

import romanname.TextUtils;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SyllableRomanBuilder {

  public List<String> build(Path syllableRomanFilePath, Path syllableFilePath) throws Exception {
    Map<String, Set<String>> chosungMap = new HashMap<>();
    Map<String, Set<String>> jungsungMap = new HashMap<>();
    Map<String, Set<String>> jongsungMap = new HashMap<>();

    List<String> syllableRomanFileLines = Files.readAllLines(syllableRomanFilePath);
    for (String line : syllableRomanFileLines) {
      String[] lineParts = line.split("\\|");
      String type = lineParts[0];
      String syllable = lineParts.length > 1 ? lineParts[1] : "";
      String roman = lineParts.length > 2 ? lineParts[2] : "";
      Set<String> romans;
      switch (type) {
        case "CHOSUNG":
          romans = chosungMap.computeIfAbsent(syllable, k -> new HashSet<>());
          break;
        case "JUNGSUNG":
          romans = jungsungMap.computeIfAbsent(syllable, k -> new HashSet<>());
          break;
        case "JONGSUNG":
          romans = jongsungMap.computeIfAbsent(syllable, k -> new HashSet<>());
          break;
        default:
          continue;
      }

      romans.add(roman);
    }

    List<String> syllables = Files.readAllLines(syllableFilePath);

    List<String> syllableRomanPairs = new ArrayList<>();
    for (String hangle : syllables) {
      List<String> jasos = TextUtils.hangleToJaso(hangle);

      String hangleChosung = jasos.get(0);
      String hangleJungsung = jasos.get(1);
      String hangleJongsung = jasos.get(2);

      Set<String> chosungSet = chosungMap.get(hangleChosung);
      Set<String> jungsungSet = jungsungMap.get(hangleJungsung);
      Set<String> jongsungSet = jongsungMap.get(hangleJongsung);

      for (String romanChosung : chosungSet) {
        for (String romanJungsung : jungsungSet) {
          if (romanJungsung.endsWith("+")) {
            if (hangleJongsung.isEmpty()) {
              continue;
            }
            romanJungsung = romanJungsung.substring(0, romanJungsung.length() - 1);
          }

          if (jongsungSet.isEmpty()) {
            String pair = hangle + " " + romanChosung + romanJungsung;
            syllableRomanPairs.add(pair);
          } else {
            for (String romanJongsung : jongsungSet) {
              String pair = hangle + " " + romanChosung + romanJungsung + romanJongsung;
              syllableRomanPairs.add(pair);
            }
          }
        }
      }
    }

    return syllableRomanPairs;
  }

}
