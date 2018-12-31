package com.kakaobank.bigdata.romanname;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultDictionaryTest {

  @Test
  public void reloadTest() throws Exception {
    Set<String> dictOld = new HashSet<>(Arrays.asList(
        "CHOSUNG|ㄱ|K",
        "JUNGSUNG|ㅣ|I",
        "JONGSUNG|ㅁ|M"
    ));

    Set<String> dictNew = new HashSet<>(Arrays.asList(
        "CHOSUNG|ㄱ|K",
        "JUNGSUNG|ㅣ|I",
        "JONGSUNG|ㅏ|A",
        "JONGSUNG|ㅁ|M"
    ));

    DictionaryLoader dictionaryLoader = mock(DictionaryLoader.class);
    when(dictionaryLoader.load()).thenReturn(dictOld);

    Dictionary dictionary = new DefaultDictionary(dictionaryLoader);
    dictionary.reload();
    assertTrue(dictionary.getData().contains("CHOSUNG|ㄱ|K"));
    assertFalse(dictionary.getData().contains("JONGSUNG|ㅏ|A"));

    when(dictionaryLoader.load()).thenReturn(dictNew);
    dictionary.reload();
    assertTrue(dictionary.getData().contains("CHOSUNG|ㄱ|K"));
    assertTrue(dictionary.getData().contains("JONGSUNG|ㅏ|A"));
  }

}
