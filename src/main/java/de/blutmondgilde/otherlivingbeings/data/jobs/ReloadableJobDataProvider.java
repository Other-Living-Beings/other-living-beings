package de.blutmondgilde.otherlivingbeings.data.jobs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.util.GsonBlockTypeAdapter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;

import java.util.Map;

public abstract class ReloadableJobDataProvider extends SimpleJsonResourceReloadListener {
    protected static final Gson GSON = (new GsonBuilder())
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .registerTypeAdapter(Block.class, new GsonBlockTypeAdapter())
            .create();
    private final String jobName;

    public ReloadableJobDataProvider(String job) {
        super(GSON, "jobs/" + job);
        this.jobName = job;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> elementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        for (Map.Entry<ResourceLocation, JsonElement> entry : elementMap.entrySet()) {
            apply(entry.getKey(), entry.getValue());
        }
        OtherLivingBeings.getLogger().info("Loaded {} {} data files", elementMap.size(), this.jobName);

        if(ServerLifecycleHooks.getCurrentServer()!=null) sync();
    }

    protected abstract void apply(ResourceLocation fileLocation, JsonElement jsonElement);

    public abstract void sync();

    public abstract void sync(ServerPlayer player);
}
