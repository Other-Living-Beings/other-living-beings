package de.blutmondgilde.otherlivingbeings.registry;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.api.gui.inventory.tabs.DefaultTabContainer;
import de.blutmondgilde.otherlivingbeings.api.skill.ISkill;
import de.blutmondgilde.otherlivingbeings.container.SkillContainer;
import de.blutmondgilde.otherlivingbeings.data.loot.FarmerLootModifier;
import de.blutmondgilde.otherlivingbeings.data.loot.MinerLootModifier;
import de.blutmondgilde.otherlivingbeings.skills.Farmer;
import de.blutmondgilde.otherlivingbeings.skills.Lumberjack;
import de.blutmondgilde.otherlivingbeings.skills.Miner;
import de.blutmondgilde.otherlivingbeings.skills.Slaughterer;
import de.blutmondgilde.otherlivingbeings.skills.Smelter;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryBuilder;

public class OtherLivingBeingRegistry {
    public static void init() {
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        Containers.init(modBus);
        LootModifier.init(modBus);
        SkillRegistry.init(modBus);
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

    private static class SkillRegistry {
        private static final DeferredRegister<ISkill> registry = DeferredRegister.create(ISkill.class, OtherLivingBeings.MOD_ID);

        public static void init(final IEventBus modEventBus) {
            registry.makeRegistry("player_skills", RegistryBuilder::new);
            registry.register("lumberjack", Lumberjack::new);
            registry.register("miner", Miner::new);
            registry.register("farmer", Farmer::new);
            registry.register("slaughterer", Slaughterer::new);
            registry.register("smelter", Smelter::new);
            registry.register(modEventBus);
        }
    }
}
