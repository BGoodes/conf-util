package fr.bgoodes.confutil.test;

import fr.bgoodes.confutil.ConfigFactory;

public class Test {
    public static void main(String[] args) {
        ServerConfig config = ConfigFactory.getInstance(ServerConfig.class);
        System.out.println("RESULT : " + config.getDefaultLanguageCode());
    }
}
