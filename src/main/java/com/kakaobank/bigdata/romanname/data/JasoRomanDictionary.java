package com.kakaobank.bigdata.romanname.data;

import java.util.*;

public class JasoRomanDictionary {

  private final Map<String, Set<String>> chosungMap = new HashMap<>();

  private final Map<String, Set<String>> jungsungMap = new HashMap<>();

  private final Map<String, Set<String>> jongsungMap = new HashMap<>();

  public Set<String> getRomansForChosung(String chosung) {
    return chosungMap.get(chosung);
  }

  public Set<String> getRomansForJungsung(String jungsung) {
    return jungsungMap.get(jungsung);
  }

  public Set<String> getRomansForJongsung(String jongsung) {
    return jongsungMap.get(jongsung);
  }

  public JasoRomanDictionary load(Collection<String> data) {
    for (String datum : data) {
      String[] fields = datum.split("\\|");
      String type = fields[0];
      String syllable = fields.length > 1 ? fields[1] : "";
      String roman = fields.length > 2 ? fields[2] : "";

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
    return this;
  }

}
