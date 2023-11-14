package fr.bgoodes.confutil;

public interface TestConfig extends Config {

    @Option(key = "string")
    String getStringValue();
    void setStringValue(String value);

    @Option(key = "int")
    int getIntValue();
    void setIntValue(int value);

    @Option(key = "test.int2")
    int getIntValue2();

    @Option(key = "test.boolean")
    boolean getBooleanValue();
    void setBooleanValue(boolean value);
}
