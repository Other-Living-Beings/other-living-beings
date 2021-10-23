package de.blutmondgilde.otherlivingbeings.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class FogBlock extends Block {
    public static final IntegerProperty teleportX = IntegerProperty.create("teleport-x", 0, Integer.MAX_VALUE);
    public static final BooleanProperty teleport_x_negative = BooleanProperty.create("teleport-x-negative");
    public static final IntegerProperty teleportY = IntegerProperty.create("teleport-y", 0, Integer.MAX_VALUE);
    public static final IntegerProperty teleportZ = IntegerProperty.create("teleport-z", 0, Integer.MAX_VALUE);
    public static final BooleanProperty teleport_z_negative = BooleanProperty.create("teleport-z-negative");

    public FogBlock() {
        super(Properties.of(new Material.Builder(MaterialColor.COLOR_GRAY)
                        .nonSolid().build())
                .noDrops()
                .strength(-1.0F, 3600000.0F)
                .isValidSpawn((p_61031_, p_61032_, p_61033_, p_61034_) -> false));
        registerDefaultState(getStateDefinition().any()
                .setValue(teleportX, 0)
                .setValue(teleport_x_negative, false)
                .setValue(teleportY, 80)
                .setValue(teleportZ, 0)
                .setValue(teleport_z_negative, false)
        );
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {

    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState pOldState, boolean pIsMoving) {
        super.onPlace(blockState, level, blockPos, pOldState, pIsMoving);
    }
}
