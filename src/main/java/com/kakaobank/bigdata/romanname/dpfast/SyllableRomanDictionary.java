package com.kakaobank.bigdata.romanname.dpfast;

import com.kakaobank.bigdata.romanname.data.JasoRomanDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.kakaobank.bigdata.romanname.util.TextUtils.hangleToJaso;
import static java.util.Collections.emptyList;

public class SyllableRomanDictionary {

  private final JasoRomanDictionary jasoRomanDictionary;

  public SyllableRomanDictionary(JasoRomanDictionary jasoRomanDictionary) {
    this.jasoRomanDictionary = jasoRomanDictionary;
  }

  public List<String> getRomansForSyllable(char syllable) {
    List<String> jasos = hangleToJaso(syllable);
    if (jasos.isEmpty()) {
      System.err.println("자소 변환 불가: " + syllable);
      return emptyList();
    }

    String hangleChosung = jasos.get(0);
    String hangleJungsung = jasos.get(1);
    String hangleJongsung = jasos.get(2);

    Set<String> chosungSet = jasoRomanDictionary.getRomansForChosung(hangleChosung);
    Set<String> jungsungSet = jasoRomanDictionary.getRomansForJungsung(hangleJungsung);
    Set<String> jongsungSet = jasoRomanDictionary.getRomansForJongsung(hangleJongsung);

    List<String> romans = new ArrayList<>();
    for (String romanChosung : chosungSet) {
      for (String romanJungsung : jungsungSet) {
        if (romanJungsung.endsWith("+")) {
          if (hangleJongsung.isEmpty()) {
            continue;
          }
          romanJungsung = romanJungsung.substring(0, romanJungsung.length() - 1);
        }

        if (jongsungSet == null) {
          System.err.println("종성 없음: " + syllable);
          continue;
        }

        if (jongsungSet.isEmpty()) {
          romans.add(romanChosung + romanJungsung);
        } else {
          for (String romanJongsung : jongsungSet) {
            romans.add(romanChosung + romanJungsung + romanJongsung);
          }
        }
      }
    }

    return romans;
  }

}
