package romanname;

import java.util.Set;

public class DefaultDictionary implements Dictionary {

  private volatile Set<String> dataSet;
  private final DictionaryLoader dictionaryLoader;

  public DefaultDictionary(DictionaryLoader dictionaryLoader) throws Exception {
    this.dictionaryLoader = dictionaryLoader;
    this.dataSet = dictionaryLoader.load();
  }

  @Override
  public boolean contains(String key) {
    return dataSet.contains(key);
  }

  @Override
  public void reload() throws Exception {
    this.dataSet = dictionaryLoader.load();
  }

}
