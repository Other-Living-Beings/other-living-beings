package de.blutmondgilde.otherlivingbeings.data.group;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupMemberData implements INBTSerializable<CompoundTag> {
    @Getter
    private UUID playerUUID;
    @Getter
    private Component displayName;
    @Getter
    private double health, maxHealth;

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("player", playerUUID);
        tag.putString("name", Component.Serializer.toJson(this.displayName));
        tag.putDouble("hp", this.health);
        tag.putDouble("maxHp", this.maxHealth);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.playerUUID = nbt.getUUID("player");
        this.displayName = Component.Serializer.fromJson(nbt.getString("name"));
        this.health = nbt.getDouble("hp");
        this.maxHealth = nbt.getDouble("maxHp");
    }

    public static GroupMemberData deserialize(CompoundTag nbt) {
        GroupMemberData data = new GroupMemberData();
        data.deserializeNBT(nbt);
        return data;
    }
}
