package fr.bgoodes.confutil;

import fr.bgoodes.confutil.exceptions.ConfigInstantiationException;

public class Test {
    public static void main(String[] args) throws ConfigInstantiationException {
        TestConfig config = ConfigFactory.getInstance(TestConfig.class);

        config.setIntValue(42);
        config.setBooleanValue(true);
        config.setStringValue("Hello World!");

        System.out.println("### Result: ");
        System.out.println(config.getStringValue());
        System.out.println(config.getIntValue());
        System.out.println(config.getIntValue2());
        System.out.println(config.getBooleanValue());
    }
}
