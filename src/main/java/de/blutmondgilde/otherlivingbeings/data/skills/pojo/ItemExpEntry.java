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
import net.minecraft.world.item.Item;
import net.minecraftforge.fmllegacy.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemExpEntry implements GeneratableObject<ItemExpEntry> {
    @Getter
    @Setter
    public String type;
    @Getter
    @Setter
    public float exp;
    @Getter
    @Setter
    public Item item;

    @Override
    public JsonObject toJson() {
        JsonObject root = new JsonObject();
        root.addProperty("type", this.type);
        root.addProperty("exp", this.exp);
        root.addProperty("id", this.item.getRegistryName().toString());
        return root;
    }

    @Override
    public ItemExpEntry fromJson(String jsonString) {
        JsonParser parser = new JsonParser();
        JsonObject root = parser.parse(jsonString).getAsJsonObject();
        this.type = root.get("type").getAsString();
        this.exp = root.get("exp").getAsFloat();
        this.item = GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation(root.get("id").getAsString()));
        return this;
    }

    public boolean isSame(Item item) {
        return item.getRegistryName().equals(this.item.getRegistryName());
    }

    public static List<ItemExpEntry> fromItems(String type, float exp, Item... items) {
        ArrayList<ItemExpEntry> list = new ArrayList<>();
        for (Item item : items) {
            list.add(new ItemExpEntry(type, exp, item));
        }
        return list;
    }
}
