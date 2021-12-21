package de.blutmondgilde.otherlivingbeings.network.packet.toclient;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.data.skills.pojo.ItemExpEntry;
import de.blutmondgilde.otherlivingbeings.data.skills.provider.SmelterData;
import lombok.AllArgsConstructor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraftforge.fmllegacy.common.registry.GameRegistry;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

@AllArgsConstructor
public class SyncItemDataPackPacket {
    private final Map<Item, ItemExpEntry> expMap;
    private final Type type;

    public static void encode(SyncItemDataPackPacket packet, FriendlyByteBuf buffer) {
        buffer.writeMap(packet.expMap, (friendlyByteBuf, item) -> friendlyByteBuf.writeResourceLocation(item.getRegistryName()),
            (friendlyByteBuf, itemExpEntry) -> friendlyByteBuf.writeUtf(itemExpEntry.toJson()
                .toString()));
        buffer.writeEnum(packet.type);
    }

    public static SyncItemDataPackPacket decode(FriendlyByteBuf buffer) {
        return new SyncItemDataPackPacket(buffer
            .readMap(friendlyByteBuf -> GameRegistry.findRegistry(Item.class).getValue(friendlyByteBuf.readResourceLocation()),
                friendlyByteBuf -> {
                    ItemExpEntry value = new ItemExpEntry();
                    return value.fromJson(friendlyByteBuf.readUtf());
                }), buffer.readEnum(Type.class));
    }

    public static void handle(final SyncItemDataPackPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> OtherLivingBeings.getInstance().getProxy().updateDataProvider(packet.expMap, packet.type));
        context.get().setPacketHandled(true);
    }

    @AllArgsConstructor
    public enum Type {
        Smelter(expMap -> SmelterData.Provider.setExpMap(new HashMap<>(expMap)));

        public final Consumer<Map<Item, ItemExpEntry>> apply;
    }
}
