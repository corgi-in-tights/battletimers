package dev.reyaan.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;

public class ConfigHandler<T> {
    File file;
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    public ConfigHandler(File file) {
        this.file = file;
    }

    public T init(Class<T> clazz) {
        if (file.exists()) {
            try {
                return fromJson(clazz);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            return saveDefault(clazz);
        } catch (IOException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public T fromJson(Class<T> clazz) throws IOException {
        String result = Files.readString(file.toPath());
        return new Gson().fromJson(result, clazz);
    }

    public void toJson(T object) throws IOException {
        String result = gson.toJson(object);

        if(!file.exists()) file.createNewFile();
        var out = new FileOutputStream(file, false);

        out.write(result.getBytes());
        out.flush();
        out.close();
    }

    public T saveDefault(Class<T> clazz) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        T config = clazz.getDeclaredConstructor().newInstance();
        toJson(config);
        return config;
    }
}
