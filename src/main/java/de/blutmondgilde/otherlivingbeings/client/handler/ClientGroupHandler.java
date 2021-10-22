package de.blutmondgilde.otherlivingbeings.client.handler;

import de.blutmondgilde.otherlivingbeings.client.ClientGroupHolder;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class ClientGroupHandler {
    public static void init(IEventBus forgeBus) {
        forgeBus.addListener(ClientGroupHandler::onClientLeave);
    }

    public static void onClientLeave(ClientPlayerNetworkEvent.LoggedOutEvent e) {
        ClientGroupHolder.reset();
    }
}
