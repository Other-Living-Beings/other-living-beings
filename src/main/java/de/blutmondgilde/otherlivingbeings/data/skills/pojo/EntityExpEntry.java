package de.blutmondgilde.otherlivingbeings.data.skills.pojo;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.blutmondgilde.otherlivingbeings.data.skills.GeneratableObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EntityExpEntry implements GeneratableObject<EntityExpEntry> {
    @Getter
    @Setter
    public String type;
    @Getter
    @Setter
    public float exp;
    @Getter
    @Setter
    public EntityType<?> entity;

    @Override
    public JsonObject toJson() {
        JsonObject root = new JsonObject();
        root.addProperty("type", this.type);
        root.addProperty("exp", this.exp);
        root.addProperty("entity", this.entity.getRegistryName().toString());
        return root;
    }

    @Override
    public EntityExpEntry fromJson(String jsonString) {
        JsonParser parser = new JsonParser();
        JsonObject root = parser.parse(jsonString).getAsJsonObject();
        this.type = root.get("type").getAsString();
        this.exp = root.get("exp").getAsFloat();
        this.entity = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(root.get("entity").getAsString()));
        return this;
    }

    public boolean isSame(Entity entity) {
        return entity.getType().equals(this.entity);
    }

    public static List<EntityExpEntry> fromEntityType(String type, float exp, EntityType<?>... entityTypes) {
        ArrayList<EntityExpEntry> list = new ArrayList<>();
        for (EntityType<?> entityType : entityTypes) {
            list.add(new EntityExpEntry(type, exp, entityType));
        }
        return list;
    }
}
