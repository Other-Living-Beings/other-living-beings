package de.blutmondgilde.otherlivingbeings.api.capability;

import de.blutmondgilde.otherlivingbeings.capability.skill.IPlayerSkills;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class OtherLivingBeingsCapability {
    public static final Capability<IPlayerSkills> PLAYER_SKILLS = CapabilityManager.get(new CapabilityToken<>() {});
}
