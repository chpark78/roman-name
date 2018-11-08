package romanname;

public interface Dictionary {

  boolean contains(String key);

  void reload() throws Exception;

}
