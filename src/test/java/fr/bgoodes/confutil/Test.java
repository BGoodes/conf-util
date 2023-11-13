package fr.bgoodes.confutil;

public class Test {
    public static void main(String[] args) {
        ServerConfig config = ConfigFactory.getInstance(ServerConfig.class);
        config.setDefaultLanguageCode("fr");
        System.out.println("RESULT : " + config.getDefaultLanguageCode());
    }
}
