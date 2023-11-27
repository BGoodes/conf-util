package fr.bgoodes.confutil;

import fr.bgoodes.confutil.exceptions.ConfigInstantiationException;
import fr.bgoodes.confutil.exceptions.DeserializationException;
import fr.bgoodes.confutil.exceptions.NoOptionMethodsFoundException;
import fr.bgoodes.confutil.holders.HolderFactory;
import fr.bgoodes.confutil.holders.OptionHolder;
import fr.bgoodes.confutil.storage.YMLStorage;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigFactory {
    private static final Logger LOGGER = Logger.getLogger(YMLStorage.class.getName());

    private ConfigFactory() {
    }

    public static <T extends Config> T getInstance(Class<T> configClass) throws ConfigInstantiationException {
        return getInstance(configClass, null);
    }

    public static <T extends Config> T getInstance(Class<T> configClass, Map<String, OptionChangeListener> listeners) throws ConfigInstantiationException {
        try {
            return createInstance(configClass, listeners);
        } catch (NoOptionMethodsFoundException e) {
            throw new ConfigInstantiationException("Failed to create instance for config class " + configClass.getName(), e);
        }
    }

    private static <T extends Config> T createInstance(Class<T> configClass, Map<String, OptionChangeListener> listeners) throws NoOptionMethodsFoundException {
        List<Method> getters = findGetters(configClass);
        Map<Method, Method> options = findMatchingSetters(configClass, getters);

        Map<Method, OptionHolder> gettersMap = new HashMap<>();
        Map<Method, OptionHolder> settersMap = new HashMap<>();

        fillMaps(options, gettersMap, settersMap);

        if (listeners != null)
            registerListeners(listeners, settersMap);

        return new ConfigProxyHandler(gettersMap, settersMap).getInstance(configClass);
    }

    private static List<Method> findGetters(Class<?> configClass) throws NoOptionMethodsFoundException {
        ArrayList<Method> methods = new ArrayList<>();

        for (Method m : configClass.getDeclaredMethods()) {
            if (isGetter(m)) {
                if (m.isAnnotationPresent(Option.class)) {
                    methods.add(m);
                }
            }
        }

        if (methods.isEmpty()) throw new NoOptionMethodsFoundException(configClass.getName());
        return methods;
    }

    private static boolean isGetter(Method method) {
        if (!method.isAnnotationPresent(Option.class))
            return false;

        if (method.getName().startsWith("get") && method.getParameterTypes().length == 0)
            return true;

        return method.getName().startsWith("is") && method.getReturnType().equals(boolean.class);
    }

    private static Map<Method, Method> findMatchingSetters(Class<?> configClass, List<Method> getters) {
        Map<Method, Method> setters = new HashMap<>();

        for (Method g : getters) {
            String optionName = getOptionName(g);
            Method s = null;

            try {
                s = findSetterMethod(configClass, g, optionName);
            } catch (NoSuchMethodException ignored) {}

            setters.put(g, s);
        }

        return setters;
    }

    private static Method findSetterMethod(Class<?> configClass, Method getter, String optionName) throws NoSuchMethodException {
        if (getter.getReturnType().equals(boolean.class) && getter.getName().startsWith("is"))
            return configClass.getMethod(optionName, getter.getReturnType());
        return configClass.getMethod("set" + optionName, getter.getReturnType());
    }

    private static String getOptionName(Method getter) {
        if (getter.getName().startsWith("is"))
            return getter.getName().substring(2);
        return getter.getName().substring(3);
    }

    private static void fillMaps(Map<Method, Method> options, Map<Method, OptionHolder> gettersMap, Map<Method, OptionHolder> settersMap) {
        for (Method getterMethod : options.keySet()) {
            OptionHolder optionHolder = createAndInitializeOptionHolder(getterMethod);
            if (optionHolder != null) {
                gettersMap.put(getterMethod, optionHolder);

                Method setterMethod = options.get(getterMethod);
                if (setterMethod != null) {
                    settersMap.put(setterMethod, optionHolder);
                }
            }
        }
    }

    private static void registerListeners(Map<String, OptionChangeListener> listeners, Map<Method, OptionHolder> settersMap) {
        for (OptionHolder o : settersMap.values()) {
            OptionChangeListener listener = listeners.get(o.getKey());
            if (listener != null)
                o.addListener(listener);
        }
    }

    private static OptionHolder createAndInitializeOptionHolder(Method method) {
        OptionHolder optionHolder = HolderFactory.getHolder(method.getReturnType());
        if (optionHolder == null) return null;

        Option option = method.getAnnotation(Option.class);
        try {
            Object deserializedValue = optionHolder.deserialize(option.defaultValue());
            optionHolder.setValue(deserializedValue);
        } catch (DeserializationException e) {
            LOGGER.log(Level.WARNING, "Cannot deserialize default value for option : " + option.key());
        }

        optionHolder.setKey(option.key());
        return optionHolder;
    }
}

