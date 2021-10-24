package de.blutmondgilde.otherlivingbeings.api.skill.listener;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public interface LivingHurtListener {
    /**
     * @param target {@link LivingEntity} getting attacked
     * @param amount of Damage to the target
     * @param source {@link DamageSource}
     * @param trueSource {@link Player} who caused the Damage
     * @return new Damage amount
     */
    float onHurt(LivingEntity target, float amount, DamageSource source, Player trueSource);
}
