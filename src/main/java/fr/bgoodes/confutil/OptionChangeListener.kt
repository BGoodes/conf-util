package fr.bgoodes.confutil;

public interface OptionChangeListener {
    boolean onChange(String key, Object oldValue, Object newValue);
}