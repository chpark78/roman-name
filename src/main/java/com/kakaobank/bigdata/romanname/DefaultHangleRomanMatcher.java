package com.kakaobank.bigdata.romanname;

import com.kakaobank.bigdata.romanname.dictionary.DictionaryMatchingStrategy;
import com.kakaobank.bigdata.romanname.dpfast.DpfastMatchingStrategy;
import com.kakaobank.bigdata.romanname.util.TextUtils;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyMap;

public class DefaultHangleRomanMatcher implements HangleRomanMatcher {

  private final HangleRomanMatchingStrategy dictionaryStrategy;
  private final HangleRomanMatchingStrategy dpfastStrategy;

  public DefaultHangleRomanMatcher(Dictionary dictionary) throws Exception {
    dictionaryStrategy = new DictionaryMatchingStrategy(emptyMap());
    dpfastStrategy = new DpfastMatchingStrategy(emptyMap());

    dictionary.addListener(dictionaryStrategy);
    dictionary.addListener(dpfastStrategy);

    dictionary.reload();
  }

  @Override
  public MatchedResult matching(String hangleName, String firstName, String lastName) {
    String romanFirstName = TextUtils.normalize(firstName);
    String romanLastName = TextUtils.normalize(lastName);

    List<MatchedEntry> matchedEntries = new ArrayList<>(hangleName.length());

    MatchedEntry mismatchedLastName = match(hangleName, romanLastName, true, matchedEntries);
    hangleName = mismatchedLastName.getHangle();
    romanLastName = mismatchedLastName.getRoman();

    System.out.println("hangleName: " + hangleName);
    System.out.println("romanLastName: " + romanLastName);

    if (!romanLastName.isEmpty()) {
      matchedEntries.add(mismatchedLastName);
    }

    MatchedEntry mismatchedFirstName = match(hangleName, romanFirstName, false, matchedEntries);
    hangleName = mismatchedFirstName.getHangle();
    romanFirstName = mismatchedFirstName.getRoman();

    System.out.println("hangleName: " + hangleName);
    System.out.println("romanFirstName: " + romanFirstName);

    if (!romanFirstName.isEmpty()) {
      matchedEntries.add(mismatchedFirstName);
    }

    boolean matched = matchedEntries.stream()
                                    .map(MatchedEntry::isMatched)
                                    .reduce((x, y) -> x && y)
                                    .orElse(false);

    return new MatchedResult(matched, matchedEntries);
  }

  private MatchedEntry match(String hangleName, String romanName, boolean lastName, List<MatchedEntry> matchedEntries) {
    List<MatchedEntry> nameEntries = dictionaryStrategy.match(hangleName, romanName, lastName);
    if (!nameEntries.get(0).isMatched()) {
      nameEntries = dpfastStrategy.match(nameEntries.get(0).getHangle(), romanName, lastName);
    }

    for (MatchedEntry nameEntry : nameEntries) {
      if (nameEntry.isMatched()) {
        hangleName = hangleName.substring(nameEntry.getHangle().length()).trim();
        romanName = romanName.substring(nameEntry.getRoman().length()).trim();
        matchedEntries.add(nameEntry);
      }
    }

    return new MatchedEntry(hangleName, romanName, false);
  }

}
