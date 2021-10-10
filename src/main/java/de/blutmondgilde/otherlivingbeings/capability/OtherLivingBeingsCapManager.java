package de.blutmondgilde.otherlivingbeings.capability;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.capability.skill.IPlayerSkills;
import de.blutmondgilde.otherlivingbeings.capability.skill.PlayerSkillsImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class OtherLivingBeingsCapManager {
    public static void init(final IEventBus modBus, final IEventBus forgeBus) {
        modBus.addListener(OtherLivingBeingsCapManager::registerCapabilities);
        forgeBus.addGenericListener(Entity.class, OtherLivingBeingsCapManager::attachEntityCap);
    }

    private static void registerCapabilities(final RegisterCapabilitiesEvent e) {
        e.register(IPlayerSkills.class);
    }

    private static void attachEntityCap(final AttachCapabilitiesEvent<Entity> e) {
        if (PlayerSkillsImpl.canAttachTo(e.getObject())) e.addCapability(new ResourceLocation(OtherLivingBeings.MOD_ID, "player_skills"), new PlayerSkillsImpl());
    }
}
