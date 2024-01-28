package fr.bgoodes.confutil.holders.impl;

import fr.bgoodes.confutil.exceptions.DeserializationException;
import fr.bgoodes.confutil.holders.model.AbstractHolder;

public class EnumHolder<T extends Enum<T>> extends AbstractHolder {

    private final Class<T> enumType;

    public EnumHolder(Class<T> enumType) {
        this.enumType = enumType;
    }

    @Override
    public String serialize(Object o) {
        if (o == null) return null;
        return ((Enum<?>) o).name();
    }

    @Override
    public Object deserialize(String s) throws DeserializationException {
        if (s == null || s.isEmpty()) return null;
        try {
            return Enum.valueOf(enumType, s);
        } catch (IllegalArgumentException e) {
            throw new DeserializationException("Cannot deserialize enum value: " + s);
        }
    }
}

