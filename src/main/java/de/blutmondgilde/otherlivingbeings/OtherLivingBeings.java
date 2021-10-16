package de.blutmondgilde.otherlivingbeings;

import de.blutmondgilde.otherlivingbeings.capability.OtherLivingBeingsCapManager;
import de.blutmondgilde.otherlivingbeings.client.OtherLivingBeingsClient;
import de.blutmondgilde.otherlivingbeings.config.OtherLivingBeingsConfig;
import de.blutmondgilde.otherlivingbeings.data.jobs.lumberjack.LumberjackDataGenerator;
import de.blutmondgilde.otherlivingbeings.data.jobs.miner.MinerDataGenerator;
import de.blutmondgilde.otherlivingbeings.handler.DataPackHandler;
import de.blutmondgilde.otherlivingbeings.handler.InventoryTabHandler;
import de.blutmondgilde.otherlivingbeings.handler.SkillHandler;
import de.blutmondgilde.otherlivingbeings.network.OtherLivingBeingNetwork;
import de.blutmondgilde.otherlivingbeings.registry.OtherLivingBeingRegistry;
import de.blutmondgilde.otherlivingbeings.registry.SkillRegistry;
import lombok.Getter;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Jankson;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.common.registry.GameRegistry;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
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

    public OtherLivingBeings() {
        instance = this;
        config = AutoConfig.register(OtherLivingBeingsConfig.class, OtherLivingBeings::configSerializer);

        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::setup);
        modBus.addListener(this::dataGen);
        modBus.addListener(OtherLivingBeingsClient::clientSetup);
        OtherLivingBeingRegistry.init();
        SkillRegistry.init(modBus);

        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        SkillHandler.init();
        DataPackHandler.init(forgeBus);
        InventoryTabHandler.init(forgeBus);
        OtherLivingBeingsCapManager.init(modBus, forgeBus);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> OtherLivingBeingsClient::init);
    }

    private void setup(final FMLCommonSetupEvent e) {
        e.enqueueWork(OtherLivingBeingNetwork::registerPackets);
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

    private void dataGen(final GatherDataEvent e) {
        if (e.includeServer()) {
            e.getGenerator().addProvider(new LumberjackDataGenerator(MOD_ID, e.getGenerator().getOutputFolder()));
            e.getGenerator().addProvider(new MinerDataGenerator(MOD_ID, e.getGenerator().getOutputFolder()));
        }
    }
}
