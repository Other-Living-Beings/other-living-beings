package de.blutmondgilde.otherlivingbeings.data.loot;

import com.google.gson.JsonObject;
import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.api.capability.OtherLivingBeingsCapability;
import de.blutmondgilde.otherlivingbeings.capability.skill.IPlayerSkills;
import de.blutmondgilde.otherlivingbeings.capability.skill.PlayerSkillsImpl;
import de.blutmondgilde.otherlivingbeings.data.skills.provider.MinerData;
import de.blutmondgilde.otherlivingbeings.skills.Miner;
import de.blutmondgilde.otherlivingbeings.util.ChatMessageUtils;
import de.blutmondgilde.otherlivingbeings.util.TranslationUtils;
import net.minecraft.Util;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public class MinerLootModifier extends LootModifier {
    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    protected MinerLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @NotNull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        //Check for BlockState
        if (!context.hasParam(LootContextParams.BLOCK_STATE)) return generatedLoot;
        //Check Block has a valid exp value
        final BlockState state = context.getParam(LootContextParams.BLOCK_STATE);
        if (!MinerData.Provider.getExpMap().containsKey(state.getBlock())) return generatedLoot;
        //Check BlockState
        if (!MinerData.Provider.getExpMap().get(state.getBlock()).isValid(state)) return generatedLoot;
        //Check for Entity
        if (!context.hasParam(LootContextParams.THIS_ENTITY)) return generatedLoot;
        //Check for Player
        if (!(context.getParam(LootContextParams.THIS_ENTITY) instanceof Player player)) return generatedLoot;
        IPlayerSkills playerSkills = player.getCapability(OtherLivingBeingsCapability.PLAYER_SKILLS).orElse(new PlayerSkillsImpl());
        Optional<Miner> minerSkill = playerSkills.getSkills()
                .stream()
                .filter(iSkill -> iSkill instanceof Miner)
                .map(iSkill -> (Miner) iSkill)
                .findFirst();

        if (minerSkill.isPresent()) {
            double random = Math.random();
            double increment = Math.ceil(minerSkill.get().getLevel() / 10.0);
            //double
            if (random < OtherLivingBeings.getConfig().get().skillConfig.miner.doubleLootChance * increment) {
                generatedLoot.addAll(generatedLoot);
                MutableComponent doubleLootMessage = ChatMessageUtils.createSkillMessage();
                doubleLootMessage.append(" ");
                doubleLootMessage.append(TranslationUtils.createSkillMessage("miner.doubleloot").withStyle(Style.EMPTY.withColor(new Color(0, 148, 4).getRGB())));
                player.sendMessage(doubleLootMessage, Util.NIL_UUID);
                minerSkill.get().increaseExp(MinerData.Provider.getExpMap().get(state.getBlock()).getExp());

                random = Math.random();
                increment = Math.ceil(minerSkill.get().getLevel() / 50.0);
                //triple
                if (random < OtherLivingBeings.getConfig().get().skillConfig.miner.tripleLootChance * increment) {
                    generatedLoot.addAll(generatedLoot);

                    MutableComponent tripleLootMessage = ChatMessageUtils.createSkillMessage();
                    tripleLootMessage.append(" ");
                    tripleLootMessage.append(TranslationUtils.createSkillMessage("miner.tripleloot").withStyle(Style.EMPTY.withColor(new Color(0, 148, 4).getRGB())));
                    player.sendMessage(tripleLootMessage, Util.NIL_UUID);
                    minerSkill.get().increaseExp(MinerData.Provider.getExpMap().get(state.getBlock()).getExp());
                }

                playerSkills.sync(player);
            }
        }
        return generatedLoot;
    }


    public static class Serializer extends GlobalLootModifierSerializer<MinerLootModifier> {

        @Override
        public MinerLootModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
            return new MinerLootModifier(ailootcondition);
        }

        @Override
        public JsonObject write(MinerLootModifier instance) {
            return makeConditions(instance.conditions);
        }
    }
}
