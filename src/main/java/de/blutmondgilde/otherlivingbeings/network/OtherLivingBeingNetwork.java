package de.blutmondgilde.otherlivingbeings.network;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.network.packet.toclient.OpenInventoryPacket;
import de.blutmondgilde.otherlivingbeings.network.packet.toclient.SyncBlockDataPackPacket;
import de.blutmondgilde.otherlivingbeings.network.packet.toclient.SyncEntityDataPackPacket;
import de.blutmondgilde.otherlivingbeings.network.packet.toclient.SyncGroupDataPacket;
import de.blutmondgilde.otherlivingbeings.network.packet.toclient.SyncSkillsPacket;
import de.blutmondgilde.otherlivingbeings.network.packet.toclient.UpdateMemberDataPacket;
import de.blutmondgilde.otherlivingbeings.network.packet.toserver.RequestInventoryOpeningPacket;
import de.blutmondgilde.otherlivingbeings.network.packet.toserver.RequestMemberDataPacket;
import de.blutmondgilde.otherlivingbeings.network.packet.toserver.RequestOpenSkillContainerPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public class OtherLivingBeingNetwork {
    private static final String PROTOCOL_VERSION = String.valueOf(1);
    private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(OtherLivingBeings.MOD_ID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void registerPackets() {
        int messageId = 1;
        getInstance().registerMessage(messageId++, SyncSkillsPacket.class, SyncSkillsPacket::encode, SyncSkillsPacket::decode, SyncSkillsPacket::handle);
        getInstance().registerMessage(messageId++, SyncBlockDataPackPacket.class, SyncBlockDataPackPacket::encode, SyncBlockDataPackPacket::decode, SyncBlockDataPackPacket::handle);
        getInstance().registerMessage(messageId++, OpenInventoryPacket.class, OpenInventoryPacket::toBytes, OpenInventoryPacket::new, OpenInventoryPacket::handle);
        getInstance().registerMessage(messageId++, RequestInventoryOpeningPacket.class, RequestInventoryOpeningPacket::toBytes, RequestInventoryOpeningPacket::new, RequestInventoryOpeningPacket::handle);
        getInstance().registerMessage(messageId++, RequestOpenSkillContainerPacket.class, RequestOpenSkillContainerPacket::toBytes, RequestOpenSkillContainerPacket::new, RequestOpenSkillContainerPacket::handle);
        getInstance().registerMessage(messageId++, SyncGroupDataPacket.class, SyncGroupDataPacket::toBytes, SyncGroupDataPacket::new, SyncGroupDataPacket::handle);
        getInstance().registerMessage(messageId++, RequestMemberDataPacket.class, RequestMemberDataPacket::toBytes, RequestMemberDataPacket::new, RequestMemberDataPacket::handle);
        getInstance().registerMessage(messageId++, UpdateMemberDataPacket.class, UpdateMemberDataPacket::toBytes, UpdateMemberDataPacket::new, UpdateMemberDataPacket::handle);
        getInstance().registerMessage(messageId++, SyncEntityDataPackPacket.class, SyncEntityDataPackPacket::encode, SyncEntityDataPackPacket::decode, SyncEntityDataPackPacket::handle);
    }

    public static SimpleChannel getInstance() {
        return INSTANCE;
    }
}
