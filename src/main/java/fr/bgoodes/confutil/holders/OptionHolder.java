package fr.bgoodes.confutil.holders;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class OptionHolder {

    protected static final Map<Class<?>, Class<? extends OptionHolder>> HOLDERS = new HashMap<>();

    static {
        HOLDERS.put(String.class, StringHolder.class);
    }

    private Object value;
    private String key;

    protected OptionHolder() {}

    public OptionHolder(String key) {
        this.key = key;
        this.value = null;
    }

    public static OptionHolder getHolder(Class<?> clazz) {
        try {
            return HOLDERS.get(clazz).getConstructor().newInstance();
        } catch (NoSuchMethodException  | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public abstract String serialize(Object o);

    public abstract Object deserialize(String s);

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
