package fr.bgoodes.confutil.storage;

import fr.bgoodes.confutil.holders.OptionHolder;

import java.util.Collection;

public interface Storage {
    void load(Collection<OptionHolder> options);
    void save(Collection<OptionHolder> options);
}