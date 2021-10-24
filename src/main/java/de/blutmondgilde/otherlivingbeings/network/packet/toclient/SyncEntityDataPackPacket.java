package de.blutmondgilde.otherlivingbeings.network.packet.toclient;

import de.blutmondgilde.otherlivingbeings.data.skills.pojo.EntityExpEntry;
import de.blutmondgilde.otherlivingbeings.data.skills.provider.SlaughtererData;
import lombok.AllArgsConstructor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

@AllArgsConstructor
public class SyncEntityDataPackPacket {
    private final Map<EntityType<?>, EntityExpEntry> expMap;
    private final Type type;

    public static void encode(SyncEntityDataPackPacket packet, FriendlyByteBuf buffer) {
        buffer.writeMap(packet.expMap, (friendlyByteBuf, entity) -> friendlyByteBuf.writeResourceLocation(entity.getRegistryName()), (friendlyByteBuf, entityExpEntry) -> friendlyByteBuf.writeUtf(entityExpEntry.toJson()
                .toString()));
        buffer.writeEnum(packet.type);
    }

    public static SyncEntityDataPackPacket decode(FriendlyByteBuf buffer) {
        return new SyncEntityDataPackPacket(buffer
                .readMap(friendlyByteBuf -> ForgeRegistries.ENTITIES.getValue(friendlyByteBuf.readResourceLocation()),
                        friendlyByteBuf -> {
                            EntityExpEntry value = new EntityExpEntry();
                            return value.fromJson(friendlyByteBuf.readUtf());
                        }), buffer.readEnum(Type.class));
    }

    public static void handle(final SyncEntityDataPackPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> UpdateDataPack.update(packet.expMap, packet.type)));
        context.get().setPacketHandled(true);
    }

    private static class UpdateDataPack {
        private static DistExecutor.SafeRunnable update(Map<EntityType<?>, EntityExpEntry> expMap, Type type) {
            return () -> type.apply.accept(expMap);
        }
    }

    @AllArgsConstructor
    public enum Type {
        Slaughterer(expMap -> SlaughtererData.Provider.setExpMap(new HashMap<>(expMap)));

        private final Consumer<Map<EntityType<?>, EntityExpEntry>> apply;
    }
}
