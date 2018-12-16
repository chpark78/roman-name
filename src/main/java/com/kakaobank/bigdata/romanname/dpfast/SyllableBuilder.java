package com.kakaobank.bigdata.romanname.dpfast;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class SyllableBuilder {

  public Collection<String> build(Collection<String> data) {
    Set<String> syllables = new TreeSet<>();
    for (String datum : data) {
      String[] fields = datum.split("\\|");
      if (fields.length < 2) {
        continue;
      }

      String type = fields[0];
      if (!type.equals("HANGLE")) {
        continue;
      }

      String syllable = fields[1];
      for (int i = 0; i < syllable.length(); i++) {
        syllables.add(String.valueOf(syllable.charAt(i)));
      }
    }
    return syllables;
  }

}
