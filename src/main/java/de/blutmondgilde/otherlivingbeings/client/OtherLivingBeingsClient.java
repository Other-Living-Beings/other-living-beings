package de.blutmondgilde.otherlivingbeings.client;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.api.capability.OtherLivingBeingsCapability;
import de.blutmondgilde.otherlivingbeings.config.OtherLivingBeingsConfig;
import de.blutmondgilde.otherlivingbeings.config.widget.BlockWidget;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import me.shedaniel.autoconfig.util.Utils;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.StringListEntry;
import me.shedaniel.clothconfig2.impl.builders.TextFieldBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fmlclient.ConfigGuiHandler;
import net.minecraftforge.fmllegacy.common.registry.GameRegistry;

import java.util.List;
import java.util.Optional;

public class OtherLivingBeingsClient {
    private static final ConfigEntryBuilder ENTRY_BUILDER = ConfigEntryBuilder.create();
    private static final Minecraft minecraft = Minecraft.getInstance();

    public static void init() {
        //Create Config GUI for Client
        ModLoadingContext.get()
                .registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () -> new ConfigGuiHandler.ConfigGuiFactory((client, parent) -> AutoConfig.getConfigScreen(OtherLivingBeingsConfig.class, parent)
                        .get()));
    }

    public static void registerConfigGUI(FMLClientSetupEvent e) {
        GuiRegistry registry = AutoConfig.getGuiRegistry(OtherLivingBeingsConfig.class);
        registry.registerTypeProvider((translationKey, configField, blockObject, defaultBlockObject, guiRegistryAccess) -> {
            Block fieldValue = Utils.getUnsafely(configField, blockObject, Blocks.AIR);

            TextFieldBuilder blockEntry = ENTRY_BUILDER.startTextField(new TranslatableComponent(translationKey), fieldValue.getRegistryName().toString())
                    .setDefaultValue(() -> {
                        Block block = Utils.getUnsafely(configField, defaultBlockObject, Blocks.AIR);
                        return block.getRegistryName().toString();
                    })
                    .setSaveConsumer(s -> Utils.setUnsafely(configField, blockObject, GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(s))));
            //Allow require restart
            if (configField.isAnnotationPresent(ConfigEntry.Gui.RequiresRestart.class)) {
                blockEntry.requireRestart();
            }
            //Allow Tooltips
            if (configField.isAnnotationPresent(ConfigEntry.Gui.Tooltip.class)) {
                int count = configField.getAnnotation(ConfigEntry.Gui.Tooltip.class).count();
                TranslatableComponent[] tooltips = new TranslatableComponent[count];
                for (int i = 0; i < count; i++) {
                    tooltips[i] = new TranslatableComponent(translationKey + "." + i);
                }
                blockEntry.setTooltip(Optional.of(tooltips));
            }

            StringListEntry textEntry = blockEntry.build();
            BlockWidget blockWidget = new BlockWidget(textEntry);

            return List.of(blockWidget);
        }, Block.class);
    }

    public static void syncSkills(final CompoundTag tag, final int targetId) {
        OtherLivingBeings.getLogger().debug("Received Player Skills Sync Packet with {} values. Start Handling...", tag.size());

        final long startTime = System.currentTimeMillis();
        final ClientLevel level = minecraft.level;

        if (level == null) {
            OtherLivingBeings.getLogger().fatal("Exception while syncing Skills from Entity {}. ClientWorld is NULL!", targetId);
            return;
        }
        Optional<Entity> target = Optional.ofNullable(level.getEntity(targetId));
        target.ifPresent(entity -> entity.getCapability(OtherLivingBeingsCapability.PLAYER_SKILLS).ifPresent(playerSkills -> playerSkills.deserializeNBT(tag)));

        OtherLivingBeings.getLogger().debug("Applied Player Skill Sync Packet in {} ms", System.currentTimeMillis() - startTime);
    }
}
