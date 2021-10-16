package de.blutmondgilde.otherlivingbeings.capability.skill;

import de.blutmondgilde.otherlivingbeings.api.skill.ISkill;
import de.blutmondgilde.otherlivingbeings.network.OtherLivingBeingNetwork;
import de.blutmondgilde.otherlivingbeings.network.packet.toclient.SyncSkillsPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import java.util.List;

public interface IPlayerSkills extends ICapabilitySerializable<CompoundTag> {
    List<ISkill> getSkills();

    void setSkills(List<ISkill> skills);

    default void sync(final Entity entity) {
        OtherLivingBeingNetwork.getInstance().send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new SyncSkillsPacket(this, entity));
    }
}
