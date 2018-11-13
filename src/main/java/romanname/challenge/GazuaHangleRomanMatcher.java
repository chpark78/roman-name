package romanname.challenge;

import romanname.HangleRomanMatcher;
import romanname.MatchedEntry;
import romanname.MatchedResult;

import java.util.*;

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
    return matches(hangleName, romanName);
  }

  private String findName(String hangleName, String romanName) {
    String name = nameDictionary.get(romanName);
    if (name != null) {
      hangleName = hangleName.replaceAll("^" + name + "|" + name + "$", "").trim();
    }
    return hangleName;
  }

  private MatchedResult matches(String hangleName, String romanName) {
    List<CharSequenceFinder> finders = new ArrayList<>();
    int lastFinerIndex = hangleName.length() - 1;
    for (int i = 0; i <= lastFinerIndex; i++) {
      char hangleChar = hangleName.charAt(i);
      LinkedCharSequence sequence = syllableRomanSequenceMap.get(hangleChar);
      finders.add(new CharSequenceFinder(sequence, romanName));
    }

    List<MatchedEntry> matchedEntries = new LinkedList<>();
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
          int newRomanNameIndex = finder.next() + 1;
          if (newRomanNameIndex == romanNameLength) {
            String hangle = String.valueOf(hangleName.charAt(i));
            String roman = romanName.substring(romanNameIndex, newRomanNameIndex);
            matchedEntries.add(new MatchedEntry(hangle, roman, true));
            return new MatchedResult(true, matchedEntries);
          }
        }
      } else if (finder.hasNext()) {
        int newRomanNameIndex = finder.next() + 1;
        String hangle = String.valueOf(hangleName.charAt(i));
        String roman = romanName.substring(romanNameIndex, newRomanNameIndex);
        matchedEntries.add(new MatchedEntry(hangle, roman, true));
        romanNameIndex = newRomanNameIndex;
        rollback = false;
        continue;
      } else if (i == 0) {
        return new MatchedResult(false, matchedEntries);
      }

      rollback = true;
      i -= 2;
    }

    return new MatchedResult(false, matchedEntries);
  }

  private String normalize(String roman) {
    return roman.replaceAll("[ ]{2,}", " ").trim();
  }

}
