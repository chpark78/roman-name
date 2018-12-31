package com.kakaobank.bigdata.romanname.dpfast;

import com.kakaobank.bigdata.romanname.HangleRomanMatchingStrategy;
import com.kakaobank.bigdata.romanname.MatchedEntry;
import com.kakaobank.bigdata.romanname.data.JasoRomanDictionary;
import com.kakaobank.bigdata.romanname.data.SyllableParser;
import com.kakaobank.bigdata.romanname.data.SyllableRomanParser;

import java.util.*;

public class DpfastMatchingStrategy implements HangleRomanMatchingStrategy {

  private volatile SyllableRomanSequenceMap syllableRomanSequenceMap;

  public DpfastMatchingStrategy() {
    this(new SyllableRomanSequenceMap(new SyllableRomanDictionary(new JasoRomanDictionary())));
  }

  public DpfastMatchingStrategy(SyllableRomanSequenceMap syllableRomanSequenceMap) {
    this.syllableRomanSequenceMap = syllableRomanSequenceMap;
  }

  @Override
  public void dataReloaded(Collection<String> data) {
    JasoRomanDictionary jasoRomanDictionary = new JasoRomanDictionary().load(data);
    SyllableRomanDictionary syllableRomanDictionary = new SyllableRomanDictionary(jasoRomanDictionary);
    SyllableRomanSequenceMap syllableRomanSequenceMap = new SyllableRomanSequenceMap(syllableRomanDictionary);

    Map<Character, Set<String>> syllableRomans = new SyllableRomanParser().parse(data);
    for (Map.Entry<Character, Set<String>> syllableRomansEntry : syllableRomans.entrySet()) {
      Character syllable = syllableRomansEntry.getKey();
      Set<String> romans = syllableRomansEntry.getValue();
      for (String roman : romans) {
        syllableRomanSequenceMap.add(syllable, roman);
      }
    }

    Collection<Character> syllables = new SyllableParser().parse(data);
    syllableRomanSequenceMap.load(syllables);

    this.syllableRomanSequenceMap = syllableRomanSequenceMap;
  }

  @Override
  public List<MatchedEntry> match(String hangleName, String romanName) {
    List<MatchedEntry> matchedEntries = new ArrayList<>(hangleName.length());

    List<CharSequenceFinder> finders = new ArrayList<>(hangleName.length());
    int lastFinerIndex = hangleName.length() - 1;
    for (int i = 0; i <= lastFinerIndex; i++) {
      char hangleChar = hangleName.charAt(i);
      LinkedCharSequence romanSequence = syllableRomanSequenceMap.get(hangleChar);
      finders.add(new CharSequenceFinder(romanSequence, romanName));
      matchedEntries.add(new MatchedEntry(String.valueOf(hangleChar), "", false));
    }

    int romanNameIndex = 0;
    int romanNameLength = romanName.length();
    boolean rollback = false;
    for (int hangleNameIndex = 0; hangleNameIndex < finders.size(); hangleNameIndex++) {
      CharSequenceFinder finder = finders.get(hangleNameIndex);

      if (!rollback) {
        finder.find(romanNameIndex);
      }

      if (hangleNameIndex == lastFinerIndex) {
        while (finder.hasNext()) {
          romanNameIndex = finder.next() + 1;
          if (romanNameIndex == romanNameLength) {
            matchedEntries.set(hangleNameIndex,
                               newMatchedEntry(finder, hangleName, hangleNameIndex, romanName, romanNameIndex));
            return matchedEntries;
          }
        }
      } else if (finder.hasNext()) {
        romanNameIndex = finder.next() + 1;
        matchedEntries.set(hangleNameIndex,
                           newMatchedEntry(finder, hangleName, hangleNameIndex, romanName, romanNameIndex));
        rollback = false;
        continue;
      } else if (hangleNameIndex == 0) {
        return matchedEntries;
      }

      rollback = true;
      hangleNameIndex -= 2;
    }

    return matchedEntries;
  }

  private MatchedEntry newMatchedEntry(CharSequenceFinder finder,
                                       String hangleName,
                                       int hangleNameIndex,
                                       String romanName,
                                       int romanNameIndex) {
    String hangle = String.valueOf(hangleName.charAt(hangleNameIndex));
    String roman = romanName.substring(finder.getBeginIndex(), romanNameIndex).trim();
    return new MatchedEntry(hangle, roman, true);
  }

}
