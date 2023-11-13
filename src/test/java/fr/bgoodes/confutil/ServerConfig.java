package fr.bgoodes.confutil;

import fr.bgoodes.confutil.Config;
import fr.bgoodes.confutil.Option;

public interface ServerConfig extends Config {

    @Option(key = "default-language-code", defaultValue = "fr")
    String getDefaultLanguageCode();
    void setDefaultLanguageCode(String value);

    @Option(key = "lobby-settings.default-gamemode")
    String getDefaultGamemode();
}
