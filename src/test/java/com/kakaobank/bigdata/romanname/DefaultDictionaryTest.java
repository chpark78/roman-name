package com.kakaobank.bigdata.romanname;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultDictionaryTest {

  @Test
  public void reloadTest() throws Exception {
    List<String> dictOld = Arrays.asList(
        "CHOSUNG|ㄱ|K",
        "JUNGSUNG|ㅣ|I",
        "JONGSUNG|ㅁ|M"
    );

    List<String> dictNew = Arrays.asList(
        "CHOSUNG|ㄱ|K",
        "JUNGSUNG|ㅣ|I",
        "JONGSUNG|ㅏ|A",
        "JONGSUNG|ㅁ|M"
    );

    DictionaryLoader dictionaryLoader = mock(DictionaryLoader.class);
    when(dictionaryLoader.load()).thenReturn(new HashSet<>(dictOld));

    Dictionary dictionary = new DefaultDictionary(dictionaryLoader);
    assertTrue(dictionary.getData().contains("CHOSUNG|ㄱ|K"));
    assertFalse(dictionary.getData().contains("JONGSUNG|ㅏ|A"));

    when(dictionaryLoader.load()).thenReturn(new HashSet<>(dictNew));
    dictionary.reload();
    assertTrue(dictionary.getData().contains("CHOSUNG|ㄱ|K"));
    assertTrue(dictionary.getData().contains("JONGSUNG|ㅏ|A"));
  }

}
