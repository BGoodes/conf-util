package fr.bgoodes.confutil;

import fr.bgoodes.confutil.holders.OptionHolder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ConfigFactory {

    private static final Pattern GETTER_PATTERN = Pattern.compile("^(get|is)[A-Z][a-zA-Z0-9]*$");

    private ConfigFactory() {}
    public static <T extends Config> T getInstance(Class<T> configClass) {

        T instance = null;
        try {
            instance = createInstance(configClass);
        } catch (Exception ignored) {
        }

        return instance;
    }


    private static <T extends Config> T createInstance(Class<T> configClass) {
        List<Method> options = getOptions(configClass);
        Map<Method, OptionHolder<?>> optionsMap = getOptionsMap(options);

        return new ConfigProxyHandler(optionsMap).getInstance(configClass);
    }

    private static Map<Method, OptionHolder<?>> getOptionsMap(List<Method> options) {
        Map<Method, OptionHolder<?>> optionsMap = new HashMap<>();

        for (Method m : options) {
            OptionHolder<?> optionHolder = OptionHolder.getHolder(m.getReturnType());


            optionsMap.put(m, optionHolder);
        }

        return optionsMap;
    }

    private static List<Method> getOptions(Class<?> configClass) {
        ArrayList<Method> methods = new ArrayList<>();

        for (Method m : configClass.getDeclaredMethods()) {

            if (GETTER_PATTERN.matcher(m.getName()).matches()) {
                //TODO : check if "is" prefix is used for boolean.
                if (m.isAnnotationPresent(Option.class)) {
                    methods.add(m);

                    System.out.println(m.getName());
                }
            }
        }

        if (methods.isEmpty()) throw new IllegalArgumentException("No options found in class " + configClass.getName());
        return methods;
    }
}
