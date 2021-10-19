package de.blutmondgilde.otherlivingbeings.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.api.capability.OtherLivingBeingsCapability;
import de.blutmondgilde.otherlivingbeings.api.gui.inventory.tabs.AbstractInventoryTab;
import de.blutmondgilde.otherlivingbeings.api.gui.inventory.tabs.DefaultTabContainer;
import de.blutmondgilde.otherlivingbeings.api.gui.inventory.tabs.DefaultTabContainerScreen;
import de.blutmondgilde.otherlivingbeings.client.gui.SkillContainerScreen;
import de.blutmondgilde.otherlivingbeings.config.OtherLivingBeingsConfig;
import de.blutmondgilde.otherlivingbeings.config.widget.BlockListWidget;
import de.blutmondgilde.otherlivingbeings.config.widget.BlockTextField;
import de.blutmondgilde.otherlivingbeings.container.SkillContainer;
import de.blutmondgilde.otherlivingbeings.handler.InventoryTabHandler;
import de.blutmondgilde.otherlivingbeings.network.OtherLivingBeingNetwork;
import de.blutmondgilde.otherlivingbeings.network.packet.toserver.RequestInventoryOpening;
import de.blutmondgilde.otherlivingbeings.network.packet.toserver.RequestOpenSkillContainer;
import de.blutmondgilde.otherlivingbeings.registry.InventoryTabRegistry;
import de.blutmondgilde.otherlivingbeings.registry.OtherLivingBeingsContainer;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import me.shedaniel.autoconfig.util.Utils;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fmlclient.ConfigGuiHandler;
import net.minecraftforge.fmllegacy.common.registry.GameRegistry;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

@OnlyIn(Dist.CLIENT)
public class OtherLivingBeingsClient {
    private static final ConfigEntryBuilder ENTRY_BUILDER = ConfigEntryBuilder.create();
    private static final Minecraft minecraft = Minecraft.getInstance();
    private static final ResourceLocation SKILLS_ICON = new ResourceLocation(OtherLivingBeings.MOD_ID, "textures/gui/tabs/skills_icon.png");

