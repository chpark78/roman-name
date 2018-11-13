package romanname.challenge;

import romanname.HangleRomanMatcher;
import romanname.MatchedResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GazuaHangleRomanMatcher implements HangleRomanMatcher {

  private final Map<String, String> nameDictionary;

  private final Map<Character, LinkedCharSequence> syllableRomanSequenceMap;

  public GazuaHangleRomanMatcher(Map<String, String> nameDictionary, Map<Character, LinkedCharSequence> syllableRomanSequenceMap) {
    this.nameDictionary = nameDictionary;
    this.syllableRomanSequenceMap = syllableRomanSequenceMap;
  }

  @Override
  public MatchedResult matching(String hangleName, String romanFirstName, String romanLastName) {
    String newHangleName = findName(hangleName, romanLastName);
    if (newHangleName.length() != hangleName.length()) {
      hangleName = newHangleName;
      romanLastName = "";
    }

    newHangleName = findName(hangleName, romanFirstName);
    if (newHangleName.length() != hangleName.length()) {
      hangleName = newHangleName;
      romanFirstName = "";
    }

    if (hangleName.isEmpty()) {
      return new MatchedResult(true, Collections.emptyList());
    }

    String romanName = normalize(romanLastName + " " + romanFirstName);
    boolean matched = matches(hangleName, romanName);
    return new MatchedResult(matched, Collections.emptyList());
  }

  private String findName(String hangleName, String romanName) {
    String name = nameDictionary.get(romanName);
    if (name != null) {
      hangleName = hangleName.replaceAll("^" + name + "|" + name + "$", "").trim();
    }
    return hangleName;
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
    return roman.replaceAll("[ ]{2,}", " ").trim();
  }

}
