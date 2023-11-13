package fr.bgoodes.confutil.holders;

public class StringHolder extends OptionHolder<String> {

    public StringHolder() {}
    public StringHolder(String key, String defaultValue) {
        super(key, defaultValue);
    }

    @Override
    public String serialize(String o) {
        return o;
    }

    @Override
    public String deserialize(String s) {
        return s;
    }

}
