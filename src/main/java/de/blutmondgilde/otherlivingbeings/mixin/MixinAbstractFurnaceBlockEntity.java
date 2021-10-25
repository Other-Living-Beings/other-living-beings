package de.blutmondgilde.otherlivingbeings.mixin;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.api.event.FurnaceEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class MixinAbstractFurnaceBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, RecipeHolder, StackedContentsCompatible {

    public MixinAbstractFurnaceBlockEntity(BlockEntityType<?> p_155076_, BlockPos p_155077_, BlockState p_155078_) {
        super(p_155076_, p_155077_, p_155078_);
    }

    @Inject(method = "getTotalCookTime(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;)I", at = @At("RETURN"), cancellable = true)
    private static void onGetTotalCookTime(Level level, RecipeType<? extends AbstractCookingRecipe> recipeType, Container container, CallbackInfoReturnable<Integer> cir) {
        int cookTime = cir.getReturnValue();
        AbstractFurnaceBlockEntity blockEntity = (AbstractFurnaceBlockEntity) container;

        FurnaceEvent.CalculateCookTime cookTimeEvent = new FurnaceEvent.CalculateCookTime(blockEntity, cookTime);
        MinecraftForge.EVENT_BUS.post(cookTimeEvent);
        OtherLivingBeings.getLogger().info("Cook time has been changed from {}, to {}", cookTime, cookTimeEvent.getCookTime());
        cookTime = cookTimeEvent.getCookTime();

        cir.setReturnValue(cookTime);
    }
}
