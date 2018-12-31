package com.kakaobank.bigdata.romanname;

import com.kakaobank.bigdata.romanname.beamsearch.BeamSearchMatchingStrategy;
import com.kakaobank.bigdata.romanname.dictionary.DictionaryMatchingStrategy;
import com.kakaobank.bigdata.romanname.dpfast.DpfastMatchingStrategy;

import java.util.ArrayList;
import java.util.List;

import static com.kakaobank.bigdata.romanname.util.TextUtils.normalize;

public class DefaultHangleRomanMatcher implements HangleRomanMatcher {

  private final HangleRomanMatchingStrategy dictionaryStrategy;
  private final HangleRomanMatchingStrategy dpfastStrategy;
  private final HangleRomanMatchingStrategy beamSearchStrategy;

  public DefaultHangleRomanMatcher(Dictionary dictionary) throws Exception {
    dictionaryStrategy = new DictionaryMatchingStrategy();
    dpfastStrategy = new DpfastMatchingStrategy();
    beamSearchStrategy = new BeamSearchMatchingStrategy();

    dictionary.addListener(dictionaryStrategy);
    dictionary.addListener(dpfastStrategy);
    dictionary.addListener(beamSearchStrategy);

    dictionary.reload();
  }

  @Override
  public MatchedResult matching(String hangleName, String romanFirstName, String romanLastName) {
    String romanName = normalize(romanFirstName) + " " + normalize(romanLastName);

    List<MatchedEntry> matchedEntries = new ArrayList<>(hangleName.length());
    List<MatchedEntry> dictionaryResults = dictionaryStrategy.match(hangleName, romanName);
    for (MatchedEntry dictionaryResult : dictionaryResults) {
      if (dictionaryResult.isMatched()) {
        matchedEntries.add(dictionaryResult);
      } else {
        List<MatchedEntry> dpfastResults = dpfastStrategy.match(dictionaryResult.getHangle(), dictionaryResult.getRoman());
        for (MatchedEntry dpfastResult : dpfastResults) {
          if (dpfastResult.isMatched()) {
            matchedEntries.add(dpfastResult);
          } else {
            List<MatchedEntry> beamSearchResults = beamSearchStrategy.match(dpfastResult.getHangle(), dpfastResult.getRoman());
            matchedEntries.addAll(beamSearchResults);
          }
        }
      }
    }

    boolean matched = matchedEntries.stream()
                                    .map(MatchedEntry::isMatched)
                                    .reduce((x, y) -> x && y)
                                    .orElse(false);

    return new MatchedResult(matched, matchedEntries);
  }

}
