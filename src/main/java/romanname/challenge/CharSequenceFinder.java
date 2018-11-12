package romanname.challenge;

import java.util.Iterator;

public class CharSequenceFinder implements Iterator<Integer> {

  private final LinkedCharSequence startSeq;

  private final String target;

  private int currentIndex;

  private LinkedCharSequence currentSeq;

  public CharSequenceFinder(LinkedCharSequence startSeq, String target) {
    this.startSeq = startSeq;
    this.target = target;
    currentSeq = startSeq;
  }

  public void resetIndex(int startIndex) {
    currentIndex = startIndex;
  }

  public void resetSeq() {
    currentSeq = startSeq;
  }

  @Override
  public boolean hasNext() {
    boolean hasNext = false;
    for (; currentIndex < target.length(); currentIndex++) {
      char ch = target.charAt(currentIndex);
      if (ch == ' ') {
        continue;
      }

      LinkedCharSequence nextSeq = currentSeq.nextStartWith(ch);
      if (nextSeq == null) {
        break;
      }
      currentSeq = nextSeq;
      if (nextSeq.canBreak()) {
        hasNext = true;
        currentIndex++;
        break;
      }
    }
    return hasNext;
  }

  @Override
  public Integer next() {
    return currentIndex;
  }

}
