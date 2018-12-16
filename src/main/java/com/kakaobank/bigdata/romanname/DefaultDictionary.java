package com.kakaobank.bigdata.romanname;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.emptyList;

public class DefaultDictionary implements Dictionary {

  private final DictionaryLoader dictionaryLoader;

  private volatile Collection<String> data = emptyList();

  private final List<Listener> listeners = new ArrayList<>();

  public DefaultDictionary(DictionaryLoader dictionaryLoader) {
    this.dictionaryLoader = dictionaryLoader;
  }

  @Override
  public void addListener(Listener listener) {
    listeners.add(listener);
  }

  @Override
  public Collection<String> getData() {
    return data;
  }

  @Override
  public void reload() throws Exception {
    this.data = dictionaryLoader.load();
    notifyListeners();
  }

  private void notifyListeners() {
    for (Listener listener : listeners) {
      listener.dataReloaded(data);
    }
  }

}