    public static void init() {
        //Create Config GUI for Client
        ModLoadingContext.get()
                .registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () -> new ConfigGuiHandler.ConfigGuiFactory((client, parent) -> AutoConfig.getConfigScreen(OtherLivingBeingsConfig.class, parent)
                        .get()));
        //Create Inventory Tab for Skills
        createSkillInventoryTab();

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        InventoryTabHandler.init(forgeBus);
    }

    private static void createSkillInventoryTab() {
        InventoryTabRegistry.register(new AbstractInventoryTab() {
            @Override
            public void sendOpenContainerPacket() {
                OtherLivingBeingNetwork.getInstance().send(PacketDistributor.SERVER.noArg(), new RequestInventoryOpening());
            }

            @Override
            public Predicate<Screen> isCurrentScreen() {
                return screen -> screen instanceof InventoryScreen;
            }

            @Override
            protected void renderIcon(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
                Minecraft.getInstance().getItemRenderer().renderGuiItem(new ItemStack(Items.CHEST), this.x + 6, this.y + 9);
            }

            @Override
            public List<? extends FormattedText> getToolTip() {
                return List.of(FormattedText.of(I18n.get("otherlivingbeings.tab.inventory.title")));
            }
        });

        InventoryTabRegistry.register(new AbstractInventoryTab() {
            @Override
            public void sendOpenContainerPacket() {
                OtherLivingBeingNetwork.getInstance().send(PacketDistributor.SERVER.noArg(), new RequestOpenSkillContainer());
            }

            @Override
            public Predicate<Screen> isCurrentScreen() {
                return screen -> screen instanceof SkillContainerScreen;
            }

            @Override
            protected void renderIcon(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, SKILLS_ICON);
                float scale = 0.50F;
                poseStack.scale(scale, scale, 1F);
                blit(poseStack, Math.round((this.x + 3) / scale), Math.round((this.y + 3) / scale), 0, 0, 48, 48, 48, 48);
                poseStack.scale(1F / scale, 1F / scale, 1F);
            }

            @Override
            public List<? extends FormattedText> getToolTip() {
                return List.of(FormattedText.of(I18n.get("otherlivingbeings.tab.skills.title")));
            }
        });
    }

    public static void clientSetup(FMLClientSetupEvent e) {
        e.enqueueWork(() -> {
            GuiRegistry registry = AutoConfig.getGuiRegistry(OtherLivingBeingsConfig.class);
            registry.registerTypeProvider((translationKey, configField, blockObject, defaultBlockObject, guiRegistryAccess) -> {
                Block fieldValue = Utils.getUnsafely(configField, blockObject, Blocks.AIR);
                BlockTextField blockWidget = new BlockTextField(
                        new TranslatableComponent(translationKey),
                        fieldValue.getRegistryName().toString(),
                        ENTRY_BUILDER.getResetButtonKey(),
                        () -> {
                            Block block = Utils.getUnsafely(configField, defaultBlockObject, Blocks.AIR);
                            return block.getRegistryName().toString();
                        }, s -> Utils.setUnsafely(configField, blockObject, GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(s))),
                        () -> {
                            if (configField.isAnnotationPresent(ConfigEntry.Gui.Tooltip.class)) {
                                int count = configField.getAnnotation(ConfigEntry.Gui.Tooltip.class).count();
                                TranslatableComponent[] tooltips = new TranslatableComponent[count];
                                for (int i = 0; i < count; i++) {
                                    tooltips[i] = new TranslatableComponent(translationKey + "." + i);
                                }
                                return Optional.of(tooltips);
                            } else {
                                return Optional.empty();
                            }
                        }, configField.isAnnotationPresent(ConfigEntry.Gui.RequiresRestart.class));

                return List.of(blockWidget);
            }, Block.class);

            registry.registerPredicateProvider((translationKey, configField, fieldObject, defaultFieldObject, guiRegistryAccess) -> {
                List<Block> fieldValue = Utils.getUnsafely(configField, fieldObject, List.of(Blocks.AIR));

                return List.of(new BlockListWidget(
                        new TranslatableComponent(translationKey),
                        new ArrayList<>(fieldValue.stream()
                                .map(Block::getRegistryName).filter(Objects::nonNull)
                                .map(ResourceLocation::toString)
                                .distinct()
                                .toList()),
                        false,
                        null,
                        strings -> Utils.setUnsafely(configField, fieldObject, new ArrayList<>(strings
                                .stream()
                                .map(s -> GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(s)))
                                .distinct()
                                .toList())),
                        () -> {
                            List<Block> defaultBlockList = Utils.getUnsafely(configField, defaultFieldObject, List.of(Blocks.AIR));
                            return new ArrayList<>(defaultBlockList.stream()
                                    .map(Block::getRegistryName)
                                    .filter(Objects::nonNull)
                                    .map(ResourceLocation::toString)
                                    .distinct()
                                    .toList());
                        },
                        ENTRY_BUILDER.getResetButtonKey(),
                        configField.isAnnotationPresent(ConfigEntry.Gui.RequiresRestart.class)
                ));
            }, isListOfType(Block.class));
        });

        e.enqueueWork(() -> MenuScreens.register(OtherLivingBeingsContainer.BASIC_TAB_CONTAINER, (MenuScreens.ScreenConstructor) (menu, inventory, title) -> new DefaultTabContainerScreen((DefaultTabContainer) menu, inventory, title)));
        e.enqueueWork(() -> MenuScreens.register(OtherLivingBeingsContainer.SKILL_CONTAINER, (MenuScreens.ScreenConstructor) (menu, inventory, title) -> new SkillContainerScreen((SkillContainer) menu, inventory, title)));
    }

    public static void syncSkills(final CompoundTag tag, final int targetId) {
        OtherLivingBeings.getLogger().debug("Received Player Skills Sync Packet with {} values. Start Handling...", tag.size());

        final long startTime = System.currentTimeMillis();
        final ClientLevel level = minecraft.level;

        if (level == null) {
            OtherLivingBeings.getLogger().fatal("Exception while syncing Skills from Entity {}. ClientWorld is NULL!", targetId);
            return;
        }
        Optional<Entity> target = Optional.ofNullable(level.getEntity(targetId));
        target.ifPresent(entity -> entity.getCapability(OtherLivingBeingsCapability.PLAYER_SKILLS).ifPresent(playerSkills -> playerSkills.deserializeNBT(tag)));

        OtherLivingBeings.getLogger().debug("Applied Player Skill Sync Packet in {} ms", System.currentTimeMillis() - startTime);
    }

    private static Predicate<Field> isListOfType(Type... types) {
        return (field) -> {
            if (List.class.isAssignableFrom(field.getType()) && field.getGenericType() instanceof ParameterizedType) {
                Type[] args = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
                return args.length == 1 && Stream.of(types).anyMatch((type) -> Objects.equals(args[0], type));
            } else {
                return false;
            }
        };
    }

    public static void openInventory() {
        Minecraft.getInstance().setScreen(new InventoryScreen(Minecraft.getInstance().player));
    }
}
