package com.kakaobank.bigdata.romanname.dictionary;

import com.kakaobank.bigdata.romanname.HangleRomanMatchingStrategy;
import com.kakaobank.bigdata.romanname.MatchedEntry;

import java.util.*;

import static java.util.Collections.emptyMap;

public class DictionaryMatchingStrategy implements HangleRomanMatchingStrategy {

  private volatile Map<String, Set<String>> dictionary;

  public DictionaryMatchingStrategy() {
    this(emptyMap());
  }

  public DictionaryMatchingStrategy(Map<String, Set<String>> dictionary) {
    this.dictionary = dictionary;
  }

  @Override
  public void dataReloaded(Collection<String> data) {
    this.dictionary = new DictionaryBuilder().build(data);
  }

  @Override
  public List<MatchedEntry> match(String hangleName, String romanName) {
    String hangleLastName = null;
    String hangleFirstName = null;
    String[] romanNameParts = romanName.split(" ");
    String romanLastName = romanNameParts[0];
    String romanFirstName = romanNameParts[1];

    boolean lastNameMatched = false;
    Set<String> matchedLastNames = dictionary.get(romanLastName);
    if (matchedLastNames == null) {
      matchedLastNames = dictionary.get("^" + romanLastName);
    }
    if (matchedLastNames != null) {
      for (String matchedLastName : matchedLastNames) {
        if (hangleName.startsWith(matchedLastName)) {
          lastNameMatched = true;
          hangleLastName = matchedLastName;
          hangleFirstName = hangleName.substring(matchedLastName.length());
        }
      }
    }

    boolean firstNameMatched = false;
    Set<String> matchedFirstNames = dictionary.get(romanFirstName);
    if (matchedFirstNames != null) {
      for (String matchedFirstName : matchedFirstNames) {
        if (hangleName.endsWith(matchedFirstName)) {
          firstNameMatched = true;
          hangleLastName = hangleName.substring(0, hangleName.length() - matchedFirstName.length());
          hangleFirstName = matchedFirstName;
        }
      }
    }

    List<MatchedEntry> result = new ArrayList<>(2);
    if (lastNameMatched || firstNameMatched) {
      result.add(new MatchedEntry(hangleLastName, romanLastName, lastNameMatched));
      result.add(new MatchedEntry(hangleFirstName, romanFirstName, firstNameMatched));
    } else {
      result.add(new MatchedEntry(hangleName, romanName, false));
    }
    return result;
  }

}
