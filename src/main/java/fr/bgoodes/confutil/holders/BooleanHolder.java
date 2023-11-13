package fr.bgoodes.confutil.holders;

import fr.bgoodes.confutil.exceptions.DeserializationException;

public class BooleanHolder extends OptionHolder {
    private static final String[] TRUE_STRINGS = {"true", "1", "y", "yes", "on"};
    private static final String[] FALSE_STRINGS = {"false", "0", "n", "no", "off"};

    public BooleanHolder() {}

    @Override
    public String serialize(Object o) {
        if (o == null) return null;
        return (Boolean) o ? "true" : "false";
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

    @Override
    public void setValue(Object value) {
        if (value != null && !(value instanceof Boolean)) throw new IllegalArgumentException("Only Boolean values are allowed");
        super.setValue(value);
    }
}

