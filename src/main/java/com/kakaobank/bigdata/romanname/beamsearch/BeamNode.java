package com.kakaobank.bigdata.romanname.beamsearch;

import com.kakaobank.bigdata.romanname.MatchedEntry;

import java.util.ArrayList;
import java.util.List;

public class BeamNode implements Comparable<BeamNode> {

  private int score = 0;

  private int lastIndex = 0;

  private boolean matched = true;

  private final List<MatchedEntry> entries = new ArrayList<>();

  public BeamNode() {
  }

  public BeamNode(BeamNode o) {
    score = o.score;
    lastIndex = o.lastIndex;
    matched = o.matched;
    entries.addAll(o.entries);
  }

  int getLastIndex() {
    return lastIndex;
  }

  int getScore() {
    return score;
  }

  void add(MatchedEntry entry, int score) {
    entries.add(entry);
    this.score += score;
    if (!entry.isMatched()) {
      matched = false;
    }
  }

  void setLastIndex(int index) {
    lastIndex = index;
  }

  List<MatchedEntry> getEntries() {
    return entries;
  }

  @Override
  public int compareTo(BeamNode o) {
    return score - o.score;
  }

  boolean isMatched() {
    return matched;
  }

}
