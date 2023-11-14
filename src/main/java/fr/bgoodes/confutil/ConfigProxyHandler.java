package fr.bgoodes.confutil;

import fr.bgoodes.confutil.holders.OptionHolder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class ConfigProxyHandler implements InvocationHandler {

    private final Map<Method, Method> configMethods;
    private final Map<Method, OptionHolder> gettersMap;
    private final Map<Method, OptionHolder> settersMap;

    public ConfigProxyHandler(Map<Method, OptionHolder> gettersMap, Map<Method, OptionHolder> settersMap) {
        this.configMethods = getConfigMethods();
        this.gettersMap = gettersMap;
        this.settersMap = settersMap;
    }

    private Map<Method, Method> getConfigMethods() {
        Map<Method, Method> methods = new HashMap<>();

        try {
            methods.put(Config.class.getMethod("load"), getClass().getDeclaredMethod("load"));
            methods.put(Config.class.getMethod("save"), getClass().getDeclaredMethod("save"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return methods;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (configMethods.containsKey(method)) {
            try {
                return configMethods.get(method).invoke(this, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw e.getCause();
            }
        }

        if (gettersMap.containsKey(method)) return getValue(gettersMap.get(method), method.getReturnType());
        settersMap.get(method).setValue(args[0]);
        return null;
    }

    private Object getValue(OptionHolder option, Class<?> type) {
        return option.getValue() == null && ConfUtil.isPrimitive(type) ?
                ConfUtil.getDefaultPrimitiveValue(type) : option.getValue();
    }

    private void load() {
        System.out.println("Loading config...");
    }

    private void save() {
        System.out.println("Saving config...");
    }

    @SuppressWarnings("unchecked")
    public <T extends Config> T getInstance(Class<T> configClass) {
        return (T) Proxy.newProxyInstance(configClass.getClassLoader(), new Class<?>[]{configClass}, this);
    }
}
