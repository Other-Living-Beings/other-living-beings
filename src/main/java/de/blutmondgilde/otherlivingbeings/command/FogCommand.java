package de.blutmondgilde.otherlivingbeings.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.data.fog.FogDataProvider;
import de.blutmondgilde.otherlivingbeings.registry.OtherLivingBeingsBlocks;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.server.permission.PermissionAPI;

public class FogCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("fog").requires(commandSourceStack -> {
            try {
                return PermissionAPI.hasPermission(commandSourceStack.getPlayerOrException(), OtherLivingBeings.MOD_ID + ".fogblocks");
            } catch (CommandSyntaxException e) {
                return false;
            }
        });
        root.then(Commands.literal("create").then(Commands.argument("position", BlockPosArgument.blockPos()).executes(FogCommand::create)));
        dispatcher.register(root);
    }

    private static int create(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "position");
        FogDataProvider.addFogTeleportData(player.getUUID(), pos);

        player.addItem(new ItemStack(OtherLivingBeingsBlocks.FOG_BLOCK.asItem()));

        return Command.SINGLE_SUCCESS;
    }
}
