package de.blutmondgilde.otherlivingbeings.capability.skill;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.api.capability.OtherLivingBeingsCapability;
import de.blutmondgilde.otherlivingbeings.api.skill.ISkill;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fmllegacy.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerSkillsImpl implements IPlayerSkills {
    @Getter
    @Setter
    private List<ISkill> skills;
    private final LazyOptional<IPlayerSkills> holder = LazyOptional.of(() -> this);

    public PlayerSkillsImpl() {
        skills = new ArrayList<>();
        skills.addAll(GameRegistry
                .findRegistry(ISkill.class)
                .getValues()
                .stream()
                .filter(iSkill -> !iSkill.isUnlockable())
                .collect(Collectors.toList()));
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        skills.stream()
                .filter(iSkill -> !iSkill.isUnlockable())
                .forEach(iSkill -> {
                    if (iSkill.getRegistryName() == null) {
                        OtherLivingBeings.getLogger().fatal("Exception while serializing PlayerSkills. A Skill had no RegistryName!");
                    } else {
                        tag.put(iSkill.getRegistryName().toString(), iSkill.serializeNBT());
                    }
                });
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        //Remove old skills
        skills.clear();
        //Add given skills
        GameRegistry.findRegistry(ISkill.class)
                .getEntries()
                .forEach(resourceKeyISkillEntry -> {
                    if (nbt.contains(resourceKeyISkillEntry.getKey().getRegistryName().toString())) {
                        ISkill skill = resourceKeyISkillEntry.getValue();
                        CompoundTag tag = (CompoundTag) nbt.get(resourceKeyISkillEntry.getKey().getRegistryName().toString());
                        assert tag != null;
                        skill.deserializeNBT(tag);
                        skills.add(skill);
                    }
                });
    }

    public static boolean canAttachTo(ICapabilityProvider provider) {
        if (!(provider instanceof Player player)) return false;
        return !player.getCapability(OtherLivingBeingsCapability.PLAYER_SKILLS).isPresent();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return OtherLivingBeingsCapability.PLAYER_SKILLS.orEmpty(cap, holder);
    }
}
