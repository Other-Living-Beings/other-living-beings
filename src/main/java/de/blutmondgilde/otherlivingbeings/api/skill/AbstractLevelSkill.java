package de.blutmondgilde.otherlivingbeings.api.skill;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractLevelSkill implements ISkill {
    @Getter
    @Setter
    private int level;
    @Getter
    @Setter
    private double exp;
    @Getter
    private ResourceLocation registryName;

    public AbstractLevelSkill() {
        this.level = 1;
        this.exp = 0;
    }

    @Override
    public void levelUp() {
        this.level++;
    }

    @Override
    public void increaseExp(double amount) {
        //Add amount of EXP
        exp += amount;
        //Check for level up
        if (exp <= nextLevelAt()) {
            //lower exp to the value on the next level
            exp -= nextLevelAt();
            //trigger level up
            levelUp();
        }
    }

    @Override
    public ISkill setRegistryName(ResourceLocation registryName) {
        this.registryName = registryName;
        return this;
    }
}
