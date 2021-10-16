package de.blutmondgilde.otherlivingbeings.registry;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.api.gui.inventory.tabs.DefaultTabContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(OtherLivingBeings.MOD_ID)
public class OtherLivingBeingsContainer {
    @ObjectHolder("basic_tab_container")
    public static final MenuType<DefaultTabContainer> BASIC_TAB_CONTAINER = null;
}
