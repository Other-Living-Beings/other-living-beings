package de.blutmondgilde.otherlivingbeings.util;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.awt.*;

public class ChatMessageUtils {

    public static MutableComponent createGroupSystemMessage(){
        MutableComponent prefix = new TextComponent("[");
        prefix.append(new TranslatableComponent("otherlivingbeings.messages.group.prefix").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(new Color(0, 131, 234).getRGB()))));
        prefix.append("] ");
        return prefix;
    }
}
