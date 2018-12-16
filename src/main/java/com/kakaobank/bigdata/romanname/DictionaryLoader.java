package com.kakaobank.bigdata.romanname;

import java.util.Collection;

public interface DictionaryLoader {

  /**
   * 한글 로마자 검사를 위한 데이터를 로드
   *
   * @return 데이터 파일 내용
   * @throws Exception
   */
  Collection<String> load() throws Exception;

}
