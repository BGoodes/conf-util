package fr.bgoodes.confutil.holders;

public class StringHolder extends OptionHolder {

    public StringHolder() {}
    public StringHolder(String key) {
        super(key);
    }

    @Override
    public String serialize(Object o) {
        return (String) o;
    }

    @Override
    public Object deserialize(String s) {
        return s;
    }

}
