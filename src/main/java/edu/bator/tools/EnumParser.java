package edu.bator.tools;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class EnumParser<T extends Enum> {

  private Class<T> clazz;

  public EnumParser(Class<T> clazz) {
    this.clazz = clazz;
  }

  public T parse(String str) {
    for (T enumConstant : clazz.getEnumConstants()) {
      if (StringUtils.containsIgnoreCase(str, enumConstant.name())) {
        return enumConstant;
      }
    }
    return null;
  }

  public Set<T> parseMany(String str) {
    HashSet<T> result = new HashSet<>();
    for (T enumConstant : clazz.getEnumConstants()) {
      if (StringUtils.containsIgnoreCase(str, enumConstant.name())) {
        result.add(enumConstant);
      }
    }
    ;
    return result;
  }
}
