package com.kakaobank.bigdata.romanname;

import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.file.Paths;

public class DefaultHangleRomanMatcherTest {

  @Test
  public void SDK_가이드에_기술된_방식으로_실행된다() throws Exception {
    System.out.println(Paths.get("").toAbsolutePath());
    // 사전 경로 및 인코딩 정보
    String dictFilePath = "data/hangle_roman_dict.txt";
    Charset charset = Charset.forName("utf-8");

    // 사전 생성
    Dictionary dictionary = new DefaultDictionary(new FileDictionaryLoader(dictFilePath, charset));

    // HangleRomanMatcher 객체를 생성
    HangleRomanMatcher matcher = new DefaultHangleRomanMatcher(dictionary);

    String hangleName = "박요수아";
    String lastName = "PARK";
    String firstName = "JOSUA";

    // 미리 생성한 HangleRomanMatcher 객체로 부터 matching 함수 실행
    MatchedResult result = matcher.matching(hangleName, firstName, lastName);
    System.out.println(result);

    // 사전을 다시 로드
    dictionary.reload();
  }

  // @Test
  // public void matchedBySyllableTest() throws Exception {
  //   String dict[] = {
  //       "SYLLABLE|한|HAN",
  //       "CHOSUNG|ㄱ|K",
  //       "JUNGSUNG|ㅣ|I",
  //       "JONGSUNG|ㅁ|M",
  //       "CHOSUNG|ㅈ|^CH",
  //       "JUNGSUNG|ㅓ|U+",
  //       "JONGSUNG|ㄴ|N",
  //       "JONGSUNG||"
  //   };
  //
  //   DictionaryLoader dictionaryLoader = mock(DictionaryLoader.class);
  //   when(dictionaryLoader.load()).thenReturn(new HashSet<>(Arrays.asList(dict)));
  //
  //   DefaultDictionary dictionary = new DefaultDictionary(dictionaryLoader);
  //   DefaultHangleRomanMatcher matcher = new DefaultHangleRomanMatcher(dictionary);
  //
  //   assertEquals(10, matcher.matchedScoreBySyllable("한", "HAN", true));
  //   assertEquals(10, matcher.matchedScoreBySyllable("김", "KIM", true));
  //   assertEquals(10, matcher.matchedScoreBySyllable("전", "CHUN", true));
  //   assertEquals(2, matcher.matchedScoreBySyllable("전", "CHUN", false));
  //   assertEquals(2, matcher.matchedScoreBySyllable("저", "CHU", true));
  // }
  //
  // @Test
  // public void matchingByNameTest() throws Exception {
  //   String dict[] = {
  //       "NAME|드림|DREAM",
  //       "SYLLABLE|한|HAN",
  //       "SYLLABLE|국|GUK",
  //       "CHOSUNG|ㄱ|K",
  //       "JUNGSUNG|ㅣ|I",
  //       "JONGSUNG|ㅁ|M"
  //   };
  //
  //   DictionaryLoader dictionaryLoader = mock(DictionaryLoader.class);
  //   when(dictionaryLoader.load()).thenReturn(new HashSet<>(Arrays.asList(dict)));
  //
  //   DefaultDictionary dictionary = new DefaultDictionary(dictionaryLoader);
  //   DefaultHangleRomanMatcher matcher = new DefaultHangleRomanMatcher(dictionary);
  //
  //   assertTrue(matcher.matchingByName("김드림", "DREAM", "KIM").isMatched());
  // }
  //
  // @Test
  // public void matchingBySyllableTest() throws Exception {
  //   String dict[] = {
  //       "NAME|드림|DREAM",
  //       "SYLLABLE|한|HAN",
  //       "SYLLABLE|국|GUK",
  //       "CHOSUNG|ㄱ|K",
  //       "JUNGSUNG|ㅣ|I",
  //       "JONGSUNG|ㅁ|M"
  //   };
  //
  //   DictionaryLoader dictionaryLoader = mock(DictionaryLoader.class);
  //   when(dictionaryLoader.load()).thenReturn(new HashSet<>(Arrays.asList(dict)));
  //
  //   DefaultDictionary dictionary = new DefaultDictionary(dictionaryLoader);
  //   DefaultHangleRomanMatcher matcher = new DefaultHangleRomanMatcher(dictionary);
  //
  //   assertFalse(matcher.matchingBySyllable("김드림", "DREAM", "KIM").isMatched());
  //   assertTrue(matcher.matchingBySyllable("김한국", "HANGUK", "KIM").isMatched());
  //   assertFalse(matcher.matchingBySyllable("김한국", "HAN GUK K", "KIM").isMatched());
  // }

  // @Test
  // public void matchingTest() throws Exception {
  //   String dict[] = {
  //       "NAME|드림|DREAM",
  //       "SYLLABLE|한|HAN",
  //       "SYLLABLE|국|GUK",
  //       "CHOSUNG|ㄱ|K",
  //       "JUNGSUNG|ㅏ|A",
  //       "JUNGSUNG|ㅏ|AR",
  //       "JUNGSUNG|ㅣ|I",
  //       "JONGSUNG|ㅁ|M",
  //       "JONGSUNG|R|ㄹ"
  //   };
  //
  //   DictionaryLoader dictionaryLoader = mock(DictionaryLoader.class);
  //   when(dictionaryLoader.load()).thenReturn(new HashSet<>(Arrays.asList(dict)));
  //
  //   DefaultDictionary dictionary = new DefaultDictionary(dictionaryLoader);
  //   DefaultHangleRomanMatcher matcher = new DefaultHangleRomanMatcher(dictionary);
  //
  //   assertTrue(matcher.matching("김드림", "DREAM", "KIM").isMatched());
  //   assertTrue(matcher.matching("김한국", "HANGUK", "KIM").isMatched());
  //   assertFalse(matcher.matching("드림김", "KIM", "DREAM").isMatched());
  //   assertTrue(matcher.matching("김한국", "HANGUK", "KIM").isMatched());
  //   assertFalse(matcher.matching("김한국", "HAN GUK K", "KIM").isMatched());
  //   assertFalse(matcher.matching("감한갈", "HANGAR", "KARM").isMatched());
  // }

}
