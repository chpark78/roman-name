package romanname.challenge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KoreanRomanMatcher2 {

  private final Map<Character, LinkedCharSequence> dictionary;

  public KoreanRomanMatcher2(Map<Character, LinkedCharSequence> dictionary) {
    this.dictionary = dictionary;
  }

  public boolean matches(String hangleName, String romanLastName, String romanFirstName) {
    String romanName = normalize(romanLastName + " " + romanFirstName);

    List<CharSequenceFinder> finders = new ArrayList<>();
    int lastFinerIndex = hangleName.length() - 1;
    for (int i = 0; i <= lastFinerIndex; i++) {
      char hangleChar = hangleName.charAt(i);
      LinkedCharSequence sequence = dictionary.get(hangleChar);
      finders.add(new CharSequenceFinder(sequence, romanName));
    }

    int romanNameIndex = 0;
    int romanNameLength = romanName.length();
    boolean rollback = false;
    for (int i = 0; i < finders.size(); i++) {
      CharSequenceFinder finder = finders.get(i);

      if (!rollback) {
        finder.find(romanNameIndex);
      }

      if (i == lastFinerIndex) {
        while (finder.hasNext()) {
          romanNameIndex = finder.next() + 1;
          if (romanNameIndex == romanNameLength) {
            return true;
          }
        }
      } else if (finder.hasNext()) {
        romanNameIndex = finder.next() + 1;
        rollback = false;
        continue;
      } else if (i == 0) {
        return false;
      }

      rollback = true;
      i -= 2;
    }

    return false;
  }

  private String normalize(String roman) {
    return roman.replaceAll("[ ]{2,}", " ");
  }

}
