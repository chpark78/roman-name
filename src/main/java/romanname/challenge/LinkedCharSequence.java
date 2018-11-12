package romanname.challenge;

import java.util.HashMap;
import java.util.Map;

public class LinkedCharSequence {

  private final char self;

  private final Map<Character, LinkedCharSequence> children = new HashMap<>();

  public LinkedCharSequence(char self) {
    this.self = self;
  }

  public char getChar() {
    return self;
  }

  public LinkedCharSequence nextStartWith(char ch) {
    return children.get(ch);
  }

  public void link(LinkedCharSequence other) {
    children.put(other.self, other);
  }

  public boolean canBreak() {
    return children.containsKey(' ');
  }

  public boolean canContinue() {
    return children.size() > (canBreak() ? 1 : 0);
  }

  @Override
  public String toString() {
    if (self == ' ' && children.isEmpty()) {
      return "END";
    }
    return self + "->" + children.values();
  }

}
