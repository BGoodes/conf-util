package fr.bgoodes.confutil;

import fr.bgoodes.confutil.storage.Storage;

public interface Config {
    void load(Storage storage);
    void save(Storage storage);
}
