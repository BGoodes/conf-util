package fr.bgoodes.confutil;

import fr.bgoodes.confutil.holders.OptionHolder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

public class ConfigProxyHandler implements InvocationHandler {

    private final Map<Method, OptionHolder<?>> gettersMap;
    private final Map<Method, OptionHolder<?>> settersMap;

    public ConfigProxyHandler(Map<Method, OptionHolder<?>> gettersMap, Map<Method, OptionHolder<?>> settersMap) {
        this.gettersMap = gettersMap;
        this.settersMap = settersMap;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        if (gettersMap.containsKey(method)) return gettersMap.get(method).getValue();
        settersMap.get(method).setValue(args[0]);
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends Config> T getInstance(Class<T> configClass) {
        return (T) Proxy.newProxyInstance(configClass.getClassLoader(), new Class<?>[]{configClass}, this);
    }
}
