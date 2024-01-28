package fr.bgoodes.confutil.holders.impl;

import fr.bgoodes.confutil.exceptions.DeserializationException;
import fr.bgoodes.confutil.holders.model.AbstractHolder;

public class IntegerHolder extends AbstractHolder {

    public IntegerHolder() {}

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
}
