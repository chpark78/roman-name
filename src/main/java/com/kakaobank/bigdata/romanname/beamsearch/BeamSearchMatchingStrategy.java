package com.kakaobank.bigdata.romanname.beamsearch;

import com.kakaobank.bigdata.romanname.HangleRomanMatchingStrategy;
import com.kakaobank.bigdata.romanname.MatchedEntry;
import com.kakaobank.bigdata.romanname.util.TextUtils;

import java.util.*;

public class BeamSearchMatchingStrategy implements HangleRomanMatchingStrategy {

  private static final String DICT_TYPE_SYLLABLE = "SYLLABLE";

  private static final String[] DICT_TYPE_JOSA_LIST = {"CHOSUNG", "JUNGSUNG", "JONGSUNG"};

  private static final int MATCHED_SCORE = 10;

  private final int beamWidth;

  private volatile Set<String> data;

  public BeamSearchMatchingStrategy(Set<String> data) {
    this(data, 10);
  }

  private BeamSearchMatchingStrategy(Set<String> data, int beamWidth) {
    this.data = data;
    this.beamWidth = beamWidth;
  }

  @Override
  public void dataReloaded(Collection<String> data) {
    this.data = new HashSet<>(data);
  }

  @Override
  public List<MatchedEntry> match(String hangleName, String romanName, boolean lastName) {
    String[] hangles = hangleName.split(TextUtils.hangleSplitter());

    BeamQueue queue = new BeamQueue(beamWidth);
    queue.add(new BeamNode());

    for (int i = 0; i < hangles.length - 1; i++) {
      BeamQueue nextQueue = new BeamQueue(beamWidth);
      String hangle = hangles[i];
      for (BeamNode prev : queue) {
        int start = prev.getLastIndex();
        for (int j = start + 1; j < romanName.length(); j++) {
          if (romanName.charAt(j - 1) == ' ') {
            break;
          }

          String roman = romanName.substring(start, j);

          BeamNode node = new BeamNode(prev);
          int score = matchedScoreBySyllable(hangle, roman, (i == 0));
          int lastIndex = (romanName.charAt(j) == ' ' ? j + 1 : j);

          node.add(new MatchedEntry(hangle, roman, (score == MATCHED_SCORE)), score);
          node.setLastIndex(lastIndex);
          nextQueue.add(node);
        }
      }
      queue = nextQueue;
    }

    BeamQueue lastQueue = new BeamQueue(beamWidth);
    String hangle = hangles[hangles.length - 1];
    for (BeamNode node : queue) {
      String roman = romanName.substring(node.getLastIndex());
      int score = matchedScoreBySyllable(hangle, roman, false);

      node.add(new MatchedEntry(hangle, roman, (score == MATCHED_SCORE)), score);
      lastQueue.add(node);
    }

    BeamNode maxNode = lastQueue.getMaxValue();
    return maxNode.getEntries();
  }

  private int matchedScoreBySyllable(String hangle, String roman, boolean lastName) {
    if (hangle.length() != 1) {
      return 0;
    }

    if (data.contains(generateKey(hangle, roman, DICT_TYPE_SYLLABLE))) {
      return MATCHED_SCORE;
    }

    if (lastName && data.contains(generateKey(hangle, "^" + roman, DICT_TYPE_SYLLABLE))) {
      return MATCHED_SCORE;
    }

    List<String> hangleJaso = TextUtils.hangleToJaso(hangle);
    List<String> romanJaso = updateRomanJaso(TextUtils.romanToJaso(roman), hangleJaso);

    int matchedCount = 0;
    for (int i = 0; i < hangleJaso.size(); i++) {
      String type = DICT_TYPE_JOSA_LIST[i];

      if (data.contains(generateKey(hangleJaso.get(i), romanJaso.get(i), type))) {
        matchedCount += 1;
      } else if (lastName && data.contains(generateKey(hangleJaso.get(i), "^" + romanJaso.get(i), type))) {
        matchedCount += 1;
      } else if (i == 1 && !hangleJaso.get(2).isEmpty() && data.contains(generateKey(hangleJaso.get(i), romanJaso.get(i) + "+", type))) {
        matchedCount += 1;
      }
    }

    if (matchedCount < 3) {
      return matchedCount;
    }

    return MATCHED_SCORE;
  }

  private List<String> updateRomanJaso(List<String> romanJaso, List<String> hangleJaso) {
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
        if (data.contains(generateKey(hangleJongsung, romanJungsungLast, DICT_TYPE_JOSA_LIST[2]))) {
          romanJungsung = romanJungsung.substring(0, romanJungsungLastIndex);
          romanJongsung = romanJungsungLast;
          return Arrays.asList(romanChosung, romanJungsung, romanJongsung);
        }
      }
    }

    return romanJaso;
  }

  private String generateKey(String hangle, String roman, String type) {
    return type + "|" + hangle + "|" + roman;
  }

}
