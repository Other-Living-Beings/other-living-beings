package de.blutmondgilde.otherlivingbeings.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import de.blutmondgilde.otherlivingbeings.data.group.GroupData;
import de.blutmondgilde.otherlivingbeings.data.group.GroupInvite;
import de.blutmondgilde.otherlivingbeings.data.group.GroupProvider;
import de.blutmondgilde.otherlivingbeings.util.ChatMessageUtils;
import de.blutmondgilde.otherlivingbeings.util.TranslationUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GroupCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("group");
        root
                .then(Commands.literal("invite")
                        .then(Commands.argument("target", EntityArgument.player()).executes(GroupCommand::invitePlayer))
                        .then(Commands.literal("accept").then(Commands.argument("uuid", MessageArgument.message()).executes(GroupCommand::inviteAccept)))
                        .then(Commands.literal("deny").then(Commands.argument("uuid", MessageArgument.message()).executes(GroupCommand::denyAccept))))
                .then(Commands.literal("leave").executes(GroupCommand::leave))
                .then(Commands.literal("kick").then(Commands.argument("target", EntityArgument.player()).executes(GroupCommand::kickPlayer)));

        dispatcher.register(root);
    }

    private static int kickPlayer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer target = EntityArgument.getPlayer(context, "target");
        ServerPlayer source = context.getSource().getPlayerOrException();

        GroupProvider.getGroup(source).ifPresent(data -> {
            if (data.isOwner(source)) {
                GroupProvider.removePlayerFromGroup(target);
            } else {
                MutableComponent failureMessage = ChatMessageUtils.createGroupSystemMessage();
                failureMessage.append(TranslationUtils.createGroupMessage("kick.notowner").withStyle(ChatFormatting.RED));
                context.getSource().sendFailure(failureMessage);
            }
        });

        return Command.SINGLE_SUCCESS;
    }

    private static int invitePlayer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer target = EntityArgument.getPlayer(context, "target");
        ServerPlayer source;
        try {
            source = context.getSource().getPlayerOrException();
        } catch (CommandSyntaxException ex) {
            context.getSource().sendFailure(new TextComponent("You can't invite a Player as Console or Command Block!"));
            throw ex;
        }
        //Get Group of Source or creates a new Group
        GroupData groupData = GroupProvider.getGroup(source).orElseGet(() -> {
            GroupData data = new GroupData(UUID.randomUUID(), source.getUUID(), new ArrayList<>(List.of(source.getUUID())));
            GroupProvider.create(data);
            return data;
        });
        //Check Source is Group Owner
        if (!groupData.getPartyOwner().equals(source.getUUID())) {
            ServerPlayer ownerPlayer = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(groupData.getPartyOwner());
            MutableComponent errorMessage = new TextComponent("[");
            errorMessage.append(new TranslatableComponent("otherlivingbeings.messages.group.prefix").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(new Color(0, 131, 234).getRGB()))));
            errorMessage.append("] ");
            throw new CommandSyntaxException(new SimpleCommandExceptionType(errorMessage), new TranslatableComponent("otherlivingbeings.messages.group.invite.notowner", ownerPlayer, target));
        }
        //Create invitation
        GroupInvite invite = GroupProvider.invite(new GroupInvite(groupData, target.getUUID()));
        //Invitation Message
        MutableComponent inviteMessage = ChatMessageUtils.createGroupSystemMessage();
        inviteMessage.append(source.getDisplayName());
        inviteMessage.append(new TextComponent(" "));
        inviteMessage.append(new TranslatableComponent("otherlivingbeings.messages.group.invite"));

        //Accept / Deny Message
        MutableComponent answerMessage = new TranslatableComponent("otherlivingbeings.messages.group.invite.accept")
                .withStyle(Style.EMPTY
                        .withColor(TextColor.fromRgb(new Color(0, 148, 4).getRGB()))
                        .withUnderlined(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/group invite accept " + invite.getInviteId())));
        answerMessage.append(new TextComponent(" ").withStyle(Style.EMPTY
                .withBold(false)
                .withUnderlined(false)));
        answerMessage.append(new TranslatableComponent("otherlivingbeings.messages.group.invite.deny")
                .withStyle(Style.EMPTY
                        .withColor(TextColor.fromRgb(new Color(255, 0, 0).getRGB()))
                        .withUnderlined(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/group invite deny " + invite.getInviteId()))));

        //Send Messages
        target.sendMessage(inviteMessage, Util.NIL_UUID);
        target.sendMessage(answerMessage, Util.NIL_UUID);

        return Command.SINGLE_SUCCESS;
    }

    private static int inviteAccept(CommandContext<CommandSourceStack> commandSourceStackCommandContext) throws CommandSyntaxException {
        UUID invitationId = UUID.fromString(MessageArgument.getMessage(commandSourceStackCommandContext, "uuid").getString());
        Optional<GroupInvite> invitation = GroupProvider.getInvitation(invitationId);

        if (invitation.isPresent()) {
            invitation.get().accept();
        } else {
            throw new CommandSyntaxException(new SimpleCommandExceptionType(ChatMessageUtils.createGroupSystemMessage()), new TranslatableComponent("otherlivingbeings.messages.group.invite.timeout"));
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int denyAccept(CommandContext<CommandSourceStack> commandSourceStackCommandContext) throws CommandSyntaxException {
        UUID invitationId = UUID.fromString(MessageArgument.getMessage(commandSourceStackCommandContext, "uuid").getString());

        Optional<GroupInvite> invitation = GroupProvider.getInvitation(invitationId);

        if (invitation.isPresent()) {
            invitation.get().deny();
        } else {
            throw new CommandSyntaxException(new SimpleCommandExceptionType(ChatMessageUtils.createGroupSystemMessage()), new TranslatableComponent("otherlivingbeings.messages.group.invite.timeout"));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int leave(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer source;
        try {
            source = context.getSource().getPlayerOrException();
        } catch (CommandSyntaxException ex) {
            context.getSource().sendFailure(new TextComponent("You can't leave a Group as Console or Command Block!"));
            throw ex;
        }

        GroupProvider.removePlayerFromGroup(source);

        return Command.SINGLE_SUCCESS;
    }
}
