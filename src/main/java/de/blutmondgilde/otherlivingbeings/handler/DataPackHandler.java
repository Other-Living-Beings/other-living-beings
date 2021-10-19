package de.blutmondgilde.otherlivingbeings.handler;

import de.blutmondgilde.otherlivingbeings.data.jobs.ReloadableJobDataProvider;
import de.blutmondgilde.otherlivingbeings.data.jobs.farmer.FarmerDataProvider;
import de.blutmondgilde.otherlivingbeings.data.jobs.lumberjack.LumberjackDataProvider;
import de.blutmondgilde.otherlivingbeings.data.jobs.miner.MinerDataProvider;
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

        dataProvider.add(new LumberjackDataProvider());
        dataProvider.add(new MinerDataProvider());
        dataProvider.add(new FarmerDataProvider());
    }

    public static void onReload(AddReloadListenerEvent e) {
        dataProvider.forEach(e::addListener);
    }

    public static void onLoginSync(PlayerEvent.PlayerLoggedInEvent e) {
        dataProvider.forEach(reloadableJobDataProvider -> reloadableJobDataProvider.sync((ServerPlayer) e.getPlayer()));
    }
}
