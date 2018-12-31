package com.kakaobank.bigdata.romanname.dpfast;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SyllableRomanSequenceMap {

  private final SyllableRomanDictionary syllableRomanDictionary;

  private final ConcurrentMap<Character, LinkedCharSequence> romanSequenceForSyllable = new ConcurrentHashMap<>();

  public SyllableRomanSequenceMap(SyllableRomanDictionary syllableRomanDictionary) {
    this.syllableRomanDictionary = syllableRomanDictionary;
  }

  public LinkedCharSequence get(char syllable) {
    LinkedCharSequence sequence = romanSequenceForSyllable.get(syllable);
    if (sequence == null) {
      List<String> romans = syllableRomanDictionary.getRomansForSyllable(syllable);
      if (!romans.isEmpty()) {
        for (String roman : romans) {
          add(syllable, roman);
        }
        sequence = romanSequenceForSyllable.get(syllable);
      }
    }
    return sequence;
  }

  public void add(Character syllable, String roman) {
    LinkedCharSequence currSeq = romanSequenceForSyllable.computeIfAbsent(syllable, k -> new LinkedCharSequence(syllable));

    for (int i = 0; i < roman.length(); i++) {
      char romanChar = roman.charAt(i);
      LinkedCharSequence nextSeq = currSeq.nextStartWith(romanChar);
      if (nextSeq == null) {
        nextSeq = new LinkedCharSequence(romanChar);
        currSeq.link(nextSeq);
      }
      currSeq = nextSeq;
    }

    if (currSeq.nextStartWith(' ') == null) {
      currSeq.link(new LinkedCharSequence(' '));
    }
  }

  public SyllableRomanSequenceMap load(Collection<Character> syllables) {
    for (Character syllable : syllables) {
      List<String> romans = syllableRomanDictionary.getRomansForSyllable(syllable);
      for (String roman : romans) {
        add(syllable, roman);
        add(roman.charAt(0), roman);
      }
    }
    return this;
  }

}
