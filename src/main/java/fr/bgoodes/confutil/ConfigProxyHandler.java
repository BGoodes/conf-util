package fr.bgoodes.confutil;

import fr.bgoodes.confutil.exceptions.StorageException;
import fr.bgoodes.confutil.holders.OptionHolder;
import fr.bgoodes.confutil.storage.Storage;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
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
            methods.put(Config.class.getMethod("load", Storage.class), getClass().getDeclaredMethod("load", Storage.class));
            methods.put(Config.class.getMethod("save", Storage.class), getClass().getDeclaredMethod("save", Storage.class));
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Config method not found", e);
        }

        return methods;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (configMethods.containsKey(method)) {
            return configMethods.get(method).invoke(this, args);
        }

        if (gettersMap.containsKey(method)) {
            return getValue(gettersMap.get(method), method.getReturnType());
        }

        if (settersMap.containsKey(method)) {
            settersMap.get(method).setValue(args[0]);
            return null;
        }

        throw new UnsupportedOperationException("Method not supported: " + method);
    }

    private Object getValue(OptionHolder option, Class<?> type) {
        return option.getValue() == null && ConfUtil.isPrimitive(type) ?
                ConfUtil.getDefaultPrimitiveValue(type) : option.getValue();
    }

    private void load(Storage storage) throws StorageException {
       storage.load(gettersMap.values());
    }

    private void save(Storage storage) throws StorageException {
        storage.save(gettersMap.values());
    }

    @SuppressWarnings("unchecked")
    public <T extends Config> T getInstance(Class<T> configClass) {
        return (T) Proxy.newProxyInstance(configClass.getClassLoader(), new Class<?>[]{configClass}, this);
    }
}
