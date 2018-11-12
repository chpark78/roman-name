package romanname.challenge;

import java.util.Map;

public class KoreanRomanMatcher {

  private final Map<Character, LinkedCharSequence> dictionary;

  public KoreanRomanMatcher(Map<Character, LinkedCharSequence> dictionary) {
    this.dictionary = dictionary;
  }

  public boolean matches(String hangleName, String romanLastName, String romanFirstName) {
    String romanName = romanLastName + " " + romanFirstName;
    romanName = normalize(romanName);

    boolean found = false;
    int romanIndex = 0;
    int romanMemento = -1;
    LinkedCharSequence seqMemento = null;
    boolean restore = false;
    for (int hangleIndex = 0, hangleMemento = -1; hangleIndex < hangleName.length(); hangleIndex++) {
      found = false;

      char hangleChar = hangleName.charAt(hangleIndex);
      LinkedCharSequence seq;
      if (restore) {
        romanIndex = romanMemento + 1;
        seq = seqMemento;
        romanMemento = -1;
        hangleMemento = -1;
        seqMemento = null;
        restore = false;
      } else {
        seq = dictionary.get(hangleChar);
      }

      // System.out.println(seq);
      for (; romanIndex < romanName.length(); romanIndex++) {
        char romanChar = romanName.charAt(romanIndex);

        seq = seq.nextStartWith(romanChar);
        if (seq == null) {
          break;
        }

        // System.out.println(String.valueOf(romanChar + ": " + seq));

        if (seq.canBreak()) {
          found = true;
          char nextRomanChar = romanIndex + 1 < romanName.length() ? romanName.charAt(romanIndex + 1) : '@';
          if (nextRomanChar == ' ') {
            romanIndex += 2;
          } else {
            if (seq.canContinue()) {
              romanMemento = romanIndex;
              hangleMemento = hangleIndex;
              seqMemento = seq;
            }
            romanIndex++;
          }
          break;
        }
      }

      if (!found) {
        if (romanMemento == -1) {
          break;
        }
        hangleIndex = hangleMemento - 1;
        restore = true;
      }
    }
    return found;
  }

  private String normalize(String roman) {
    return roman.replaceAll("[ ]{2,}", " -");
  }

}
