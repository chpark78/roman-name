import java.util.HashMap;
import java.util.Map;

public class CharTreeNode {

  private final char self;

  private final Map<Character, CharTreeNode> children = new HashMap<>();

  public CharTreeNode(char self) {
    this.self = self;
  }

  public char getSelf() {
    return self;
  }

  public CharTreeNode getChild(Character child) {
    return children.get(child);
  }

  public void addChild(CharTreeNode child) {
    children.put(child.self, child);
  }

  public boolean canEnd() {
    return children.containsKey(' ');
  }

  public boolean canContinue() {
    return canEnd() ? children.size() > 1 : !children.isEmpty();
  }

  @Override
  public String toString() {
    if (self == ' ' && children.isEmpty()) {
      return "END";
    }
    return "{" + (self == ' ' ? "BEGIN" : self) + ", " + children.values() + '}';
  }

}
