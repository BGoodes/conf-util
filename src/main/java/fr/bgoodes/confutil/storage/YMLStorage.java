package fr.bgoodes.confutil.storage;

import fr.bgoodes.confutil.holders.OptionHolder;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class YMLStorage implements Storage {
    private final File file;

    public YMLStorage(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    @Override
    public void load(Collection<OptionHolder> options) {
        InputStream in;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            //TODO: throw exception if file not exists
            e.printStackTrace();
            return;
        }

        loadFromStream(in, options);
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

        OutputStream out;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            //TODO: throw exception if file not exists
            e.printStackTrace();
            return;
        }

        saveToStream(out, options);
    }

    private void loadFromStream(InputStream in, Collection<OptionHolder> options) {
        Yaml yaml = new Yaml();
        Map<String, Object> map = yaml.load(in);

        if (map != null)
            for (OptionHolder option : options) {
                Object value = findValueInMap(map, option.getKey());
                option.setValue(value);
            }
    }

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

    private void saveToStream(OutputStream out, Collection<OptionHolder> options) {
        Yaml yaml = new Yaml();
        Map<String, Object> map = new HashMap<>();

        for (OptionHolder option : options)
            insertValueInMap(map, option.getKey(), option.getValue());

        try {
            yaml.dump(map, new OutputStreamWriter(out));
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                //TODO: improve exception handling
                e.printStackTrace();
            }
        }
    }

    private void insertValueInMap(Map<String, Object> map, String key, Object value) {
        String[] parts = key.split("\\.");
        Map<String, Object> currentMap = map;

        for (int i = 0; i < parts.length - 1; i++) {
            currentMap = (Map<String, Object>) currentMap.computeIfAbsent(parts[i], k -> new HashMap<>());
        }

        currentMap.put(parts[parts.length - 1], value);
    }

}
