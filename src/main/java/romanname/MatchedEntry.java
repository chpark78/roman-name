package romanname;

import java.util.Objects;

public class MatchedEntry {

  private final String hangle;
  private final String roman;
  private final boolean matched;

  public MatchedEntry(String hangle, String roman, boolean matched) {
    this.hangle = hangle;
    this.roman = roman;
    this.matched = matched;
  }

  /**
   * 한글명으로 부터 추출된 글자 또는 이름
   *
   * @return 한글
   */
  public String getHangle() {
    return hangle;
  }

  /**
   * 한글과 매칭되는 로마자
   *
   * @return 로마자
   */
  public String getRoman() {
    return roman;
  }

  /**
   * 한글과 로마자 쌍이 적합 유무
   *
   * @return 적합 유무
   */
  public boolean isMatched() {
    return matched;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MatchedEntry entry = (MatchedEntry) o;
    return matched == entry.matched
        && Objects.equals(hangle, entry.hangle)
        && Objects.equals(roman, entry.roman);
  }

  @Override
  public int hashCode() {
    return Objects.hash(matched, hangle, roman);
  }

  @Override
  public String toString() {
    return "MatchedEntry{" +
        "hangle='" + hangle + '\'' +
        ", roman='" + roman + '\'' +
        ", matched=" + matched +
        '}';
  }

}
