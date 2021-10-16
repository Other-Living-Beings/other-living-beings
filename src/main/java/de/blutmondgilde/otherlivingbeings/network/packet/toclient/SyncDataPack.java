package de.blutmondgilde.otherlivingbeings.network.packet.toclient;

import de.blutmondgilde.otherlivingbeings.data.jobs.lumberjack.LumberjackDataProvider;
import lombok.AllArgsConstructor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.common.registry.GameRegistry;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@AllArgsConstructor
public class SyncDataPack {
    private final Map<Block, Float> expMap;

    public static void encode(SyncDataPack packet, FriendlyByteBuf buffer) {
        buffer.writeMap(packet.expMap, (friendlyByteBuf, block) -> friendlyByteBuf.writeResourceLocation(block.getRegistryName()), FriendlyByteBuf::writeFloat);
    }

    public static SyncDataPack decode(FriendlyByteBuf buffer) {
        return new SyncDataPack(buffer
                .readMap(friendlyByteBuf -> GameRegistry.findRegistry(Block.class).getValue(friendlyByteBuf.readResourceLocation()),
                        FriendlyByteBuf::readFloat));
    }

    public static void handle(final SyncDataPack packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> UpdateDataPack.update(packet.expMap)));
        context.get().setPacketHandled(true);
    }

    private static class UpdateDataPack {
        private static DistExecutor.SafeRunnable update(Map<Block, Float> expMap) {
            return () -> LumberjackDataProvider.setExpMap(new HashMap<>(expMap));
        }
    }
}
