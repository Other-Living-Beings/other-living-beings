package de.blutmondgilde.otherlivingbeings.network.packet.toserver;

import de.blutmondgilde.otherlivingbeings.container.SkillContainer;
import de.blutmondgilde.otherlivingbeings.util.TranslationUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import java.util.function.Supplier;

public class RequestOpenSkillContainerPacket {

    public RequestOpenSkillContainerPacket() {}

    public RequestOpenSkillContainerPacket(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> NetworkHooks.openGui(ctx.get().getSender(), new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return TranslationUtils.createModComponent("container.skills.title");
            }

            @Override
            public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
                return new SkillContainer(pContainerId, pPlayer, pInventory);
            }
        }));
        ctx.get().setPacketHandled(true);
    }
}