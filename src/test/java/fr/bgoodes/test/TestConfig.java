package fr.bgoodes.test;

import fr.bgoodes.confutil.Config;
import fr.bgoodes.confutil.Option;

public interface TestConfig extends Config {

    @Option(key = "test.string", defaultValue = "hello world!")
    String getString();
    void setString(String value);


    @Option(key = "test.int", defaultValue = "42")
    int getInt();
    void setInt(int value);

    @Option(key = "test.int2")
    int getInt2();
    void setInt2(int value);

    @Option(key = "test.boolean", defaultValue = "true")
    boolean isBoolean();
    void setBoolean(boolean value);
}
