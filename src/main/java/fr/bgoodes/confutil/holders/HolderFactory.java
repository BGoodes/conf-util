package fr.bgoodes.confutil.holders;

import fr.bgoodes.confutil.holders.impl.BooleanHolder;
import fr.bgoodes.confutil.holders.impl.EnumHolder;
import fr.bgoodes.confutil.holders.impl.IntegerHolder;
import fr.bgoodes.confutil.holders.impl.StringHolder;
import fr.bgoodes.confutil.holders.model.AbstractHolder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HolderFactory {
    private static final Map<Class<?>, Class<? extends AbstractHolder>> HOLDERS = new HashMap<>();
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

    public static void registerHolder(Class<?> clazz, Class<? extends AbstractHolder> holder) {
        HOLDERS.put(clazz, holder);
    }

    public static AbstractHolder getHolder(Class<?> clazz) {
        // Check for enum type
        if (Enum.class.isAssignableFrom(clazz)) {
            return createEnumHolder(clazz.asSubclass(Enum.class));
        }

        // Retrieve the corresponding holder class
        Class<? extends AbstractHolder> holderClass = HOLDERS.get(clazz);
        if (holderClass == null) {
            LOGGER.log(Level.WARNING, "No OptionHolder found for class: " + clazz.getName());
            return null;
        }

        // Attempt to create a new instance of the holder
        try {
            Constructor<? extends AbstractHolder> constructor = holderClass.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.log(Level.SEVERE, "Error creating an instance of OptionHolder for class: " + clazz.getName(), e);
            return null;
        }
    }


    private static <T extends Enum<T>> AbstractHolder createEnumHolder(Class<T> enumClass) {
        return new EnumHolder<>(enumClass);
    }
}
