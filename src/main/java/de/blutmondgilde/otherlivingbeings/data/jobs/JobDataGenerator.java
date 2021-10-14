package de.blutmondgilde.otherlivingbeings.data.jobs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.util.GsonBlockTypeAdapter;
import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.world.level.block.Block;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public abstract class JobDataGenerator<T extends GeneratableObject<T>> implements DataProvider {
    protected static final Gson GSON = (new GsonBuilder())
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .registerTypeAdapter(Block.class, new GsonBlockTypeAdapter())
            .create();

    @Getter(AccessLevel.PROTECTED)
    private final String modId, jobName;
    @Getter(AccessLevel.PROTECTED)
    private final Path outputFolder;
    private final Map<String, T> pojoMap = new HashMap<>();

    public JobDataGenerator(String modId, String jobName, Path outputFolder) {
        this.modId = modId;
        this.jobName = jobName;
        this.outputFolder = outputFolder.resolve("data").resolve(this.modId).resolve("jobs").resolve(this.jobName);
    }

    @Override
    public abstract void run(HashCache cache) throws IOException;

    @Override
    public String getName() {
        return this.modId + " " + this.jobName + " DataGenerator";
    }


    protected void generateWithKey(HashCache cache, String key, T pojoObject) throws IOException {
        getOutputFolder().toFile().mkdirs();
        if (pojoMap.containsKey(key)) {
            throw new IOException("A value with the key " + key + " already exists");
        } else {
            OtherLivingBeings.getLogger().info("Writing {} to File", pojoObject.toJson());
            DataProvider.save(GSON, cache, pojoObject.toJson(), this.outputFolder.resolve(key + ".json"));
            pojoMap.put(key, pojoObject);
        }
    }
}
