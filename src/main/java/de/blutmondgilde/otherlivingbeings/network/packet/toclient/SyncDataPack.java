package de.blutmondgilde.otherlivingbeings.network.packet.toclient;

import de.blutmondgilde.otherlivingbeings.data.jobs.farmer.FarmerDataProvider;
import de.blutmondgilde.otherlivingbeings.data.jobs.lumberjack.LumberjackDataProvider;
import de.blutmondgilde.otherlivingbeings.data.jobs.miner.MinerDataProvider;
import lombok.AllArgsConstructor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.common.registry.GameRegistry;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

@AllArgsConstructor
public class SyncDataPack {
    private final Map<Block, Float> expMap;
    private final Type type;

    public static void encode(SyncDataPack packet, FriendlyByteBuf buffer) {
        buffer.writeMap(packet.expMap, (friendlyByteBuf, block) -> friendlyByteBuf.writeResourceLocation(block.getRegistryName()), FriendlyByteBuf::writeFloat);
        buffer.writeEnum(packet.type);
    }

    public static SyncDataPack decode(FriendlyByteBuf buffer) {
        return new SyncDataPack(buffer
                .readMap(friendlyByteBuf -> GameRegistry.findRegistry(Block.class).getValue(friendlyByteBuf.readResourceLocation()),
                        FriendlyByteBuf::readFloat), buffer.readEnum(Type.class));
    }

    public static void handle(final SyncDataPack packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> UpdateDataPack.update(packet.expMap, packet.type)));
        context.get().setPacketHandled(true);
    }

    private static class UpdateDataPack {
        private static DistExecutor.SafeRunnable update(Map<Block, Float> expMap, Type type) {
            return () -> type.apply.accept(expMap);
        }
    }

    @AllArgsConstructor
    public enum Type {
        Lumberjack(expMap -> LumberjackDataProvider.setExpMap(new HashMap<>(expMap))),
        Miner(expMap -> MinerDataProvider.setExpMap(new HashMap<>(expMap))),
        Farmer(expMap -> FarmerDataProvider.setExpMap(new HashMap<>(expMap)));

        private final Consumer<Map<Block, Float>> apply;
    }
}
