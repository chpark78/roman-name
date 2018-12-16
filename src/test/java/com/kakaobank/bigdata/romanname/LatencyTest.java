package com.kakaobank.bigdata.romanname;

import com.kakaobank.bigdata.romanname.dpfast.*;
import com.kakaobank.bigdata.romanname.dictionary.DictionaryBuilder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LatencyTest {

  public static void main(String[] args) throws Exception {
    // Path syllableFilePath = Paths.get("data/syllables.txt");
    // Path dictionaryFilePath = Paths.get("data/hangle_roman_dict.txt");
    //
    // Collection<String> syllables = new SyllableFileLoader().load(syllableFilePath);
    // List<String> syllableRomanPairs = new SyllableRomanPairBuilder().build(syllables, dictionaryFilePath);
    // Map<String, Set<String>> nameDictionary = new DictionaryBuilder().build(dictionaryFilePath);
    // Map<Character, LinkedCharSequence> syllableRomanSequenceMap = new SyllableRomanSequenceBuilder().build(syllableRomanPairs);

    // --------------------------------------------------------------------------------------------------------------------------------------

    // HangleRomanMatcher matcher = new DpfastMatchingStrategy(nameDictionary, syllableRomanSequenceMap);
    //
    // long startTimeMillis = System.currentTimeMillis();
    // for (int i = 0; i < 10000; i++) {
    //   MatchedResult result = matcher.matching("김연우인미명란병호본권다애남근호준경남은리주양선화효림", "YOUNWOOINMIMYEONGRANBYUNGHOBONKWONDAAENAMKEUNHOJOONKYUNGNAMEONRYJOOYANGSUNHWAHYOLIM", "KIM");
    //   if (!result.isMatched()) {
    //     System.err.println("Failed!");
    //   }
    // }
    // long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
    // System.out.println(elapsedTimeMillis + " ms");
  }

}
