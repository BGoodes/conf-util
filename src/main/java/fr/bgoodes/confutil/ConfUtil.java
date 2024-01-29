package fr.bgoodes.confutil;

import fr.bgoodes.confutil.holders.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ConfUtil {

    private static final Map<Class<?>, Object> DEFAULT_PRIMITIVE_VALUES = new HashMap<>();

    private ConfUtil() {}

    static {
        registerDefaultPrimitiveValues();
    }

    private static void registerDefaultPrimitiveValues() {
        DEFAULT_PRIMITIVE_VALUES.put(byte.class, (byte) 0);
        DEFAULT_PRIMITIVE_VALUES.put(short.class, (short)0);
        DEFAULT_PRIMITIVE_VALUES.put(int.class, 0);
        DEFAULT_PRIMITIVE_VALUES.put(long.class, 0L);
        DEFAULT_PRIMITIVE_VALUES.put(float.class, 0.0f);
        DEFAULT_PRIMITIVE_VALUES.put(double.class, 0.0d);
        DEFAULT_PRIMITIVE_VALUES.put(boolean.class, Boolean.FALSE);
        DEFAULT_PRIMITIVE_VALUES.put(char.class, '\u0000');
    }

    public static boolean isPrimitive(Class<?> clazz) {
        return DEFAULT_PRIMITIVE_VALUES.containsKey(clazz);
    }

    public static Object getDefaultPrimitiveValue(Class<?> clazz) {
        return DEFAULT_PRIMITIVE_VALUES.get(clazz);
    }
}
