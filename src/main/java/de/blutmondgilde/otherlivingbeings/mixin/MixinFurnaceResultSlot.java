package de.blutmondgilde.otherlivingbeings.mixin;

import de.blutmondgilde.otherlivingbeings.api.event.FurnaceEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FurnaceResultSlot.class)
public class MixinFurnaceResultSlot {

    @Shadow
    @Final
    private Player player;

    @Inject(method = "checkTakeAchievements(Lnet/minecraft/world/item/ItemStack;)V", at = @At("RETURN"))
    public void onTake(ItemStack itemStack, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new FurnaceEvent.TakeResult(this.player, itemStack));
    }
}
