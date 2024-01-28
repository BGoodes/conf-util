package fr.bgoodes.confutil.holders.impl;

import fr.bgoodes.confutil.holders.model.AbstractHolder;

public class StringHolder extends AbstractHolder {

    public StringHolder() {}

    @Override
    public String serialize(Object o) {
        return (String) o;
    }

    @Override
    public Object deserialize(String s) {
        return s;
    }

}
