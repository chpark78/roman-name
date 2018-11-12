package romanname.challenge;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NameDictionaryBuilder {

  public Map<String, String> build(Path nameDictionaryFilePath) throws IOException {
    List<String> fileLines = Files.readAllLines(nameDictionaryFilePath);

    Map<String, String> nameDictionary = new HashMap<>();
    for (String line : fileLines) {
      String[] lineParts = line.split("\\|");
      if (lineParts[0].equals("NAME")) {
        nameDictionary.put(lineParts[2], lineParts[1]);
      }
    }
    return nameDictionary;
  }

}
