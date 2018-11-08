package romanname;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultHangleRomanMatcher implements HangleRomanMatcher {

  private static final String DICT_TYPE_NAME = "NAME";
  private static final String DICT_TYPE_SYLLABLE = "SYLLABLE";
  private static final String[] DICT_TYPE_JOSA_LIST = {"CHOSUNG", "JUNGSUNG", "JONGSUNG"};
  private static final int MATCHED_SCORE = 10;
  private final Dictionary dictionary;

  public DefaultHangleRomanMatcher(Dictionary dictionary) {
    this.dictionary = dictionary;
  }

  @Override
  public MatchedResult matching(String hangleName, String firstName, String lastName) {
    String romanFirstName = TextUtils.cleansing(firstName);
    String romanLastName = TextUtils.cleansing(lastName);
    MatchedResult matchedResult = matchingByName(hangleName, romanFirstName, romanLastName);
    if (matchedResult == null) {
      matchedResult = matchingBySyllable(hangleName, romanFirstName, romanLastName);
    }
    return matchedResult;
  }

  MatchedResult matchingByName(String hangleName, String firstName, String lastName) {
    for (int i = 1; i < hangleName.length(); i++) {
      String hangleLastName = hangleName.substring(i - 1, i);
      String hangleFirstName = hangleName.substring(i);
      if (dictionary.contains(generateKey(hangleFirstName, firstName, DICT_TYPE_NAME))) {
        MatchedResult matchedResult = matchingBySyllable(hangleLastName, "", lastName);
        List<MatchedEntry> matchedEntries = matchedResult.getMatchedEntries();
        matchedEntries.add(new MatchedEntry(hangleFirstName, firstName, true));
        return new MatchedResult(matchedResult.isMatched(), matchedEntries);
      }
    }
    return null;
  }

  MatchedResult matchingBySyllable(String hangleName, String firstName, String lastName) {
    String romanName = TextUtils.cleansing(lastName + TextUtils.romanSplitter() + firstName);
    String[] hangles = hangleName.split(TextUtils.hangleSplitter());
    int maxMatchedCount = -1;
    int maxMatchedScore = -1;
    List<MatchedEntry> maxMatchedEntries = null;
    for (String candidateRomanName : TextUtils.splitSyllables(romanName, hangles.length)) {
      String[] romans = candidateRomanName.split(TextUtils.romanSplitter());
      int matchedCount = 0;
      int matchedScore = 0;
      List<MatchedEntry> matchedEntries = new ArrayList<>(hangles.length);
      for (int index = 0; index < romans.length; index++) {
        String hangle = (index < hangles.length) ? hangles[index] : "";
        String roman = romans[index];
        int score = matchedScoreBySyllable(hangle, roman, (index == 0));
        matchedScore += score;
        boolean matched = (score == MATCHED_SCORE);
        matchedEntries.add(new MatchedEntry(hangle, roman, matched));
        if (matched) {
          matchedCount += 1;
        }
      }
      if (maxMatchedScore < matchedScore) {
        maxMatchedScore = matchedScore;
        maxMatchedEntries = matchedEntries;
        maxMatchedCount = matchedCount;
      }
    }
    boolean matched = (maxMatchedCount == hangles.length) && (maxMatchedCount == maxMatchedEntries.size());
    return new MatchedResult(matched, maxMatchedEntries);
  }

  int matchedScoreBySyllable(String hangle, String roman, boolean lastName) {
    if (hangle.length() != 1) {
      return 0;
    }
    if (dictionary.contains(generateKey(hangle, roman, DICT_TYPE_SYLLABLE))) {
      return MATCHED_SCORE;
    }
    List<String> hangleJaso = TextUtils.hangleToJaso(hangle);
    List<String> romanJaso = updateRomanJaso(TextUtils.romanToJaso(roman), hangleJaso);
    int matchedCount = 0;
    for (int i = 0; i < hangleJaso.size(); i++) {
      String type = DICT_TYPE_JOSA_LIST[i];
      String key = generateKey(hangleJaso.get(i), romanJaso.get(i), type);
      if (!dictionary.contains(key)) {
        if (i == 0 && lastName) {
          if (dictionary.contains(generateKey(hangleJaso.get(i), "^" + romanJaso.get(i), type))) {
            matchedCount += 1;
            continue;
          }
        }
        if (i == 1 && !hangleJaso.get(2).isEmpty()) {
          if (dictionary.contains(generateKey(hangleJaso.get(i), romanJaso.get(i) + "+", type))) {
            matchedCount += 1;
            continue;
          }
        }
      } else {
        matchedCount += 1;
      }
    }
    if (matchedCount < 3) {
      return matchedCount;
    }
    return MATCHED_SCORE;
  }

  private String generateKey(String hangle, String roman, String type) {
    return type + "|" + hangle + "|" + roman;
  }

  List<String> updateRomanJaso(List<String> romanJaso, List<String> hangleJaso) {
    if (romanJaso.size() < 3) {
      return romanJaso;
    }
    if (hangleJaso.size() < 3) {
      return romanJaso;
    }
    String hangleJongsung = hangleJaso.get(2);
    String romanChosung = romanJaso.get(0);
    String romanJungsung = romanJaso.get(1);
    String romanJongsung = romanJaso.get(2);
    if (!hangleJongsung.isEmpty() && !romanJungsung.isEmpty() && romanJongsung.isEmpty()) {
      int romanJungsungLastIndex = romanJungsung.length() - 1;
      String romanJungsungLast = String.valueOf(romanJungsung.charAt(romanJungsungLastIndex));
      if (romanJungsungLast.equals("R") || romanJungsungLast.equals("H")) {
        if (dictionary.contains(generateKey(hangleJongsung, romanJungsungLast, DICT_TYPE_JOSA_LIST[2]))) {
          romanJungsung = romanJungsung.substring(0, romanJungsungLastIndex);
          romanJongsung = String.valueOf(romanJungsungLast);
          return Arrays.asList(romanChosung, romanJungsung, romanJongsung);
        }
      }
    }
    return romanJaso;
  }

}
