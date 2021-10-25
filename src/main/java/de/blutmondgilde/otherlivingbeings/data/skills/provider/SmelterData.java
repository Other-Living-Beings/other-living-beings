package de.blutmondgilde.otherlivingbeings.data.skills.provider;

import com.google.gson.JsonElement;
import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.data.skills.ExpEntryDataGenerator;
import de.blutmondgilde.otherlivingbeings.data.skills.ReloadableJobDataProvider;
import de.blutmondgilde.otherlivingbeings.data.skills.pojo.ItemExpEntry;
import de.blutmondgilde.otherlivingbeings.network.OtherLivingBeingNetwork;
import de.blutmondgilde.otherlivingbeings.network.packet.toclient.SyncItemDataPackPacket;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public class SmelterData {
    private static final String name = "smelter";
    private static final String type = OtherLivingBeings.MOD_ID + ":" + name + "_items";

    public static class Provider extends ReloadableJobDataProvider {
        @Setter
        @Getter
        private static HashMap<Item, ItemExpEntry> expMap = new HashMap<>();

        public Provider() {
            super(name);
        }

        @Override
        protected void apply(ResourceLocation fileLocation, JsonElement jsonElement) {
            ItemExpEntry data = new ItemExpEntry().fromJson(jsonElement.toString());
            if (data.type.equals(type)) expMap.put(data.item, data);
        }

        @Override
        public void sync() {
            OtherLivingBeingNetwork.getInstance().send(PacketDistributor.ALL.noArg(), new SyncItemDataPackPacket(expMap, SyncItemDataPackPacket.Type.Smelter));
        }

        @Override
        public void sync(ServerPlayer player) {
            OtherLivingBeingNetwork.getInstance().send(PacketDistributor.PLAYER.with(() -> player), new SyncItemDataPackPacket(expMap, SyncItemDataPackPacket.Type.Smelter));
        }
    }

    public static class DataGenerator extends ExpEntryDataGenerator<ItemExpEntry> {
        public DataGenerator(String modId, Path outputFolder) {
            super(modId, name, outputFolder);
        }

        @Override
        public void run(HashCache cache) throws IOException {

            for (ItemExpEntry entry : ItemExpEntry.fromItems(type, 1.0F,
                    Items.BAKED_POTATO,
                    Items.COOKED_BEEF,
                    Items.COOKED_CHICKEN,
                    Items.COOKED_COD,
                    Items.COOKED_MUTTON,
                    Items.COOKED_PORKCHOP,
                    Items.COOKED_RABBIT,
                    Items.COOKED_SALMON,
                    Items.DRIED_KELP,
                    Items.COPPER_INGOT,
                    Items.IRON_INGOT,
                    Items.GOLD_INGOT,
                    Items.NETHERITE_SCRAP,
                    Items.GLASS,
                    Items.STONE,
                    Items.SMOOTH_SANDSTONE,
                    Items.SMOOTH_RED_SANDSTONE,
                    Items.SMOOTH_STONE,
                    Items.SMOOTH_QUARTZ,
                    Items.BRICK,
                    Items.NETHER_BRICK,
                    Items.CRACKED_NETHER_BRICKS,
                    Items.SMOOTH_BASALT,
                    Items.TERRACOTTA,
                    Items.CRACKED_STONE_BRICKS,
                    Items.CRACKED_POLISHED_BLACKSTONE_BRICKS,
                    Items.DEEPSLATE,
                    Items.CRACKED_DEEPSLATE_BRICKS,
                    Items.CRACKED_DEEPSLATE_TILES,
                    Items.GRAY_GLAZED_TERRACOTTA,
                    Items.BLACK_GLAZED_TERRACOTTA,
                    Items.BLUE_GLAZED_TERRACOTTA,
                    Items.BROWN_GLAZED_TERRACOTTA,
                    Items.CYAN_GLAZED_TERRACOTTA,
                    Items.GREEN_GLAZED_TERRACOTTA,
                    Items.LIME_GLAZED_TERRACOTTA,
                    Items.MAGENTA_GLAZED_TERRACOTTA,
                    Items.ORANGE_GLAZED_TERRACOTTA,
                    Items.PINK_GLAZED_TERRACOTTA,
                    Items.PURPLE_GLAZED_TERRACOTTA,
                    Items.RED_GLAZED_TERRACOTTA,
                    Items.WHITE_GLAZED_TERRACOTTA,
                    Items.YELLOW_GLAZED_TERRACOTTA,
                    Items.LIGHT_BLUE_GLAZED_TERRACOTTA,
                    Items.LIGHT_GRAY_GLAZED_TERRACOTTA,
                    Items.GREEN_DYE,
                    Items.CHARCOAL,
                    Items.POPPED_CHORUS_FRUIT,
                    Items.LIME_DYE
                    )) {
                generateWithKey(cache, entry.getItem().getRegistryName().getPath() + "_exp", entry);
            }
        }
    }
}
