package com.kakaobank.bigdata.romanname.util;

import org.junit.Test;

import java.util.Arrays;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

public class TextUtilsTest {

  @Test
  public void hangleToJasoTest() {
    assertEquals(Arrays.asList("ㅎ", "ㅏ", "ㄴ"), TextUtils.hangleToJaso("한"));
    assertEquals(Arrays.asList("ㅈ", "ㅏ", ""), TextUtils.hangleToJaso("자"));
  }

  @Test
  public void romanToJasoTest() {
    assertEquals(Arrays.asList("H", "A", "N"), TextUtils.romanToJaso("HAN"));
    assertEquals(Arrays.asList("J", "A", ""), TextUtils.romanToJaso("JA"));
    assertEquals(Arrays.asList("G", "E", "L"), TextUtils.romanToJaso("GLE"));
    assertEquals(Arrays.asList("WH", "I", ""), TextUtils.romanToJaso("WHI"));
    assertEquals(Arrays.asList("", "I", "M"), TextUtils.romanToJaso("IM"));
    assertEquals(Arrays.asList("", "", ""), TextUtils.romanToJaso(""));
  }

  @Test
  public void splitSyllablesTest() {
    assertEquals(Arrays.asList("A BC D", "AB C D"), TextUtils.splitSyllables("ABC D", 3));
    assertEquals(singletonList("AB C"), TextUtils.splitSyllables("AB C", 4));
    assertEquals(singletonList("A B C D"), TextUtils.splitSyllables("A B C D", 3));
  }

  @Test
  public void cleansingTest() {
    String actual = TextUtils.normalize(" AB  C D ");
    assertEquals("AB C D", actual);
  }

  @Test
  public void spacingTest() {
    assertEquals("AB CD E", TextUtils.spacing("ABCDE", Arrays.asList(2, 4)));
  }

}
