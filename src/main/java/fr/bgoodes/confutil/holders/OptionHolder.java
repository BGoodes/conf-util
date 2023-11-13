package fr.bgoodes.confutil.holders;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class OptionHolder<T> {

    protected static final Map<Class<?>, Class<? extends OptionHolder<?>>> HOLDERS = new HashMap<>();

    static {
        HOLDERS.put(String.class, StringHolder.class);
    }

    private T value;
    private String key;
    private T defaultValue;

    protected OptionHolder() {}
    protected OptionHolder(String key) {
        this(key, null);
    }

    public OptionHolder(String key, T defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public static OptionHolder<?> getHolder(Class<?> clazz) {
        try {
            return HOLDERS.get(clazz).getConstructor().newInstance();
        } catch (NoSuchMethodException  | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public abstract String serialize(T o);

    public abstract T deserialize(String s);

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public T getDefaultValue() {
        return defaultValue;
    }
}
