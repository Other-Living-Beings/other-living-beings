package de.blutmondgilde.otherlivingbeings.data;

import de.blutmondgilde.otherlivingbeings.data.jobs.lumberjack.LumberjackDataProvider;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class DataPackHandler {
    public static void init(IEventBus forgeBus) {
        forgeBus.addListener(DataPackHandler::onReload);
    }

    public static void onReload(AddReloadListenerEvent e) {
        e.addListener(new LumberjackDataProvider());
    }
}
