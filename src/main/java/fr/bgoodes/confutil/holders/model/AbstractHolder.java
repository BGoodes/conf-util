package fr.bgoodes.confutil.holders.model;

import fr.bgoodes.confutil.OptionChangeListener;
import fr.bgoodes.confutil.exceptions.DeserializationException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractHolder {
    private Object value;
    private String key;
    private final List<OptionChangeListener> listeners;

    protected AbstractHolder() {
        this.listeners = new ArrayList<>();
    }

    public abstract String serialize(Object o);

    public abstract Object deserialize(String s) throws DeserializationException;

    public Object getValue() {
        return value;
    }

    public void setValue(Object newValue) {
        Object oldValue = this.value;
        if (notifyChange(oldValue, newValue))
            this.value = newValue;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void addListener(OptionChangeListener listener) {
        listeners.add(listener);
    }

    public Boolean notifyChange(Object oldValue, Object newValue) {
        for (OptionChangeListener listener : listeners) {
            if (!listener.onChange(key, oldValue, newValue))
                return false;
        }
        return true;
    }
}
