package fr.bgoodes.confutil;

import fr.bgoodes.confutil.holders.OptionHolder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

public class ConfigProxyHandler implements InvocationHandler {

    private final Map<Method, OptionHolder<?>> optionsMap;

    public ConfigProxyHandler(Map<Method, OptionHolder<?>> optionsMap) {
        this.optionsMap = optionsMap;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return optionsMap.get(method).getValue();
    }

    @SuppressWarnings("unchecked")
    public <T extends Config> T getInstance(Class<T> configClass) {
        return (T) Proxy.newProxyInstance(configClass.getClassLoader(), new Class<?>[]{configClass}, this);
    }
}
