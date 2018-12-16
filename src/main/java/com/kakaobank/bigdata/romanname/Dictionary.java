package com.kakaobank.bigdata.romanname;

import java.util.Collection;

public interface Dictionary {

  interface Listener {
    void dataReloaded(Collection<String> data);
  }

  void addListener(Listener listener);

  Collection<String> getData();

  void reload() throws Exception;

}
