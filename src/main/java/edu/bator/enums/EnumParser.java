package edu.bator.enums;

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
}
