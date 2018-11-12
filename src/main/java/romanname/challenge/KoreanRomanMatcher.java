package romanname.challenge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KoreanRomanMatcher {

  private final Map<String, String> nameDictionary;

  private final Map<Character, LinkedCharSequence> syllableRomanSequenceMap;

  public KoreanRomanMatcher(Map<String, String> nameDictionary, Map<Character, LinkedCharSequence> syllableRomanSequenceMap) {
    this.nameDictionary = nameDictionary;
    this.syllableRomanSequenceMap = syllableRomanSequenceMap;
  }

  public boolean matches(String hangleName, String romanLastName, String romanFirstName) {
    String lastName = nameDictionary.get(romanLastName);
    if (lastName != null) {
      String newHangleName = hangleName.replaceAll("^" + lastName + "|" + lastName + "$", "").trim();
      if (hangleName.length() != newHangleName.length()) {
        hangleName = newHangleName;
        romanLastName = "";
      }
    }

    String firstName = nameDictionary.get(romanFirstName);
    if (firstName != null) {
      String newHangleName = hangleName.replaceAll("^" + firstName + "|" + firstName + "$", "").trim();
      if (hangleName.length() != newHangleName.length()) {
        hangleName = newHangleName;
        romanFirstName = "";
      }
    }

    if (hangleName.isEmpty()) {
      return true;
    }

    String romanName = normalize(romanLastName + " " + romanFirstName).trim();
    return matches(hangleName, romanName);
  }

  private boolean matches(String hangleName, String romanName) {
    List<CharSequenceFinder> finders = new ArrayList<>();
    int lastFinerIndex = hangleName.length() - 1;
    for (int i = 0; i <= lastFinerIndex; i++) {
      char hangleChar = hangleName.charAt(i);
      LinkedCharSequence sequence = syllableRomanSequenceMap.get(hangleChar);
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
