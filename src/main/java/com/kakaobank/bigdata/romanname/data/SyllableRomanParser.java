package com.kakaobank.bigdata.romanname.data;

import java.util.*;

public class SyllableRomanParser {

  public Map<Character, Set<String>> parse(Collection<String> data) {
    Map<Character, Set<String>> result = new HashMap<>();

    for (String datum : data) {
      String[] fields = datum.split("\\|");
      if (fields.length < 3) {
        continue;
      }

      String type = fields[0].trim();
      if (!type.equals("SYLLABLE")) {
        continue;
      }

      String hangle = fields[1].trim();
      if (hangle.isEmpty()) {
        continue;
      }

      String roman = fields[2].trim();

      Set<String> romans = result.computeIfAbsent(hangle.charAt(0), k -> new HashSet<>());
      romans.add(roman);
    }

    return result;
  }

}
