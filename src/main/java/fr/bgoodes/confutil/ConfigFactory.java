package fr.bgoodes.confutil;

import fr.bgoodes.confutil.exceptions.ConfigInstantiationException;
import fr.bgoodes.confutil.exceptions.DeserializationException;
import fr.bgoodes.confutil.exceptions.NoOptionMethodsFoundException;
import fr.bgoodes.confutil.holders.BooleanHolder;
import fr.bgoodes.confutil.holders.IntegerHolder;
import fr.bgoodes.confutil.holders.OptionHolder;
import fr.bgoodes.confutil.holders.StringHolder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ConfigFactory {
    private static final Pattern GETTER_PATTERN = Pattern.compile("^(get|is)[A-Z][a-zA-Z0-9]*$");

    private ConfigFactory() {}
    public static <T extends Config> T getInstance(Class<T> configClass) throws ConfigInstantiationException {
        try {
            return createInstance(configClass);
        } catch (NoOptionMethodsFoundException | DeserializationException e) {
            throw new ConfigInstantiationException("Failed to create instance for config class " + configClass.getName() + ": " + e.getMessage());
        }
    }


    private static <T extends Config> T createInstance(Class<T> configClass) throws NoOptionMethodsFoundException, DeserializationException {
        List<Method> getters = findGetters(configClass);
        Map<Method, Method> options = findMatchingSetters(configClass, getters);

        Map<Method, OptionHolder> gettersMap = new HashMap<>();
        Map<Method, OptionHolder> settersMap = new HashMap<>();

        fillMaps(options, gettersMap, settersMap);

        return new ConfigProxyHandler(gettersMap, settersMap).getInstance(configClass);
    }

    private static List<Method> findGetters(Class<?> configClass) throws NoOptionMethodsFoundException {
        ArrayList<Method> methods = new ArrayList<>();

        for (Method m : configClass.getDeclaredMethods()) {
            if (GETTER_PATTERN.matcher(m.getName()).matches()) {
                //TODO : check if "is" prefix is used for boolean.
                if (m.isAnnotationPresent(Option.class)) {
                    methods.add(m);
                }
            }
        }

        if (methods.isEmpty()) throw new NoOptionMethodsFoundException(configClass.getName());
        return methods;
    }

    private static Map<Method, Method> findMatchingSetters(Class<?> configClass, List<Method> getters) {
        Map<Method, Method> setters = new HashMap<>();

        for (Method g : getters) {
            String optionName = getOptionName(g);
            Method s = null;

            try {
                //TODO: check if "is" prefix is used for boolean. If so, don't use "set" prefix.
                s = configClass.getMethod("set" + optionName, g.getReturnType());
            } catch (NoSuchMethodException ignored) {}

            setters.put(g, s);
        }

        return setters;
    }

    //TODO: improve this function
    private static String getOptionName(Method getter) {
        return getter.getName().replace("get", "");
    }

    private static void fillMaps(Map<Method, Method> options, Map<Method, OptionHolder> gettersMap, Map<Method, OptionHolder> settersMap) throws DeserializationException {
        for (Method g : options.keySet()) {
            // Instantiate holder
            OptionHolder optionHolder = ConfUtil.getHolder(g.getReturnType());

            //TODO: clean this
            if (optionHolder == null)
                continue;

            // Set default value
            Option option = g.getAnnotation(Option.class);
            optionHolder.setValue(optionHolder.deserialize(option.defaultValue()));
            optionHolder.setKey(option.key());

            // Add getter to gettersMap
            gettersMap.put(g, optionHolder);

            // Add setter to settersMap
            Method s = options.get(g);
            if (s != null) settersMap.put(s, optionHolder);
        }
    }
}
