import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DictionaryLoader {

  public Map<Character, CharTreeNode> load(Path filePath) throws IOException {
    List<String> lines = Files.readAllLines(filePath);

    Map<Character, CharTreeNode> dictionary = new HashMap<>();
    for (String line : lines) {
      String[] parts = line.split(" ");
      Character korean = parts[0].charAt(0);
      String roman = parts[1];

      CharTreeNode node = dictionary.computeIfAbsent(korean, k -> new CharTreeNode(' '));
      for (int i = 0; i < roman.length(); i++) {
        char child = roman.charAt(i);
        CharTreeNode childNode = node.getChild(child);
        if (childNode == null) {
          childNode = new CharTreeNode(child);
          node.addChild(childNode);
        }
        node = childNode;
      }
      if (node.getChild(' ') == null) {
        node.addChild(new CharTreeNode(' '));
      }
    }
    return dictionary;
  }

}
