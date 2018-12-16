package com.kakaobank.bigdata.romanname.dpfast;

import com.kakaobank.bigdata.romanname.util.TextUtils;

import java.util.*;

public class SyllableRomanPairBuilder {

  public List<String> build(Collection<String> syllables, Collection<String> data) {
    List<String> syllableRomanPairs = new ArrayList<>();

    Map<String, Set<String>> chosungMap = new HashMap<>();
    Map<String, Set<String>> jungsungMap = new HashMap<>();
    Map<String, Set<String>> jongsungMap = new HashMap<>();

    for (String datum : data) {
      String[] fields = datum.split("\\|");
      String type = fields[0];
      String syllable = fields.length > 1 ? fields[1] : "";
      String roman = fields.length > 2 ? fields[2] : "";
      Set<String> romans;
      switch (type) {
        case "CHOSUNG":
          romans = chosungMap.computeIfAbsent(syllable, k -> new HashSet<>());
          break;
        case "JUNGSUNG":
          romans = jungsungMap.computeIfAbsent(syllable, k -> new HashSet<>());
          break;
        case "JONGSUNG":
          romans = jongsungMap.computeIfAbsent(syllable, k -> new HashSet<>());
          break;
        case "SYLLABLE":
          syllableRomanPairs.add(syllable + " " + roman);
          continue;
        default:
          continue;
      }
      romans.add(roman);
    }

    for (String hangle : syllables) {
      List<String> jasos = TextUtils.hangleToJaso(hangle);
      if (jasos.isEmpty()) {
        System.err.println("자소 변환 불가: " + hangle);
        continue;
      }

      String hangleChosung = jasos.get(0);
      String hangleJungsung = jasos.get(1);
      String hangleJongsung = jasos.get(2);

      Set<String> chosungSet = chosungMap.get(hangleChosung);
      Set<String> jungsungSet = jungsungMap.get(hangleJungsung);
      Set<String> jongsungSet = jongsungMap.get(hangleJongsung);

      for (String romanChosung : chosungSet) {
        for (String romanJungsung : jungsungSet) {
          if (romanJungsung.endsWith("+")) {
            if (hangleJongsung.isEmpty()) {
              continue;
            }
            romanJungsung = romanJungsung.substring(0, romanJungsung.length() - 1);
          }

          if (jongsungSet == null) {
            System.err.println("종성 없음: " + hangle);
            continue;
          }

          if (jongsungSet.isEmpty()) {
            String pair = hangle + " " + romanChosung + romanJungsung;
            syllableRomanPairs.add(pair);
          } else {
            for (String romanJongsung : jongsungSet) {
              String pair = hangle + " " + romanChosung + romanJungsung + romanJongsung;
              syllableRomanPairs.add(pair);
            }
          }
        }
      }
    }

    return syllableRomanPairs;
  }

}
