package fr.bgoodes.confutil;

import fr.bgoodes.confutil.exceptions.ConfigInstantiationException;
import fr.bgoodes.confutil.storage.Storage;
import fr.bgoodes.confutil.storage.YMLStorage;

import java.io.File;

public class Test {
    public static void main(String[] args) throws ConfigInstantiationException {
        TestConfig config = ConfigFactory.getInstance(TestConfig.class);

        File file = new File("test.yml");
        YMLStorage storage = new YMLStorage(file);

        config.load(storage);

        config.setIntValue(config.getIntValue() + 1);
        config.setBooleanValue(!config.getBooleanValue());
        config.setStringValue("Hello World!");

        System.out.println("### Result: ");
        System.out.println(config.getStringValue());
        System.out.println(config.getIntValue());
        System.out.println(config.getIntValue2());
        System.out.println(config.getBooleanValue());

        config.save(storage);
    }
}
