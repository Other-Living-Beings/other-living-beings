package de.blutmondgilde.otherlivingbeings.network;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.network.packet.SyncDataPack;
import de.blutmondgilde.otherlivingbeings.network.packet.SyncSkillsPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public class OtherLivingBeingNetwork {
    private static final String PROTOCOL_VERSION = String.valueOf(1);
    private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(OtherLivingBeings.MOD_ID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void registerPackets() {
        int messageId = 1;
        getInstance().registerMessage(messageId++, SyncSkillsPacket.class, SyncSkillsPacket::encode, SyncSkillsPacket::decode, SyncSkillsPacket::handle);
        getInstance().registerMessage(messageId++, SyncDataPack.class, SyncDataPack::encode, SyncDataPack::decode, SyncDataPack::handle);
    }

    public static SimpleChannel getInstance() {
        return INSTANCE;
    }
}
