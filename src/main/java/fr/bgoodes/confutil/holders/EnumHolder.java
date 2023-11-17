package fr.bgoodes.confutil.holders;

import fr.bgoodes.confutil.exceptions.DeserializationException;

public class EnumHolder<T extends Enum<T>> extends OptionHolder {

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

    @Override
    public void setValue(Object value) {
        if (value != null && !enumType.isInstance(value))
            throw new IllegalArgumentException("Value must be an instance of " + enumType.getSimpleName());
        super.setValue(value);
    }
}

