package fr.bgoodes.confutil.holders;

import fr.bgoodes.confutil.exceptions.DeserializationException;

public class IntegerHolder extends OptionHolder {

    public IntegerHolder() {
    }

    @Override
    public String serialize(Object o) {
        if (o == null) return null;
        return o.toString();
    }

    @Override
    public Object deserialize(String s) throws DeserializationException {
        if (s == null || s.isEmpty()) return null;

        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new DeserializationException("Cannot deserialize integer value: " + s);
        }
    }

    @Override
    public void setValue(Object value) {
        if (value != null && !(value instanceof Integer)) throw new IllegalArgumentException("Only Integer values are allowed");
        super.setValue(value);
    }
}
