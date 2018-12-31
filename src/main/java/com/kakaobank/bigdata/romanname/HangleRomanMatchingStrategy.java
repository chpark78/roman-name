package com.kakaobank.bigdata.romanname;

import java.util.List;

public interface HangleRomanMatchingStrategy extends Dictionary.Listener {

  List<MatchedEntry> match(String hangleName, String romanName);

}
