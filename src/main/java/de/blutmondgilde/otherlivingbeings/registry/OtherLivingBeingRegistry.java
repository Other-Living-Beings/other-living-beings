package de.blutmondgilde.otherlivingbeings.registry;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.api.gui.inventory.tabs.DefaultTabContainer;
import de.blutmondgilde.otherlivingbeings.container.SkillContainer;
import de.blutmondgilde.otherlivingbeings.data.loot.FarmerLootModifier;
import de.blutmondgilde.otherlivingbeings.data.loot.MinerLootModifier;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class OtherLivingBeingRegistry {
    public static void init() {
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        Containers.init(modBus);
        LootModifier.init(modBus);
    }

    private static class Containers {
        private static final DeferredRegister<MenuType<?>> containers = DeferredRegister.create(ForgeRegistries.CONTAINERS, OtherLivingBeings.MOD_ID);

        public static void init(IEventBus modBus) {
            containers.register(modBus);
            containers.register("basic_tab_container", () -> IForgeContainerType.create((windowId, inv, data) -> new DefaultTabContainer(windowId, inv.player, inv)));
            containers.register("skill_container", () -> IForgeContainerType.create((windowId, inv, data) -> new SkillContainer(windowId, inv.player, inv)));
        }
    }

    private static class LootModifier {
        private static final DeferredRegister<GlobalLootModifierSerializer<?>> serializer = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, OtherLivingBeings.MOD_ID);

        private static void init(IEventBus modBus) {
            serializer.register(modBus);
            serializer.register("farmer_skill_modifier", FarmerLootModifier.Serializer::new);
            serializer.register("miner_skill_modifier", MinerLootModifier.Serializer::new);
        }
    }
}
