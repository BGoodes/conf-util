package fr.bgoodes.confutil.holders;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HolderFactory {
    private static final Map<Class<?>, Class<? extends OptionHolder>> HOLDERS = new HashMap<>();
    private static final Logger LOGGER = Logger.getLogger(HolderFactory.class.getName());

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

    public static OptionHolder getHolder(Class<?> clazz) {
        // Check for enum type
        if (Enum.class.isAssignableFrom(clazz)) {
            return createEnumHolder(clazz.asSubclass(Enum.class));
        }

        // Retrieve the corresponding holder class
        Class<? extends OptionHolder> holderClass = HOLDERS.get(clazz);
        if (holderClass == null) {
            LOGGER.log(Level.WARNING, "No OptionHolder found for class: " + clazz.getName());
            return null;
        }

        // Attempt to create a new instance of the holder
        try {
            Constructor<? extends OptionHolder> constructor = holderClass.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.log(Level.SEVERE, "Error creating an instance of OptionHolder for class: " + clazz.getName(), e);
            return null;
        }
    }


    private static <T extends Enum<T>> OptionHolder createEnumHolder(Class<T> enumClass) {
        return new EnumHolder<>(enumClass);
    }
}
