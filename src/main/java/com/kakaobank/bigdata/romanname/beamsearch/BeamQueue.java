package com.kakaobank.bigdata.romanname.beamsearch;

import java.util.PriorityQueue;

public class BeamQueue extends PriorityQueue<BeamNode> {

  private BeamNode maxValue = null;

  private final long maxSize;

  public BeamQueue(long maxSize) {
    this.maxSize = maxSize;
  }

  @Override
  public boolean offer(BeamNode value) {
    if (maxValue == null) {
      maxValue = value;
    } else if (maxValue.compareTo(value) < 0) {
      maxValue = value;
    }

    boolean result = super.offer(value);
    if (size() > maxSize) {
      poll();
    }

    return result;
  }

  @Override
  public boolean add(BeamNode value) {
    return offer(value);
  }

  BeamNode getMaxValue() {
    return maxValue;
  }

}
