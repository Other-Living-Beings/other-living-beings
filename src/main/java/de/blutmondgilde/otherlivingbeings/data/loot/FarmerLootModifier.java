package de.blutmondgilde.otherlivingbeings.data.loot;

import com.google.gson.JsonObject;
import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.api.capability.OtherLivingBeingsCapability;
import de.blutmondgilde.otherlivingbeings.capability.skill.PlayerSkillsImpl;
import de.blutmondgilde.otherlivingbeings.data.skills.provider.FarmerData;
import de.blutmondgilde.otherlivingbeings.skills.Farmer;
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

public class FarmerLootModifier extends LootModifier {
    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    protected FarmerLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @NotNull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        //Check for BlockState
        if (!context.hasParam(LootContextParams.BLOCK_STATE)) return generatedLoot;
        //Check Block has a valid exp value
        final BlockState state = context.getParam(LootContextParams.BLOCK_STATE);
        if (!FarmerData.Provider.getExpMap().containsKey(state.getBlock())) return generatedLoot;
        //Check BlockState
        if (!FarmerData.Provider.getExpMap().get(state.getBlock()).isValid(state)) return generatedLoot;
        //Check for Entity
        if (!context.hasParam(LootContextParams.THIS_ENTITY)) return generatedLoot;
        //Check for Player
        if (!(context.getParam(LootContextParams.THIS_ENTITY) instanceof Player player)) return generatedLoot;
        Optional<Farmer> farmerSkill = player.getCapability(OtherLivingBeingsCapability.PLAYER_SKILLS).orElse(new PlayerSkillsImpl()).getSkills()
                .stream()
                .filter(iSkill -> iSkill instanceof Farmer)
                .map(iSkill -> (Farmer) iSkill)
                .findFirst();
        if (farmerSkill.isPresent()) {
            double random = Math.random();
            double increment = Math.ceil(farmerSkill.get().getLevel() / 10.0);
            if (random < OtherLivingBeings.getConfig().get().skillConfig.farmer.doubleLootChance * increment) {
                generatedLoot.addAll(generatedLoot);
                MutableComponent doubleLootMessage = ChatMessageUtils.createSkillMessage();
                doubleLootMessage.append(" ");
                doubleLootMessage.append(TranslationUtils.createSkillMessage("farmer.doubleloot").withStyle(Style.EMPTY.withColor(new Color(0, 148, 4).getRGB())));
                player.sendMessage(doubleLootMessage, Util.NIL_UUID);
            }
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<FarmerLootModifier> {

        @Override
        public FarmerLootModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
            return new FarmerLootModifier(ailootcondition);
        }

        @Override
        public JsonObject write(FarmerLootModifier instance) {
            return makeConditions(instance.conditions);
        }
    }
}
