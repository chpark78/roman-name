package com.kakaobank.bigdata.romanname.util;

import java.util.*;

public class TextUtils {

  // ㄱ      ㄲ      ㄴ      ㄷ      ㄸ      ㄹ      ㅁ      ㅂ      ㅃ      ㅅ      ㅆ      ㅇ      ㅈ      ㅉ      ㅊ      ㅋ      ㅌ      ㅍ      ㅎ
  private final static char[] CHO_SUNG = {0x3131, 0x3132, 0x3134, 0x3137, 0x3138, 0x3139, 0x3141, 0x3142, 0x3143, 0x3145, 0x3146, 0x3147, 0x3148, 0x3149, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e};

  // ㅏ      ㅐ      ㅑ      ㅒ      ㅓ      ㅔ      ㅕ      ㅖ      ㅗ      ㅘ      ㅙ      ㅚ      ㅛ      ㅜ      ㅝ      ㅞ      ㅟ      ㅠ      ㅡ      ㅢ      ㅣ
  private final static char[] JUNG_SUNG = {0x314f, 0x3150, 0x3151, 0x3152, 0x3153, 0x3154, 0x3155, 0x3156, 0x3157, 0x3158, 0x3159, 0x315a, 0x315b, 0x315c, 0x315d, 0x315e, 0x315f, 0x3160, 0x3161, 0x3162, 0x3163};

  //         ㄱ      ㄲ      ㄳ      ㄴ      ㄵ      ㄶ      ㄷ      ㄹ      ㄺ      ㄻ      ㄼ      ㄽ      ㄾ      ㄿ      ㅀ      ㅁ      ㅂ      ㅄ      ㅅ      ㅆ      ㅇ      ㅈ      ㅊ      ㅋ      ㅌ      ㅍ      ㅎ
  private final static char[] JONG_SUNG = {0, 0x3131, 0x3132, 0x3133, 0x3134, 0x3135, 0x3136, 0x3137, 0x3139, 0x313a, 0x313b, 0x313c, 0x313d, 0x313e, 0x313f, 0x3140, 0x3141, 0x3142, 0x3144, 0x3145, 0x3146, 0x3147, 0x3148, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e};

  public static List<String> hangleToJaso(String s) {
    if (s.isEmpty()) {
      return Collections.emptyList();
    }

    char ch = s.charAt(0);
    if (ch >= 0xAC00 && ch <= 0xD7A3) {
      int a, b, c;
      c = ch - 0xAC00;
      a = c / (21 * 28);
      c = c % (21 * 28);
      b = c / 28;
      c = c % 28;

      String chosung = String.valueOf(CHO_SUNG[a]);
      String jungsung = String.valueOf(JUNG_SUNG[b]);
      String jongsung = (c != 0) ? String.valueOf(JONG_SUNG[c]) : "";

      return Arrays.asList(chosung, jungsung, jongsung);
    }

    return Collections.emptyList();
  }

  public static List<String> romanToJaso(String roman) {
    String chosung = roman;
    String jungsung = "";
    String jongsung = "";

    for (int chosungIndex = 0; chosungIndex < roman.length(); chosungIndex++) {
      char chosungAlpha = roman.charAt(chosungIndex);
      if (!isVowels(chosungAlpha)) {
        continue;
      }

      if (chosungIndex == 0 && roman.length() > 1 && roman.substring(0, 2).equals("WH")) {
        continue;
      }

      chosung = roman.substring(0, chosungIndex);
      for (int jungsungIndex = chosungIndex + 1; jungsungIndex < roman.length(); jungsungIndex++) {
        char jungsungAlpha = roman.charAt(jungsungIndex);
        if (isVowels(jungsungAlpha)) {
          continue;
        }

        if (jungsungAlpha == 'R' || jungsungAlpha == 'H') {
          continue;
        }

        jungsung = roman.substring(chosungIndex, jungsungIndex);
        jongsung = roman.substring(jungsungIndex);
        break;
      }

      if (jungsung.equals("")) {
        jungsung = roman.substring(chosungIndex);
      }
      break;
    }

    // for GLE, SLE..
    int lastChosungIndex = chosung.length() - 1;
    if (jungsung.equals("E") && jongsung.equals("") && chosung.length() > 1 && chosung.charAt(lastChosungIndex) == 'L') {
      chosung = chosung.substring(0, lastChosungIndex);
      jungsung = "E";
      jongsung = "L";
    }

    return Arrays.asList(chosung, jungsung, jongsung);
  }

  public static boolean isVowels(char c) {
    switch (c) {
      case 'A':
      case 'E':
      case 'I':
      case 'O':
      case 'U':
      case 'W':
      case 'Y':
        return true;
      default:
        return false;
    }
  }

  public static List<String> splitSyllables(String s, int maxCount) {
    int currentCount = s.split(romanSplitter()).length;
    List<String> splittedSyllables = splitSyllables(s, 0, currentCount, maxCount);
    if (splittedSyllables.isEmpty()) {
      splittedSyllables.add(s);
    }

    return splittedSyllables;
  }

  private static List<String> splitSyllables(String s, int start, int currentCount, int maxCount) {
    if (currentCount >= maxCount) {
      return Collections.singletonList(s);
    }

    List<String> results = new ArrayList<>();

    for (int i = start + 1; i < s.length(); i++) {
      if (s.charAt(i) == ' ') {
        continue;
      }

      if (s.charAt(i - 1) == ' ') {
        continue;
      }

      String prefix = s.substring(0, i);
      String suffix = s.substring(i);

      results.addAll(splitSyllables(prefix + " " + suffix, i + 1, currentCount + 1, maxCount));
    }

    return results;
  }

  public static String normalize(String s) {
    return s.replaceAll("\\s+", " ").trim();
  }

  public static String hangleSplitter() {
    return "";
  }

  public static String romanSplitter() {
    return " ";
  }

  public static String spacing(String s, Collection<Integer> indexes) {
    StringBuilder result = new StringBuilder();
    int startIndex = 0;
    for (Integer lastIndex : indexes) {
      result.append(s, startIndex, lastIndex).append(" ");
      startIndex = lastIndex;
    }
    result.append(s.substring(startIndex));

    return result.toString();
  }

}
