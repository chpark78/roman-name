package romanname.challenge;

import romanname.HangleRomanMatcher;
import romanname.MatchedEntry;
import romanname.MatchedResult;

import java.util.ArrayList;
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
    List<MatchedEntry> matchedEntries = new ArrayList<>(hangleName.length());

    MatchedEntry lastNameResult = findName(hangleName, romanLastName, true);
    if (lastNameResult.isMatched()) {
      matchedEntries.add(lastNameResult);
      hangleName = hangleName.replaceAll("^" + lastNameResult.getHangle() + "|" + lastNameResult.getHangle() + "$", "").trim();
      romanLastName = "";
    } else {
      romanLastName += " ";
    }

    MatchedEntry firstNameResult = findName(hangleName, romanFirstName, false);
    if (firstNameResult.isMatched()) {
      hangleName = hangleName.replaceAll("^" + firstNameResult.getHangle() + "|" + firstNameResult.getHangle() + "$", "").trim();
      romanFirstName = "";
    }

    if (hangleName.isEmpty()) {
      return new MatchedResult(true, matchedEntries);
    }

    String romanName = normalize(romanLastName + romanFirstName);
    boolean matched = findMatches(hangleName, romanName, matchedEntries);

    if (firstNameResult.isMatched()) {
      matchedEntries.add(firstNameResult);
    }

    return new MatchedResult(matched, matchedEntries);
  }

  private MatchedEntry findName(String hangleName, String romanName, boolean lastName) {
    String name = nameDictionary.get(romanName);
    if (name == null && lastName) {
      name = nameDictionary.get("^" + romanName);
    }
    boolean matched = name != null && (hangleName.startsWith(name) || hangleName.endsWith(name));
    return new MatchedEntry(name, romanName, matched);
  }

  private boolean findMatches(String hangleName, String romanName, List<MatchedEntry> matchedEntries) {
    int matchedEntriesBeginIndex = matchedEntries.size();
    List<CharSequenceFinder> finders = new ArrayList<>(hangleName.length());
    int lastFinerIndex = hangleName.length() - 1;
    for (int i = 0; i <= lastFinerIndex; i++) {
      char hangleChar = hangleName.charAt(i);
      LinkedCharSequence sequence = syllableRomanSequenceMap.get(hangleChar);
      finders.add(new CharSequenceFinder(sequence, romanName));
      matchedEntries.add(new MatchedEntry(String.valueOf(hangleChar), "", false));
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
            String hangle = String.valueOf(hangleName.charAt(i));
            String roman = romanName.substring(finder.getBeginIndex(), romanNameIndex);
            matchedEntries.set(matchedEntriesBeginIndex + i, new MatchedEntry(hangle, roman, true));
            return true;
          }
        }
      } else if (finder.hasNext()) {
        romanNameIndex = finder.next() + 1;
        String hangle = String.valueOf(hangleName.charAt(i));
        String roman = romanName.substring(finder.getBeginIndex(), romanNameIndex);
        matchedEntries.set(matchedEntriesBeginIndex + i, new MatchedEntry(hangle, roman, true));
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
