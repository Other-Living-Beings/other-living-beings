package de.blutmondgilde.otherlivingbeings.data.group;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.util.GsonUUIDTypeAdapter;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class GroupProvider {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(UUID.class, new GsonUUIDTypeAdapter())
            .create();

    private static final Path storagePath = FMLPaths.GAMEDIR.get().resolve(OtherLivingBeings.MOD_ID).resolve("groups");
    static final CopyOnWriteArrayList<GroupInvite> groupInvites = new CopyOnWriteArrayList<>();
    private static GroupMasterList groupMasterList = new GroupMasterList();

    public static void initialize(MinecraftServer server) {
        MinecraftForge.EVENT_BUS.addListener(GroupProvider::tick);
        storagePath.toFile().mkdirs();

        String fileName = server.getWorldData().getLevelName() + ".json";

        try {
            GroupMasterList loadedList = gson.fromJson(new FileReader(storagePath.resolve(fileName).toFile()), GroupMasterList.class);
            groupMasterList = Objects.requireNonNullElseGet(loadedList, GroupMasterList::new);
        } catch (FileNotFoundException e) {
            OtherLivingBeings.getLogger().warn("Could not find a Group storage file for {}! If your world is new you can ignore this warning.", fileName);
        }
    }

    public static void shutdown(MinecraftServer server) {
        String fileName = server.getWorldData().getLevelName() + ".json";

        try {
            FileWriter writer = new FileWriter(storagePath.resolve(fileName).toFile(), false);
            gson.toJson(groupMasterList, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            OtherLivingBeings.getLogger().fatal("Error while trying to save GroupMasterList.\n {}", e.getMessage());
            e.printStackTrace();
        }
    }

    public static void addPlayerToGroup(Player player, GroupData group) {
        groupMasterList.addPlayerToGroup(player, group);
    }

    public static void removePlayerFromGroup(Player player) {
        groupMasterList.removePlayerFromGroup(player);
    }

    public static Optional<GroupData> getGroup(Player player) {
        return Optional.ofNullable(groupMasterList.getPlayerGroup(player));
    }

    public static Optional<GroupData> getGroup(UUID player) {
        return Optional.ofNullable(groupMasterList.getPlayerGroup(player));
    }

    public static void tick(TickEvent.ServerTickEvent e) {
        if (!e.phase.equals(TickEvent.Phase.END)) return;
        //Remove old invites
        groupInvites.stream()
                .filter(groupInvite -> groupInvite.getRemainingTicks() <= 0)
                .forEach(groupInvites::remove);
        //Tick lower invite duration
        groupInvites.forEach(GroupInvite::tick);
    }

    public static GroupInvite invite(GroupInvite groupInvite) {
        groupInvites.add(groupInvite);
        return groupInvite;
    }

    public static Optional<GroupInvite> getInvitation(UUID id) {
        return groupInvites.stream().filter(groupInvite -> groupInvite.getInviteId().equals(id)).findFirst();
    }

    public static void create(GroupData data) {
        groupMasterList.createGroup(data);
    }
}
