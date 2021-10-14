package de.blutmondgilde.otherlivingbeings.handler;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.api.capability.OtherLivingBeingsCapability;
import de.blutmondgilde.otherlivingbeings.api.skill.listener.BlockBreakListener;
import de.blutmondgilde.otherlivingbeings.api.skill.listener.BlockBrokenListener;
import de.blutmondgilde.otherlivingbeings.capability.skill.IPlayerSkills;
import de.blutmondgilde.otherlivingbeings.capability.skill.PlayerSkillsImpl;
import lombok.experimental.UtilityClass;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@UtilityClass
public class SkillHandler {
    public static void init() {
        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(SkillHandler::onBlockBroken);
        forgeBus.addListener(SkillHandler::onBreakBlock);
    }

    public static void onBreakBlock(PlayerEvent.BreakSpeed e) {
        final IPlayerSkills skills = e.getPlayer().getCapability(OtherLivingBeingsCapability.PLAYER_SKILLS).orElse(new PlayerSkillsImpl());
        AtomicReference<Float> breakSpeed = new AtomicReference<>(e.getNewSpeed());

        skills.getSkills()
                .stream()
                .filter(iSkill -> iSkill instanceof BlockBreakListener)
                .map(iSkill -> (BlockBreakListener) iSkill)
                .forEach(blockBreakListener -> {
                    float newBreakSpeed = blockBreakListener.onBlockBreak(e.getPlayer(), e.getState(), e.getPos(), e.getOriginalSpeed(), breakSpeed.get());
                    //Apply the highest value if break hasn't been canceled
                    if (newBreakSpeed > 0 && breakSpeed.get() > 0) {
                        breakSpeed.set(Math.max(breakSpeed.get(), newBreakSpeed));
                    } else {
                        breakSpeed.set(0F);
                    }
                });
    }

    public static void onBlockBroken(BlockEvent.BreakEvent e) {
        final IPlayerSkills skills = e.getPlayer().getCapability(OtherLivingBeingsCapability.PLAYER_SKILLS).orElse(new PlayerSkillsImpl());
        AtomicBoolean isCanceled = new AtomicBoolean(false);
        OtherLivingBeings.getLogger().debug("Block Break Event");
        skills.getSkills()
                .stream()
                .filter(iSkill -> iSkill instanceof BlockBrokenListener)
                .map(iSkill -> (BlockBrokenListener) iSkill)
                .forEach(blockBrokenListener -> {
                    OtherLivingBeings.getLogger().debug("Firing onBlockBroken");
                    blockBrokenListener.onBlockBroken(e.getPlayer(), e.getState(), e.getPos(), e.getWorld());
                });

        e.setCanceled(isCanceled.get());
    }
}
