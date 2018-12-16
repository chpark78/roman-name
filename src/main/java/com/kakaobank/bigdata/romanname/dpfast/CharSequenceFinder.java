package com.kakaobank.bigdata.romanname.dpfast;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public class CharSequenceFinder implements Iterator<Integer> {

  private final LinkedCharSequence seq;

  private final String str;

  private Deque<Integer> matches;

  private int beginIndex;

  public CharSequenceFinder(LinkedCharSequence seq, String str) {
    this.seq = seq;
    this.str = str;
  }

  public void find(int beginIndex) {
    matches = new LinkedList<>();
    LinkedCharSequence currentSeq = seq;
    for (int i = this.beginIndex = beginIndex; i < str.length(); i++) {
      char ch = str.charAt(i);
      if (ch == ' ') {
        if (i == beginIndex) {
          continue;
        } else {
          break;
        }
      }

      LinkedCharSequence nextSeq = currentSeq.nextStartWith(ch);
      if (nextSeq == null) {
        break;
      }

      currentSeq = nextSeq;
      if (nextSeq.canBreak()) {
        matches.push(i);
      }
    }
  }

  public int getBeginIndex() {
    return beginIndex;
  }

  @Override
  public boolean hasNext() {
    return !matches.isEmpty();
  }

  @Override
  public Integer next() {
    return matches.pollFirst();
  }

}
