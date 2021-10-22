package de.blutmondgilde.otherlivingbeings.registry;

import de.blutmondgilde.otherlivingbeings.command.GroupCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class CommandRegistry {
    public static void init(IEventBus forgeBus) {
        forgeBus.addListener(CommandRegistry::registerCommands);
    }

    public static void registerCommands(RegisterCommandsEvent e) {
        GroupCommand.register(e.getDispatcher());
    }
}
