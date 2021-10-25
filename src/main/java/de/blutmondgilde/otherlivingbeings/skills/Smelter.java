package de.blutmondgilde.otherlivingbeings.skills;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.api.capability.OtherLivingBeingsCapability;
import de.blutmondgilde.otherlivingbeings.api.skill.AbstractLevelSkill;
import de.blutmondgilde.otherlivingbeings.api.skill.listener.FurnaceCookTimeListener;
import de.blutmondgilde.otherlivingbeings.api.skill.listener.TakeFurnaceResultListener;
import de.blutmondgilde.otherlivingbeings.capability.skill.IPlayerSkills;
import de.blutmondgilde.otherlivingbeings.capability.skill.PlayerSkillsImpl;
import de.blutmondgilde.otherlivingbeings.data.skills.pojo.ItemExpEntry;
import de.blutmondgilde.otherlivingbeings.data.skills.provider.SmelterData;
import de.blutmondgilde.otherlivingbeings.util.TranslationUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

import java.awt.*;
import java.util.List;

public class Smelter extends AbstractLevelSkill implements TakeFurnaceResultListener, FurnaceCookTimeListener {
    @Override
    public MutableComponent getDisplayName() {
        return TranslationUtils.createSkillComponent("smelter")
                .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(new Color(255, 76, 0, 255).getRGB())));
    }

    @Override
    public void onTake(Player player, ItemStack itemStack) {
        List<Smelter> smelterList = getSkillsFromPlayer(player);

        for (Smelter smelter : smelterList) {
            if (!SmelterData.Provider.getExpMap().containsKey(itemStack.getItem())) return;
            ItemExpEntry expValue = SmelterData.Provider.getExpMap().get(itemStack.getItem());
            smelter.increaseExp(expValue.getExp());

            for (int i = 0; i < itemStack.getCount(); i++) {
                double random = Math.random();
                double increment = Math.ceil(smelter.getLevel() / 10.0);
                if (random < OtherLivingBeings.getConfig().get().skillConfig.smelter.doubleLootChance * increment) {
                    ItemStack stack = itemStack.copy();
                    stack.setCount(1);
                    player.addItem(stack);
                }
            }
        }

        IPlayerSkills skills = player.getCapability(OtherLivingBeingsCapability.PLAYER_SKILLS).orElse(new PlayerSkillsImpl());
        skills.sync(player);
    }

    @Override
    public int calculateCookTime(AbstractFurnaceBlockEntity blockEntity, ServerPlayer player, int currentCookTime) {
        List<Smelter> smelterList = getSkillsFromPlayer(player);

        int cookTime = currentCookTime;
        for (Smelter smelter : smelterList) {
            cookTime = smelter.modifyCookTime(cookTime);
        }

        return Math.max(1, cookTime);
    }

    private int modifyCookTime(int currentTime) {
        currentTime -= OtherLivingBeings.getConfig().get().skillConfig.smelter.smeltBoost * getLevel();
        return currentTime;
    }

    private List<Smelter> getSkillsFromPlayer(Player player) {
        return player.getCapability(OtherLivingBeingsCapability.PLAYER_SKILLS).orElse(new PlayerSkillsImpl())
                .getSkills()
                .stream()
                .filter(iSkill -> iSkill instanceof Smelter)
                .map(iSkill -> (Smelter) iSkill)
                .toList();
    }
}
