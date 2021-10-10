package de.blutmondgilde.otherlivingbeings;

import de.blutmondgilde.otherlivingbeings.client.OtherLivingBeingsClient;
import de.blutmondgilde.otherlivingbeings.config.OtherLivingBeingsConfig;
import lombok.Getter;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraftforge.api.distmarker.Dist;
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
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::setup);

        instance = this;
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> OtherLivingBeingsClient::init);
    }

    private void setup(final FMLCommonSetupEvent event) {
        AutoConfig.register(OtherLivingBeingsConfig.class, Toml4jConfigSerializer::new);
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static OtherLivingBeingsConfig getConfig() {
        return AutoConfig.getConfigHolder(OtherLivingBeingsConfig.class).getConfig();
    }
}
