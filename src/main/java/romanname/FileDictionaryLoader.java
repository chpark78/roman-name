package romanname;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

public class FileDictionaryLoader implements DictionaryLoader {

  private final Path filePath;

  public FileDictionaryLoader(String filePath) {
    this(Paths.get(filePath));
  }

  public FileDictionaryLoader(Path filePath) {
    this.filePath = filePath;
  }

  @Override
  public Set<String> load() throws Exception {
    return Files.readAllLines(filePath)
                .stream()
                .map(String::trim)
                .filter(s -> !(s.isEmpty() || s.startsWith("#")))
                .collect(Collectors.toSet());
  }

}
