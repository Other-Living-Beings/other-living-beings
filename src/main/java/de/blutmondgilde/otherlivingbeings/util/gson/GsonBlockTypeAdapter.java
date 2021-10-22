package de.blutmondgilde.otherlivingbeings.util.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fmllegacy.common.registry.GameRegistry;

import java.io.IOException;

public class GsonBlockTypeAdapter extends TypeAdapter<Block> {
    @Override
    public void write(JsonWriter out, Block value) throws IOException {
        out.beginObject()
                .name("id")
                .value(value.getRegistryName().toString())
                .endObject();
    }

    @Override
    public Block read(JsonReader in) throws IOException {
        Block block = Blocks.AIR;
        in.beginObject();
        String fieldName = null;

        while (in.hasNext()) {
            JsonToken token = in.peek();
            if (token.equals(JsonToken.NAME)) {
                fieldName = in.nextName();
            }

            if ("id".equals(fieldName)) {
                token = in.peek();
                block = GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(in.nextString()));
            }
        }

        return block;
    }
}
