package com.kakaobank.bigdata.romanname;

/**
 * 한글명에 대한 로마자표기를 매칭
 */
public interface HangleRomanMatcher {

  /**
   * 한글명에 대한 로마자표기를 매칭하는 함수
   *
   * @param hangleName 한글명
   * @param firstName 로마자 성
   * @param lastName 로마자 이름
   * @return MatchedResult
   */
  MatchedResult matching(String hangleName, String firstName, String lastName);

}
