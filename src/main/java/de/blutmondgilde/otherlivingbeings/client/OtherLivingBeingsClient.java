package de.blutmondgilde.otherlivingbeings.client;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.api.capability.OtherLivingBeingsCapability;
import de.blutmondgilde.otherlivingbeings.config.OtherLivingBeingsConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fmlclient.ConfigGuiHandler;

import java.util.Optional;

public class OtherLivingBeingsClient {
    public static void init() {
        //Create Config GUI for Client
        ModLoadingContext.get()
                .registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () -> new ConfigGuiHandler.ConfigGuiFactory((client, parent) -> AutoConfig.getConfigScreen(OtherLivingBeingsConfig.class, parent)
                        .get()));
    }

    public static void syncSkills(final CompoundTag tag, final int targetId) {
        OtherLivingBeings.getLogger().debug("Received Player Skills Sync Packet with {} values. Start Handling...", tag.size());

        final long startTime = System.currentTimeMillis();
        final ClientLevel level = Minecraft.getInstance().level;

        if (level == null) {
            OtherLivingBeings.getLogger().fatal("Exception while syncing Skills from Entity {}. ClientWorld is NULL!", targetId);
            return;
        }
        Optional<Entity> target = Optional.ofNullable(level.getEntity(targetId));
        target.ifPresent(entity -> entity.getCapability(OtherLivingBeingsCapability.PLAYER_SKILLS).ifPresent(playerSkills -> playerSkills.deserializeNBT(tag)));

        OtherLivingBeings.getLogger().debug("Applied Player Skill Sync Packet in {} ms", System.currentTimeMillis() - startTime);
    }
}
