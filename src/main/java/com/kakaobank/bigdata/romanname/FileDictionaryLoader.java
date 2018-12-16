package com.kakaobank.bigdata.romanname;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import static java.nio.charset.Charset.defaultCharset;
import static java.util.stream.Collectors.toList;

public class FileDictionaryLoader implements DictionaryLoader {

  private final Path filePath;

  private final Charset charset;

  public FileDictionaryLoader(String filePath) {
    this(Paths.get(filePath));
  }

  public FileDictionaryLoader(Path filePath) {
    this(filePath, defaultCharset());
  }

  public FileDictionaryLoader(String filePath, Charset charset) {
    this(Paths.get(filePath), charset);
  }

  public FileDictionaryLoader(Path filePath, Charset charset) {
    this.filePath = filePath;
    this.charset = charset;
  }

  @Override
  public Collection<String> load() throws Exception {
    return Files.readAllLines(filePath, charset)
                .stream()
                .map(String::trim)
                .filter(s -> !(s.isEmpty() || s.startsWith("#")))
                .collect(toList());
  }

}
