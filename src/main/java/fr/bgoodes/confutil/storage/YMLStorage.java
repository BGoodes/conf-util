package fr.bgoodes.confutil.storage;

import fr.bgoodes.confutil.exceptions.DeserializationException;
import fr.bgoodes.confutil.holders.OptionHolder;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public record YMLStorage(File file) implements Storage {

    private static final DumperOptions DUMPER_OPTIONS = new DumperOptions();
    private static final Yaml YAML = new Yaml(DUMPER_OPTIONS);
    private static final Logger LOGGER = Logger.getLogger(YMLStorage.class.getName());

    static {
        DUMPER_OPTIONS.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        DUMPER_OPTIONS.setPrettyFlow(true);
    }

    @Override
    public void load(Collection<OptionHolder> options) {
        try (InputStream in = new FileInputStream(file)) {
            loadFromStream(in, options);
        } catch (IOException e) {
            throw new RuntimeException("Error loading from file", e);
        }
    }

    @Override
    public void save(Collection<OptionHolder> options) {
        //TODO: improve exception handling
        if (!file.exists() || !file.isFile()) {
            if (file.getParentFile().exists())
                throw new RuntimeException("The path is not a directory " + file.getParentFile().getAbsolutePath());
            if (!file.getParentFile().mkdirs())
                throw new RuntimeException("Cannot create directory " + file.getParentFile().getAbsolutePath());
        }

        try (OutputStream out = new FileOutputStream(file)) {
            saveToStream(out, options);
        } catch (IOException e) {
            throw new RuntimeException("Error saving to file", e);
        }
    }

    private void loadFromStream(InputStream in, Collection<OptionHolder> options) throws IOException {
        Map<String, Object> map = YAML.load(in);

        if (map != null) {
            for (OptionHolder option : options) {
                Object value = findValueInMap(map, option.getKey());
                if (value != null) {
                    try {
                        option.setValue(option.deserialize(value.toString()));
                    } catch (DeserializationException e) {
                        LOGGER.log(Level.WARNING, "Failed to deserialize option: " + option.getKey(), e);
                    }
                } else {
                    LOGGER.log(Level.INFO, "Option not found in YAML: " + option.getKey());
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Object findValueInMap(Map<String, Object> map, String key) {
        String[] parts = key.split("\\.");
        Map<String, Object> currentMap = map;

        for (int i = 0; i < parts.length - 1; i++) {
            Object part = currentMap.get(parts[i]);

            if (!(part instanceof Map))
                return null;

            currentMap = (Map<String, Object>) part;
        }

        return currentMap.get(parts[parts.length - 1]);
    }

    private void saveToStream(OutputStream out, Collection<OptionHolder> options) throws IOException {
        Map<String, Object> map = new HashMap<>();

        for (OptionHolder option : options) {
            String serializedValue = option.serialize(option.getValue());
            insertValueInMap(map, option.getKey(), serializedValue);
        }

        YAML.dump(map, new OutputStreamWriter(out));
    }

    @SuppressWarnings("unchecked")
    private void insertValueInMap(Map<String, Object> map, String key, Object value) {
        String[] parts = key.split("\\.");
        Map<String, Object> currentMap = map;

        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            currentMap = (Map<String, Object>) currentMap.computeIfAbsent(part, k -> new HashMap<>());
        }

        currentMap.put(parts[parts.length - 1], value);
    }
}
