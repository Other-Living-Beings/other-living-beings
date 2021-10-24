package de.blutmondgilde.otherlivingbeings.api.skill.listener;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public interface LivingHurtListener {
    void onHurt(LivingEntity target, float amount, DamageSource source, Player trueSource);
    //TODO add Listener
}
