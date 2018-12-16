package com.kakaobank.bigdata.romanname.dictionary;

import java.util.*;

public class DictionaryBuilder {

  public Map<String, Set<String>> build(Collection<String> data) {
    Map<String, Set<String>> nameDictionary = new HashMap<>();
    for (String datum : data) {
      String[] fields = datum.split("\\|");
      String type = fields[0];
      String hangle = fields.length > 1 ? fields[1] : "";
      String roman = fields.length > 2 ? fields[2] : "";
      switch (type) {
        case "NAME":
          nameDictionary.computeIfAbsent(roman, key -> new HashSet<>()).add(hangle);
          break;
        case "SYLLABLE":
          if (roman.startsWith("^")) {
            nameDictionary.computeIfAbsent(roman, key -> new HashSet<>()).add(hangle);
          }
        default:
          break;
      }
    }
    return nameDictionary;
  }

}
