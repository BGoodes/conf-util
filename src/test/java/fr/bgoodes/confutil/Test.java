package fr.bgoodes.confutil;

import fr.bgoodes.confutil.exceptions.ConfigInstantiationException;

public class Test {
    public static void main(String[] args) throws ConfigInstantiationException {
        ServerConfig config = ConfigFactory.getInstance(ServerConfig.class);

        config.setDefaultLanguageCode("fr");
        System.out.println("RESULT : " + config.getDefaultLanguageCode());
    }
}
