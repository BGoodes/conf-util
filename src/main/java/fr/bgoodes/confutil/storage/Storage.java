package fr.bgoodes.confutil.storage;

import fr.bgoodes.confutil.exceptions.StorageException;
import fr.bgoodes.confutil.holders.OptionHolder;

import java.util.Collection;

public interface Storage {
    void load(Collection<OptionHolder> options) throws StorageException;
    void save(Collection<OptionHolder> options) throws StorageException;
}