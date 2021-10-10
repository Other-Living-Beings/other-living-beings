package de.blutmondgilde.otherlivingbeings.client;

import de.blutmondgilde.otherlivingbeings.config.OtherLivingBeingsConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fmlclient.ConfigGuiHandler;

public class OtherLivingBeingsClient {
    public static void init() {
        //Create Config GUI for Client
        ModLoadingContext.get()
                .registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () -> new ConfigGuiHandler.ConfigGuiFactory((client, parent) -> AutoConfig.getConfigScreen(OtherLivingBeingsConfig.class, parent)
                        .get()));
    }
}
