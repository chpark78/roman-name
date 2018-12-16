package com.kakaobank.bigdata.romanname.dpfast;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SyllableRomanSequenceBuilder {

  public Map<Character, LinkedCharSequence> build(Collection<String> syllableRomanPairs) {
    Map<Character, LinkedCharSequence> syllableRomanSequences = new HashMap<>();
    for (String line : syllableRomanPairs) {
      if (line.trim().isEmpty()) {
        continue;
      }

      String[] parts = line.split(" ");
      Character syllable = parts[0].charAt(0);
      String roman = parts.length < 2 ? "" : parts[1];

      LinkedCharSequence node = syllableRomanSequences.computeIfAbsent(syllable, k -> new LinkedCharSequence(syllable));
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
    return syllableRomanSequences;
  }

}
