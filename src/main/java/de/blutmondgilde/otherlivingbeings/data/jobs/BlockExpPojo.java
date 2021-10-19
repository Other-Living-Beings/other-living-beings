package de.blutmondgilde.otherlivingbeings.data.jobs;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fmllegacy.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BlockExpPojo implements GeneratableObject<BlockExpPojo> {
    @Getter
    @Setter
    public float exp;

    @Getter
    @Setter
    public List<Block> blocks;

    @Override
    public JsonObject toJson() {
        JsonObject root = new JsonObject();
        root.addProperty("exp", this.exp);
        JsonArray blockArray = new JsonArray();
        blocks.forEach(block -> blockArray.add(block.getRegistryName().toString()));
        root.add("blocks", blockArray);
        return root;
    }

    @Override
    public BlockExpPojo fromJson(String jsonString) {
        JsonParser parser = new JsonParser();
        JsonObject root = parser.parse(jsonString).getAsJsonObject();
        this.exp = root.get("exp").getAsFloat();
        this.blocks = new ArrayList<>();
        JsonArray array = root.getAsJsonArray("blocks");
        array.forEach(jsonElement -> {
            ResourceLocation blockLocation = new ResourceLocation(jsonElement.getAsString());
            this.blocks.add(GameRegistry.findRegistry(Block.class).getValue(blockLocation));
        });
        return this;
    }
}
