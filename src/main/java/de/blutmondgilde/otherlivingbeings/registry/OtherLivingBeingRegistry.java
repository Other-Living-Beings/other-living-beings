package de.blutmondgilde.otherlivingbeings.registry;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.api.gui.inventory.tabs.AbstractTabContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class OtherLivingBeingRegistry {
    public static void init() {
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        Containers.init(modBus);
    }

    private static class Containers {
        private static final DeferredRegister<MenuType<?>> containers = DeferredRegister.create(ForgeRegistries.CONTAINERS, OtherLivingBeings.MOD_ID);

        public static void init(IEventBus modBus) {
            containers.register(modBus);
            containers.register("basic_tab_container", () -> IForgeContainerType.create((windowId, inv, data) -> new AbstractTabContainer(windowId, inv.player, inv)));
        }
    }
}
