package romanname.challenge;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SyllableBuilder {

  public void build(Path inputFilePath, Path outputFilePath) throws Exception {
    List<String> inputFileLines = Files.readAllLines(inputFilePath);
    Set<String> syllables = new TreeSet<>();
    for (String line : inputFileLines) {
      String[] lineParts = line.split("\t");
      String hangleName = lineParts[0];
      for (int i = 0; i < hangleName.length(); i++) {
        syllables.add(String.valueOf(hangleName.charAt(i)));
      }
    }

    Files.write(outputFilePath, syllables, StandardOpenOption.TRUNCATE_EXISTING);
  }

}
