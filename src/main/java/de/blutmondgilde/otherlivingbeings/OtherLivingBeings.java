package de.blutmondgilde.otherlivingbeings;

import de.blutmondgilde.otherlivingbeings.client.ClientProxy;
import de.blutmondgilde.otherlivingbeings.config.OtherLivingBeingsConfig;
import lombok.Getter;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Jankson;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(OtherLivingBeings.MOD_ID)
public class OtherLivingBeings {
    public static final String MOD_ID = "otherlivingbeings";
    private static final Logger LOGGER = LogManager.getLogger();
    @Getter
    private static OtherLivingBeings instance;
    @Getter
    private static ConfigHolder<OtherLivingBeingsConfig> config;
    @Getter
    private final CommonProxy proxy;

    public OtherLivingBeings() {
        instance = this;
        config = AutoConfig.register(OtherLivingBeingsConfig.class, OtherLivingBeings::configSerializer);
        proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
        proxy.preInit(FMLJavaModLoadingContext.get().getModEventBus(),MinecraftForge.EVENT_BUS);
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    private static JanksonConfigSerializer<OtherLivingBeingsConfig> configSerializer(Config config, Class<OtherLivingBeingsConfig> aClass) {
        Jankson.Builder builder = Jankson.builder()
            .registerSerializer(Block.class, (block, marshaller) -> {
                String id = block.getRegistryName().toString();
                return marshaller.serialize(id);
            })
            .registerDeserializer(String.class, Block.class, (s, marshaller) -> GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(s)));

        return new JanksonConfigSerializer<>(config, aClass, builder.build());
    }
}
