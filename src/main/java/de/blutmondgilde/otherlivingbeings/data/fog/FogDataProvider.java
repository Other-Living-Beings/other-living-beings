package de.blutmondgilde.otherlivingbeings.data.fog;

import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class FogDataProvider {
    private static final HashMap<UUID, BlockPos> fogTeleportData = new HashMap<>();

    public static void addFogTeleportData(final UUID player, final BlockPos pos) {
        fogTeleportData.put(player, pos);
    }

    public static Optional<BlockPos> getTeleportData(final UUID uuid) {
        return Optional.ofNullable(fogTeleportData.get(uuid));
    }
}
