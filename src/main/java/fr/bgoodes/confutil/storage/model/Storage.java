package fr.bgoodes.confutil.storage.model;

import fr.bgoodes.confutil.exceptions.StorageException;
import fr.bgoodes.confutil.holders.model.AbstractHolder;

import java.util.Collection;

public interface Storage {
    void load(Collection<AbstractHolder> options) throws StorageException;
    void save(Collection<AbstractHolder> options) throws StorageException;
}