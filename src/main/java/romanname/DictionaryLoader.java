package romanname;

import java.util.Set;
public interface DictionaryLoader {
  /**
   * 한글 로마자 검사를 위한 사전을 로드
   *
   * @return 사전
   * @throws Exception
   */
  Set<String> load() throws Exception;
}
