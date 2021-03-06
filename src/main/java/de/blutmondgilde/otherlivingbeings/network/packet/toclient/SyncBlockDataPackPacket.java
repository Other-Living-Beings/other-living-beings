package de.blutmondgilde.otherlivingbeings.network.packet.toclient;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.data.skills.pojo.BlockStateExpEntry;
import de.blutmondgilde.otherlivingbeings.data.skills.provider.FarmerData;
import de.blutmondgilde.otherlivingbeings.data.skills.provider.LumberjackData;
import de.blutmondgilde.otherlivingbeings.data.skills.provider.MinerData;
import lombok.AllArgsConstructor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fmllegacy.common.registry.GameRegistry;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

@AllArgsConstructor
public class SyncBlockDataPackPacket {
    private final Map<Block, BlockStateExpEntry> expMap;
    private final Type type;

    public static void encode(SyncBlockDataPackPacket packet, FriendlyByteBuf buffer) {
        buffer.writeMap(packet.expMap, (friendlyByteBuf, block) -> friendlyByteBuf.writeResourceLocation(block.getRegistryName()), (friendlyByteBuf, blockStateExpEntry) -> friendlyByteBuf.writeUtf(blockStateExpEntry.toJson()
                .toString()));
        buffer.writeEnum(packet.type);
    }

    public static SyncBlockDataPackPacket decode(FriendlyByteBuf buffer) {
        return new SyncBlockDataPackPacket(buffer
                .readMap(friendlyByteBuf -> GameRegistry.findRegistry(Block.class).getValue(friendlyByteBuf.readResourceLocation()),
                        friendlyByteBuf -> {
                            BlockStateExpEntry value = new BlockStateExpEntry();
                            return value.fromJson(friendlyByteBuf.readUtf());
                        }), buffer.readEnum(Type.class));
    }

    public static void handle(final SyncBlockDataPackPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> OtherLivingBeings.getInstance().getProxy().updateDataProvider(packet.expMap, packet.type));
        context.get().setPacketHandled(true);
    }

    @AllArgsConstructor
    public enum Type {
        Lumberjack(expMap -> LumberjackData.Provider.setExpMap(new HashMap<>(expMap))),
        Miner(expMap -> MinerData.Provider.setExpMap(new HashMap<>(expMap))),
        Farmer(expMap -> FarmerData.Provider.setExpMap(new HashMap<>(expMap)));

        public final Consumer<Map<Block, BlockStateExpEntry>> apply;
    }
}
