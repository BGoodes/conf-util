package fr.bgoodes.confutil.holders.impl;

import fr.bgoodes.confutil.exceptions.DeserializationException;
import fr.bgoodes.confutil.holders.model.AbstractHolder;

public class BooleanHolder extends AbstractHolder {
    private static final String[] TRUE_STRINGS = {"true", "1", "y", "yes", "on"};
    private static final String[] FALSE_STRINGS = {"false", "0", "n", "no", "off"};

    public BooleanHolder() {}

    @Override
    public String serialize(Object o) {
        if (o == null) return null;
        return (Boolean) o ? TRUE_STRINGS[0] : FALSE_STRINGS[0];
    }

    @Override
    public Object deserialize(String s) throws DeserializationException {
        if (s == null || s.isEmpty()) return null;

        for (String yes : TRUE_STRINGS)
            if (yes.equalsIgnoreCase(s)) return Boolean.TRUE;
        for (String no : FALSE_STRINGS)
            if (no.equalsIgnoreCase(s)) return Boolean.FALSE;

        throw new DeserializationException("Cannot deserialize boolean value: " + s);
    }
}

