package de.blutmondgilde.otherlivingbeings;

import de.blutmondgilde.otherlivingbeings.capability.OtherLivingBeingsCapManager;
import de.blutmondgilde.otherlivingbeings.client.OtherLivingBeingsClient;
import de.blutmondgilde.otherlivingbeings.config.OtherLivingBeingsConfig;
import de.blutmondgilde.otherlivingbeings.network.OtherLivingBeingNetwork;
import de.blutmondgilde.otherlivingbeings.registry.SkillRegistry;
import lombok.Getter;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(OtherLivingBeings.MOD_ID)
public class OtherLivingBeings {
    public static final String MOD_ID = "otherlivingbeings";
    private static final Logger LOGGER = LogManager.getLogger();
    @Getter
    private static OtherLivingBeings instance;

    public OtherLivingBeings() {
        instance = this;

        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::setup);
        SkillRegistry.init(modBus);

        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        OtherLivingBeingsCapManager.init(modBus, forgeBus);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> OtherLivingBeingsClient::init);
    }

    private void setup(final FMLCommonSetupEvent event) {
        AutoConfig.register(OtherLivingBeingsConfig.class, Toml4jConfigSerializer::new);
        OtherLivingBeingNetwork.registerPackets();
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static OtherLivingBeingsConfig getConfig() {
        return AutoConfig.getConfigHolder(OtherLivingBeingsConfig.class).getConfig();
    }
}
