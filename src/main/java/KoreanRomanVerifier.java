import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class KoreanRomanVerifier {

  private final Map<Character, CharTreeNode> dictionary;

  public KoreanRomanVerifier(Map<Character, CharTreeNode> dictionary) {
    this.dictionary = dictionary;
  }

  public boolean verify(String korean, String roman) {
    roman = roman.replaceAll("[ ]{2,}", " "); // normalize

    boolean found = false;
    int romanIndex = 0;
    int romanMemento = 0;
    for (int koreanIndex = 0, koreanMemento = 0; koreanIndex < korean.length(); koreanIndex++) {
      found = false;
      char koreanChar = korean.charAt(koreanIndex);
      CharTreeNode node = dictionary.get(koreanChar);
      for (; romanIndex < roman.length(); romanIndex++) {
        char romanChar = roman.charAt(romanIndex);

        node = node.getChild(romanChar);
        if (node == null) {
          break;
        }

        if (node.canEnd()) {
          found = true;
          char nextRomanChar = romanIndex + 1 < roman.length() ? roman.charAt(romanIndex + 1) : '@';
          if (nextRomanChar == ' ') {
            romanIndex += 2;
          } else {
            if (node.canContinue()) {
              romanMemento = romanIndex;
              koreanMemento = koreanIndex;
            }
            romanIndex++;
          }
          break;
        }
      }

      if (!found) {
        if (romanMemento == 0) {
          break;
        }
        romanIndex = romanMemento;
        koreanIndex = koreanMemento;
      }
    }
    return found;
  }

  public static void main(String[] args) throws Exception {
    URI fileUri = DictionaryLoader.class.getResource("korean-roman.dic").toURI();
    Path filePath = Paths.get(fileUri);
    Map<Character, CharTreeNode> dictionary = new DictionaryLoader().load(filePath);
    KoreanRomanVerifier verifier = new KoreanRomanVerifier(dictionary);
    boolean result = verifier.verify("박찬호", "PARK  CHAN  HO");
    System.out.println(result);
  }

}
