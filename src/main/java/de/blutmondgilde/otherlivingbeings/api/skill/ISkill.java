package de.blutmondgilde.otherlivingbeings.api.skill;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.IForgeRegistryEntry;

/** Interface required for creating Skills */
public interface ISkill extends INBTSerializable<CompoundTag>, IForgeRegistryEntry<ISkill> {
    void setLevel(int level);

    void levelUp();

    int getLevel();

    void setExp(double amount);

    void increaseExp(double amount);

    double getExp();

    /**
     * returns the amount of exp required to trigger {@link ISkill#levelUp()}
     */
    default double nextLevelAt() {
        return Math.max(Math.ceil(60 * Math.pow(getLevel(), 2.8) - 60), 1);
    }

    @Override
    default CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("level", this.getLevel());
        tag.putDouble("exp", this.getExp());
        return tag;
    }

    @Override
    default void deserializeNBT(CompoundTag nbt) {
        setLevel(nbt.getInt("level"));
        setExp(nbt.getDouble("exp"));
    }

    /**
     * false: The skill will be applied on first join
     * true: The skill will be applied if the Player unlocks it
     */
    default boolean isUnlockable() {
        return false;
    }

    @Override
    default Class<ISkill> getRegistryType() {
        return ISkill.class;
    }

    /** Returns a {@link MutableComponent} which contains the Name which will be displayed in all GUIs */
    MutableComponent getDisplayName();
}
