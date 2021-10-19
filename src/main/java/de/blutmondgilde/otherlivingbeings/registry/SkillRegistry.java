package de.blutmondgilde.otherlivingbeings.registry;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.api.skill.ISkill;
import de.blutmondgilde.otherlivingbeings.skills.Farmer;
import de.blutmondgilde.otherlivingbeings.skills.Lumberjack;
import de.blutmondgilde.otherlivingbeings.skills.Miner;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryBuilder;

public class SkillRegistry {
    private static final DeferredRegister<ISkill> registry = DeferredRegister.create(ISkill.class, OtherLivingBeings.MOD_ID);

    public static void init(final IEventBus modEventBus) {
        registry.makeRegistry("player_skills", RegistryBuilder::new);
        registry.register("lumberjack", Lumberjack::new);
        registry.register("miner", Miner::new);
        registry.register("farmer", Farmer::new);
        registry.register(modEventBus);
    }
}
