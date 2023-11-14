package fr.bgoodes.confutil;

import fr.bgoodes.confutil.holders.BooleanHolder;
import fr.bgoodes.confutil.holders.IntegerHolder;
import fr.bgoodes.confutil.holders.OptionHolder;
import fr.bgoodes.confutil.holders.StringHolder;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ConfUtil {

    private static final Map<Class<?>, Class<? extends OptionHolder>> HOLDERS = new HashMap<>();
    private static final Map<Class<?>, Object> DEFAULT_PRIMITIVE_VALUES = new HashMap<>();

    static {
        registerHolders();
        registerDefaultPrimitiveValues();
    }

    private ConfUtil() {}

    private static void registerHolders() {
        registerHolder(String.class, StringHolder.class);
        registerHolder(Integer.class, IntegerHolder.class);
        registerHolder(int.class, IntegerHolder.class);
        registerHolder(Boolean.class, BooleanHolder.class);
        registerHolder(boolean.class, BooleanHolder.class);
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

    private static void registerHolder(Class<?> clazz, Class<? extends OptionHolder> holder) {
        HOLDERS.put(clazz, holder);
    }

    //TODO: clean this mess
    public static OptionHolder getHolder(Class<?> clazz) {
        try {
            return HOLDERS.get(clazz).getConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean isPrimitive(Class<?> clazz) {
        return DEFAULT_PRIMITIVE_VALUES.containsKey(clazz);
    }

    public static Object getDefaultPrimitiveValue(Class<?> clazz) {
        return DEFAULT_PRIMITIVE_VALUES.get(clazz);
    }
}
