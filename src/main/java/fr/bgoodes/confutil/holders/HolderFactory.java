package fr.bgoodes.confutil.holders;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class HolderFactory {
    private static final Map<Class<?>, Class<? extends OptionHolder>> HOLDERS = new HashMap<>();

    static {
        registerDefaultsHolders();
    }

    private static void registerDefaultsHolders() {
        registerHolder(String.class, StringHolder.class);
        registerHolder(Integer.class, IntegerHolder.class);
        registerHolder(int.class, IntegerHolder.class);
        registerHolder(Boolean.class, BooleanHolder.class);
        registerHolder(boolean.class, BooleanHolder.class);
    }

    public static void registerHolder(Class<?> clazz, Class<? extends OptionHolder> holder) {
        HOLDERS.put(clazz, holder);
    }

    //TODO: clean this mess
    public static OptionHolder getHolder(Class<?> clazz) {

        //TODO: add exception if no holder found
        if (Enum.class.isAssignableFrom(clazz)) {
            return createEnumHolder(clazz.asSubclass(Enum.class));
        } else {
            try {
                return HOLDERS.get(clazz).getConstructor().newInstance();
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private static <T extends Enum<T>> OptionHolder createEnumHolder(Class<T> enumClass) {
        return new EnumHolder<>(enumClass);
    }
}
