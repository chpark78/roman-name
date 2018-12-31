package com.kakaobank.bigdata.romanname.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class SyllableParser {

  public Collection<Character> parse(Collection<String> data) {
    Set<Character> syllables = new HashSet<>();
    for (String datum : data) {
      String[] fields = datum.split("\\|");
      if (fields.length < 2) {
        continue;
      }

      String type = fields[0].trim();
      if (!type.equals("HANGLE")) {
        continue;
      }

      String syllable = fields[1].trim();
      for (int i = 0; i < syllable.length(); i++) {
        syllables.add(syllable.charAt(i));
      }
    }
    return syllables;
  }

}
