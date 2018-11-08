package romanname;

import java.util.List;
import java.util.Objects;

public class MatchedResult {

  private final boolean matched;
  private final List<MatchedEntry> matchedEntries;

  public MatchedResult(boolean matched, List<MatchedEntry> matchedEntries) {
    this.matched = matched;
    this.matchedEntries = matchedEntries;
  }

  /**
   * 한글명과 로마자표기의 검증 결과를 반환한다.
   *
   * @return 검증 결과
   */
  public boolean isMatched() {
    return matched;
  }

  /**
   * 한글(글자 또는 이름)과 로마자의 매칭된 결과를 반환한다.
   *
   * @return 매칭된 결과 목록
   */
  public List<MatchedEntry> getMatchedEntries() {
    return matchedEntries;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MatchedResult entry = (MatchedResult) o;
    return matched == entry.matched
        && Objects.equals(matchedEntries, entry.matchedEntries);
  }

  @Override
  public String toString() {
    return "MatchedResult{" +
        "matched=" + matched +
        ", matchedEntries=" + matchedEntries +
        '}';
  }

}
