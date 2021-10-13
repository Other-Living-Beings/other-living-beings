package de.blutmondgilde.otherlivingbeings.client;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.api.capability.OtherLivingBeingsCapability;
import de.blutmondgilde.otherlivingbeings.config.OtherLivingBeingsConfig;
import de.blutmondgilde.otherlivingbeings.config.elements.TreeListEntry;
import de.blutmondgilde.otherlivingbeings.config.widget.BlockListWidget;
import de.blutmondgilde.otherlivingbeings.config.widget.BlockTextField;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import me.shedaniel.autoconfig.gui.registry.api.GuiRegistryAccess;
import me.shedaniel.autoconfig.util.Utils;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
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

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            BlockTextField blockWidget = new BlockTextField(
                    new TranslatableComponent(translationKey),
                    fieldValue.getRegistryName().toString(),
                    ENTRY_BUILDER.getResetButtonKey(),
                    () -> {
                        Block block = Utils.getUnsafely(configField, defaultBlockObject, Blocks.AIR);
                        return block.getRegistryName().toString();
                    }, s -> Utils.setUnsafely(configField, blockObject, GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(s))),
                    () -> {
                        if (configField.isAnnotationPresent(ConfigEntry.Gui.Tooltip.class)) {
                            int count = configField.getAnnotation(ConfigEntry.Gui.Tooltip.class).count();
                            TranslatableComponent[] tooltips = new TranslatableComponent[count];
                            for (int i = 0; i < count; i++) {
                                tooltips[i] = new TranslatableComponent(translationKey + "." + i);
                            }
                            return Optional.of(tooltips);
                        } else {
                            return Optional.empty();
                        }
                    }, configField.isAnnotationPresent(ConfigEntry.Gui.RequiresRestart.class));

            return List.of(blockWidget);
        }, Block.class);

        registry.registerPredicateProvider((translationKey, configField, fieldObject, defaultFieldObject, guiRegistryAccess) -> {
            List<Block> fieldValue = Utils.getUnsafely(configField, fieldObject, List.of(Blocks.AIR));

            return List.of(new BlockListWidget(
                    new TranslatableComponent(translationKey),
                    new ArrayList<>(fieldValue.stream()
                            .map(Block::getRegistryName).filter(Objects::nonNull)
                            .map(ResourceLocation::toString)
                            .distinct()
                            .toList()),
                    false,
                    null,
                    strings -> Utils.setUnsafely(configField, fieldObject, new ArrayList<>(strings
                            .stream()
                            .map(s -> GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(s)))
                            .distinct()
                            .toList())),
                    () -> {
                        List<Block> defaultBlockList = Utils.getUnsafely(configField, defaultFieldObject, List.of(Blocks.AIR));
                        return new ArrayList<>(defaultBlockList.stream()
                                .map(Block::getRegistryName)
                                .filter(Objects::nonNull)
                                .map(ResourceLocation::toString)
                                .distinct()
                                .toList());
                    },
                    ENTRY_BUILDER.getResetButtonKey(),
                    configField.isAnnotationPresent(ConfigEntry.Gui.RequiresRestart.class)
            ));
        }, isListOfType(Block.class));

        registry.registerTypeProvider((translationKey, configField, fieldObject, defaultFieldObject, guiRegistryAccess) -> {
            TreeListEntry fieldValue = Utils.getUnsafely(configField, fieldObject, new TreeListEntry());
            TreeListEntry defaultValue = Utils.getUnsafely(configField, defaultFieldObject, new TreeListEntry());

            return List.of(ENTRY_BUILDER
                            .startFloatField(new TranslatableComponent(translationKey + ".exp"), fieldValue.exp)
                            .setDefaultValue(() -> defaultValue.exp)
                            .setSaveConsumer((newValue) -> {
                                try {
                                    Field field = TreeListEntry.class.getField("exp");
                                    Utils.setUnsafely(field, fieldValue.exp, newValue);
                                } catch (NoSuchFieldException ex) {
                                    ex.printStackTrace();
                                }
                            })
                            .build(),
                    new BlockListWidget(
                            new TranslatableComponent(translationKey + ".blocks"),
                            new ArrayList<>(fieldValue.blocks.stream()
                                    .map(Block::getRegistryName).filter(Objects::nonNull)
                                    .map(ResourceLocation::toString)
                                    .distinct()
                                    .toList()),
                            false,
                            null,
                            strings -> Utils.setUnsafely(configField, fieldValue, new TreeListEntry(fieldValue.exp, new ArrayList<>(strings
                                    .stream()
                                    .map(s -> GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(s)))
                                    .distinct()
                                    .toList()))),
                            () -> {
                                List<Block> defaultBlockList = defaultValue.blocks;
                                return new ArrayList<>(defaultBlockList.stream()
                                        .map(Block::getRegistryName)
                                        .filter(Objects::nonNull)
                                        .map(ResourceLocation::toString)
                                        .distinct()
                                        .toList());
                            },
                            ENTRY_BUILDER.getResetButtonKey(),
                            configField.isAnnotationPresent(ConfigEntry.Gui.RequiresRestart.class)
                    )
            );
        }, TreeListEntry.class);
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

    private static Predicate<Field> isListOfType(Type... types) {
        return (field) -> {
            if (List.class.isAssignableFrom(field.getType()) && field.getGenericType() instanceof ParameterizedType) {
                Type[] args = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
                return args.length == 1 && Stream.of(types).anyMatch((type) -> Objects.equals(args[0], type));
            } else {
                return false;
            }
        };
    }

    @SuppressWarnings({"rawtypes"})
    private static List<AbstractConfigListEntry> getChildren(String translationKey, Class<?> fieldType, Object iConfig, Object iDefaults, GuiRegistryAccess guiProvider) {
        return Arrays.stream(fieldType.getDeclaredFields()).map((iField) -> {
            String iI13n = String.format("%s.%s", translationKey, iField.getName());
            return guiProvider.getAndTransform(iI13n, iField, iConfig, iDefaults, guiProvider);
        }).filter(Objects::nonNull).flatMap(Collection::stream).collect(Collectors.toList());
    }
}
