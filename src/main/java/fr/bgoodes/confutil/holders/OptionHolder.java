package fr.bgoodes.confutil.holders;

import fr.bgoodes.confutil.exceptions.DeserializationException;

public abstract class OptionHolder {

    private Object value;
    private String key;

    protected OptionHolder() {}

    public OptionHolder(String key) {
        this.key = key;
        this.value = null;
    }

    public abstract String serialize(Object o);

    public abstract Object deserialize(String s) throws DeserializationException;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }
}
