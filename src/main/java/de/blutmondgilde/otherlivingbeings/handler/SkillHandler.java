package de.blutmondgilde.otherlivingbeings.handler;

import de.blutmondgilde.otherlivingbeings.api.capability.OtherLivingBeingsCapability;
import de.blutmondgilde.otherlivingbeings.api.skill.listener.BlockBreakListener;
import de.blutmondgilde.otherlivingbeings.api.skill.listener.BlockBrokenListener;
import de.blutmondgilde.otherlivingbeings.api.skill.listener.CropGrowListener;
import de.blutmondgilde.otherlivingbeings.api.skill.listener.LivingHurtListener;
import de.blutmondgilde.otherlivingbeings.capability.skill.IPlayerSkills;
import de.blutmondgilde.otherlivingbeings.capability.skill.PlayerSkillsImpl;
import lombok.experimental.UtilityClass;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@UtilityClass
public class SkillHandler {
    public static void init() {
        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(SkillHandler::onBlockBroken);
        forgeBus.addListener(SkillHandler::onBreakBlock);
        forgeBus.addListener(SkillHandler::onPlantGrowth);
        forgeBus.addListener(SkillHandler::LivingHurt);
    }

    public static void onBreakBlock(final PlayerEvent.BreakSpeed e) {
        final IPlayerSkills skills = e.getPlayer().getCapability(OtherLivingBeingsCapability.PLAYER_SKILLS).orElseThrow(() -> new IllegalStateException("No Skill Capablility present!"));
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

    public static void onBlockBroken(final BlockEvent.BreakEvent e) {
        final IPlayerSkills skills = e.getPlayer().getCapability(OtherLivingBeingsCapability.PLAYER_SKILLS).orElseThrow(() -> new IllegalStateException("No Skill Capablility present!"));
        AtomicBoolean isCanceled = new AtomicBoolean(false);
        skills.getSkills()
                .stream()
                .filter(iSkill -> iSkill instanceof BlockBrokenListener)
                .map(iSkill -> (BlockBrokenListener) iSkill)
                .forEach(blockBrokenListener -> blockBrokenListener.onBlockBroken(e.getPlayer(), e.getState(), e.getPos(), e.getWorld()));
        e.setCanceled(isCanceled.get());
    }

    public static void onPlantGrowth(final BlockEvent.CropGrowEvent.Pre e) {
        LevelAccessor world = e.getWorld();
        BlockPos position = e.getPos();
        BoundingBox range = new BoundingBox(position);
        List<Entity> entities = world.getEntities(null, AABB.of(range.inflate(8)));
        if (entities.isEmpty()) return;

        List<Player> players = entities.stream()
                .filter(entity -> entity instanceof Player)
                .map(entity -> (Player) entity)
                .collect(Collectors.toList());
        if (players.isEmpty()) return;
        AtomicReference<Event.Result> result = new AtomicReference<>(Event.Result.DEFAULT);

        players.forEach(player -> player.getCapability(OtherLivingBeingsCapability.PLAYER_SKILLS).orElse(new PlayerSkillsImpl())
                .getSkills()
                .stream()
                .filter(iSkill -> iSkill instanceof CropGrowListener)
                .map(iSkill -> (CropGrowListener) iSkill)
                .forEach(cropGrowListener -> {
                    Event.Result listenerResult = cropGrowListener.onGrowTick(player, position, e.getState(), world);
                    if (result.get().equals(Event.Result.DEFAULT)) {
                        result.set(listenerResult);
                    }
                }));

        e.setResult(result.get());
    }

    public static void LivingHurt(final LivingHurtEvent e) {
        Optional<Player> source = isCausedByPlayer(e.getSource());
        source.ifPresent(player -> player.getCapability(OtherLivingBeingsCapability.PLAYER_SKILLS).orElse(new PlayerSkillsImpl())
                .getSkills()
                .stream()
                .filter(iSkill -> iSkill instanceof LivingHurtListener)
                .map(iSkill -> (LivingHurtListener) iSkill)
                .forEach(livingHurtListener -> livingHurtListener.onHurt(e.getEntityLiving(), e.getAmount(), e.getSource(), player)));
    }

    private static Optional<Player> isCausedByPlayer(DamageSource damageSource) {
        if (damageSource.getDirectEntity() instanceof Player player) return Optional.of(player);
        if (damageSource.getEntity() instanceof Player player) return Optional.of(player);
        return Optional.empty();
    }
}
