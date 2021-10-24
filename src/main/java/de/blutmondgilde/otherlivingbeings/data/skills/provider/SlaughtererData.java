package de.blutmondgilde.otherlivingbeings.data.skills.provider;

import com.google.gson.JsonElement;
import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.data.skills.ExpEntryDataGenerator;
import de.blutmondgilde.otherlivingbeings.data.skills.ReloadableJobDataProvider;
import de.blutmondgilde.otherlivingbeings.data.skills.pojo.BlockStateExpEntry;
import de.blutmondgilde.otherlivingbeings.data.skills.pojo.EntityExpEntry;
import de.blutmondgilde.otherlivingbeings.network.OtherLivingBeingNetwork;
import de.blutmondgilde.otherlivingbeings.network.packet.toclient.SyncDataPackPacket;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public class SlaughtererData {
    private static final String name = "slaughterer";
    private static final String type = OtherLivingBeings.MOD_ID + ":" + name + "_blocks";

    public static class Provider extends ReloadableJobDataProvider {
        @Setter
        @Getter
        private static HashMap<Block, BlockStateExpEntry> expMap = new HashMap<>();

        public Provider() {
            super(name);
        }

        @Override
        protected void apply(ResourceLocation fileLocation, JsonElement jsonElement) {
            BlockStateExpEntry data = new BlockStateExpEntry().fromJson(jsonElement.toString());
            if (data.type.equals(type)) expMap.put(data.block, data);
        }

        @Override
        public void sync() {
            OtherLivingBeingNetwork.getInstance().send(PacketDistributor.ALL.noArg(), new SyncDataPackPacket(expMap, SyncDataPackPacket.Type.Slaughterer));
        }

        @Override
        public void sync(ServerPlayer player) {
            OtherLivingBeingNetwork.getInstance().send(PacketDistributor.PLAYER.with(() -> player), new SyncDataPackPacket(expMap, SyncDataPackPacket.Type.Slaughterer));
        }
    }

    public static class DataGenerator extends ExpEntryDataGenerator<EntityExpEntry> {
        public DataGenerator(String modId, Path outputFolder) {
            super(modId, name, outputFolder);
        }

        @Override
        public void run(HashCache cache) throws IOException {

            for (EntityExpEntry entry : EntityExpEntry.fromEntityType(type, 1.0F,
                    EntityType.BAT,
                    EntityType.AXOLOTL,
                    EntityType.BLAZE,
                    EntityType.BEE,
                    EntityType.CAT,
                    EntityType.CAVE_SPIDER,
                    EntityType.CHICKEN,
                    EntityType.COD,
                    EntityType.COW,
                    EntityType.CREEPER,
                    EntityType.DOLPHIN,
                    EntityType.DONKEY,
                    EntityType.DROWNED,
                    EntityType.ELDER_GUARDIAN,
                    EntityType.ENDER_DRAGON,
                    EntityType.ENDERMAN,
                    EntityType.ENDERMITE,
                    EntityType.EVOKER,
                    EntityType.FOX,
                    EntityType.GHAST,
                    EntityType.GIANT,
                    EntityType.GLOW_SQUID,
                    EntityType.GOAT,
                    EntityType.GUARDIAN,
                    EntityType.HOGLIN,
                    EntityType.HORSE,
                    EntityType.HUSK,
                    EntityType.ILLUSIONER,
                    EntityType.IRON_GOLEM,
                    EntityType.LLAMA,
                    EntityType.MAGMA_CUBE,
                    EntityType.MOOSHROOM,
                    EntityType.MULE,
                    EntityType.OCELOT,
                    EntityType.PANDA,
                    EntityType.PARROT,
                    EntityType.PHANTOM,
                    EntityType.PIG,
                    EntityType.PIGLIN,
                    EntityType.PIGLIN_BRUTE,
                    EntityType.PILLAGER,
                    EntityType.PLAYER,
                    EntityType.POLAR_BEAR,
                    EntityType.PUFFERFISH,
                    EntityType.RABBIT,
                    EntityType.RAVAGER,
                    EntityType.SALMON,
                    EntityType.SHEEP,
                    EntityType.SHULKER,
                    EntityType.SILVERFISH,
                    EntityType.SKELETON,
                    EntityType.SKELETON_HORSE,
                    EntityType.SLIME,
                    EntityType.SNOW_GOLEM,
                    EntityType.SPIDER,
                    EntityType.SQUID,
                    EntityType.STRAY,
                    EntityType.STRIDER,
                    EntityType.TRADER_LLAMA,
                    EntityType.TROPICAL_FISH,
                    EntityType.TURTLE,
                    EntityType.VEX,
                    EntityType.VILLAGER,
                    EntityType.VINDICATOR,
                    EntityType.WANDERING_TRADER,
                    EntityType.WITCH,
                    EntityType.WITHER,
                    EntityType.WITHER_SKELETON,
                    EntityType.WOLF,
                    EntityType.ZOGLIN,
                    EntityType.ZOMBIE,
                    EntityType.ZOMBIE_HORSE,
                    EntityType.ZOMBIE_VILLAGER,
                    EntityType.ZOMBIFIED_PIGLIN)) {
                generateWithKey(cache, entry.getEntity().getRegistryName().getPath() + "_exp", entry);
            }
        }
    }
}
