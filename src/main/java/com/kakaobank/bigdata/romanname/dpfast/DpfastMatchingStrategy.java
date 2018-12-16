package com.kakaobank.bigdata.romanname.dpfast;

import com.kakaobank.bigdata.romanname.HangleRomanMatchingStrategy;
import com.kakaobank.bigdata.romanname.MatchedEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DpfastMatchingStrategy implements HangleRomanMatchingStrategy {

  private volatile Map<Character, LinkedCharSequence> romanSequenceForSyllable;

  public DpfastMatchingStrategy(Map<Character, LinkedCharSequence> romanSequenceForSyllable) {
    this.romanSequenceForSyllable = romanSequenceForSyllable;
  }

  @Override
  public void dataReloaded(Collection<String> data) {
    Collection<String> syllables = new SyllableBuilder().build(data);
    List<String> syllableRomanPairs = new SyllableRomanPairBuilder().build(syllables, data);
    this.romanSequenceForSyllable = new SyllableRomanSequenceBuilder().build(syllableRomanPairs);
  }

  @Override
  public List<MatchedEntry> match(String hangleName, String romanName, boolean lastName) {
    List<MatchedEntry> matchedEntries = new ArrayList<>(hangleName.length());
    findMatches(hangleName, romanName, matchedEntries);
    return matchedEntries;
  }

  private boolean findMatches(String hangleName, String romanName, List<MatchedEntry> matchedEntries) {
    List<CharSequenceFinder> finders = new ArrayList<>(hangleName.length());
    int lastHandleNameIndex = hangleName.length() - 1;
    int matchedEntriesBeginIndex = matchedEntries.size();
    for (int handleNameIndex = 0; handleNameIndex <= lastHandleNameIndex; handleNameIndex++) {
      char hangleChar = hangleName.charAt(handleNameIndex);
      LinkedCharSequence romanSequence = romanSequenceForSyllable.get(hangleChar);
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

      if (hangleNameIndex == lastHandleNameIndex) {
        while (finder.hasNext()) {
          romanNameIndex = finder.next() + 1;
          if (romanNameIndex == romanNameLength) {
            matchedEntries.set(matchedEntriesBeginIndex + hangleNameIndex,
                               newMatchedEntry(finder, hangleName, hangleNameIndex, romanName, romanNameIndex));
            return true;
          }
        }
      } else if (finder.hasNext()) {
        romanNameIndex = finder.next() + 1;
        matchedEntries.set(matchedEntriesBeginIndex + hangleNameIndex,
                           newMatchedEntry(finder, hangleName, hangleNameIndex, romanName, romanNameIndex));
        rollback = false;
        continue;
      } else if (hangleNameIndex == 0) {
        return false;
      }

      rollback = true;
      hangleNameIndex -= 2;
    }

    return false;
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
