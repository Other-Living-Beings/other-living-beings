package de.blutmondgilde.otherlivingbeings.handler;

import de.blutmondgilde.otherlivingbeings.data.skills.ReloadableJobDataProvider;
import de.blutmondgilde.otherlivingbeings.data.skills.provider.FarmerData;
import de.blutmondgilde.otherlivingbeings.data.skills.provider.LumberjackData;
import de.blutmondgilde.otherlivingbeings.data.skills.provider.MinerData;
import de.blutmondgilde.otherlivingbeings.data.skills.provider.SlaughtererData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.ArrayList;

public class DataPackHandler {
    private static final ArrayList<ReloadableJobDataProvider> dataProvider = new ArrayList<>();

    public static void init(IEventBus forgeBus) {
        forgeBus.addListener(DataPackHandler::onReload);
        forgeBus.addListener(DataPackHandler::onLoginSync);

        dataProvider.add(new LumberjackData.Provider());
        dataProvider.add(new MinerData.Provider());
        dataProvider.add(new FarmerData.Provider());
        dataProvider.add(new SlaughtererData.Provider());
    }

    public static void onReload(AddReloadListenerEvent e) {
        dataProvider.forEach(e::addListener);
    }

    public static void onLoginSync(PlayerEvent.PlayerLoggedInEvent e) {
        dataProvider.forEach(reloadableJobDataProvider -> reloadableJobDataProvider.sync((ServerPlayer) e.getPlayer()));
    }
}
