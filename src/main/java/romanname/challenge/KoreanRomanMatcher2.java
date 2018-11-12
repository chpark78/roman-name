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
    String romanName = romanLastName + " " + romanFirstName;
    romanName = normalize(romanName);

    List<CharSequenceFinder> finders = new ArrayList<>();
    for (int i = 0; i < hangleName.length(); i++) {
      char hangleChar = hangleName.charAt(i);
      LinkedCharSequence sequence = dictionary.get(hangleChar);
      finders.add(new CharSequenceFinder(sequence, romanName));
    }

    int romanNameIndex = 0;
    for (int i = 0; i < hangleName.length(); i++) {
      CharSequenceFinder finder = finders.get(i);
      finder.resetIndex(romanNameIndex);
      if (finder.hasNext()) {
        romanNameIndex = finder.next();
      } else {
        if (i == 0) {
          break;
        }
        finder.resetSeq();
        i--;
      }
    }

    return romanName.length() == romanNameIndex;
  }

  private String normalize(String roman) {
    return roman.replaceAll("[ ]{2,}", " -");
  }

}
