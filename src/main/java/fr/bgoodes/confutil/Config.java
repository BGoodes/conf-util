package fr.bgoodes.confutil;

import fr.bgoodes.confutil.exceptions.StorageException;
import fr.bgoodes.confutil.storage.Storage;

public interface Config {
    void load(Storage storage) throws StorageException;
    void save(Storage storage) throws StorageException;
}
