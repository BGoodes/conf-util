package fr.bgoodes.test;

import fr.bgoodes.confutil.ConfigFactory;
import fr.bgoodes.confutil.exceptions.ConfigInstantiationException;

public class ConfigTest {

    //TODO : create true unit tests

    public static void main(String[] args) throws ConfigInstantiationException {
        TestConfig configTest = ConfigFactory.getInstance(TestConfig.class);

        System.out.println(configTest.getString());
        System.out.println(configTest.getInt());
        System.out.println(configTest.getInt2());
        System.out.println(configTest.isBoolean());

        configTest.setString("test");
        configTest.setInt(1);

        System.out.println(configTest.getString());
        System.out.println(configTest.getInt());
    }
}
