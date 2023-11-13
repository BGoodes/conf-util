package fr.bgoodes.confutil;

import fr.bgoodes.confutil.holders.OptionHolder;

import java.beans.Introspector;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigFactory {

    private static final Pattern GETTER_PATTERN = Pattern.compile("^(get|is)[A-Z][a-zA-Z0-9]*$");

    private ConfigFactory() {}
    public static <T extends Config> T getInstance(Class<T> configClass) {

        T instance = null;
        try {
            instance = createInstance(configClass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return instance;
    }


    private static <T extends Config> T createInstance(Class<T> configClass) {
        List<Method> getters = findGetters(configClass);
        Map<Method, Method> options = findMatchingSetters(configClass, getters);

        Map<Method, OptionHolder> gettersMap = new HashMap<>();
        Map<Method, OptionHolder> settersMap = new HashMap<>();

        fillMaps(options, gettersMap, settersMap);

        return new ConfigProxyHandler(gettersMap, settersMap).getInstance(configClass);
    }

    private static void fillMaps(Map<Method, Method> options, Map<Method, OptionHolder> gettersMap, Map<Method, OptionHolder> settersMap) {
        for (Method g : options.keySet()) {
            // Add getter to gettersMap
            OptionHolder optionHolder = OptionHolder.getHolder(g.getReturnType());
            gettersMap.put(g, optionHolder);

            // Add setter to settersMap
            Method s = options.get(g);
            if (s != null) settersMap.put(s, optionHolder);
        }
    }

    private static List<Method> findGetters(Class<?> configClass) {
        ArrayList<Method> methods = new ArrayList<>();

        for (Method m : configClass.getDeclaredMethods()) {
            if (GETTER_PATTERN.matcher(m.getName()).matches()) {
                //TODO : check if "is" prefix is used for boolean.
                if (m.isAnnotationPresent(Option.class)) {
                    methods.add(m);
                }
            }
        }

        if (methods.isEmpty()) throw new IllegalArgumentException("No options found in class " + configClass.getName());
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
}
