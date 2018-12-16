package com.kakaobank.bigdata.romanname.dictionary;

import com.kakaobank.bigdata.romanname.HangleRomanMatchingStrategy;
import com.kakaobank.bigdata.romanname.MatchedEntry;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.singletonList;

public class DictionaryMatchingStrategy implements HangleRomanMatchingStrategy {

  private volatile Map<String, Set<String>> dictionary;

  public DictionaryMatchingStrategy(Map<String, Set<String>> dictionary) {
    this.dictionary = dictionary;
  }

  @Override
  public void dataReloaded(Collection<String> data) {
    this.dictionary = new DictionaryBuilder().build(data);
  }

  @Override
  public List<MatchedEntry> match(String hangleName, String romanName, boolean lastName) {
    Set<String> names = dictionary.get(romanName);
    if (names == null && lastName) {
      names = dictionary.get("^" + romanName);
    }

    if (names != null) {
      for (String name : names) {
        if (lastName ? hangleName.startsWith(name) : hangleName.endsWith(name)) {
          return singletonList(new MatchedEntry(name, romanName, true));
        }
      }
    }

    return singletonList(new MatchedEntry(hangleName, romanName, false));
  }

}
