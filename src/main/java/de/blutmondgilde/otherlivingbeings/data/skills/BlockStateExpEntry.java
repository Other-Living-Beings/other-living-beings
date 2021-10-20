package de.blutmondgilde.otherlivingbeings.data.skills;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.fmllegacy.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BlockStateExpEntry implements GeneratableObject<BlockStateExpEntry> {
    @Getter
    @Setter
    public String type;
    @Getter
    @Setter
    public float exp;
    @Getter
    @Setter
    public Block block;
    @Getter
    @Setter
    public BlockState blockState;

    @Override
    public JsonObject toJson() {
        JsonObject root = new JsonObject();
        root.addProperty("type", this.type);
        root.addProperty("exp", this.exp);
        root.addProperty("block", this.block.getRegistryName().toString());

        JsonObject nbt = new JsonObject();
        final BlockState defaultState = this.block.defaultBlockState();

        ArrayList<Property<?>> nonDefaultProperties = new ArrayList<>(defaultState.getValues().keySet());
        nonDefaultProperties.removeIf(property -> isSame(property, defaultState, this.blockState));

        nonDefaultProperties.forEach(property -> nbt.addProperty(property.getName(), this.blockState.getValue(property).toString()));
        root.add("nbt", nbt);

        return root;
    }

    @Override
    public BlockStateExpEntry fromJson(String jsonString) {
        JsonParser parser = new JsonParser();
        JsonObject root = parser.parse(jsonString).getAsJsonObject();
        this.type = root.get("type").getAsString();
        this.exp = root.get("exp").getAsFloat();
        this.block = GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(root.get("block").getAsString()));

        JsonObject nbt = root.getAsJsonObject("nbt");
        BlockState loadedState = this.block.defaultBlockState();
        StateDefinition<Block, BlockState> definition = this.block.getStateDefinition();
        for (Property<?> property : definition.getProperties()) {
            if (nbt.has(property.getName())) {
                loadedState = setValue(loadedState, property, nbt.get(property.getName()).getAsString());
            }
        }
        this.blockState = loadedState;
        return this;
    }

    private <T extends Comparable<T>> boolean isSame(Property<T> property, BlockState defaultState, BlockState currentState) {
        return defaultState.getValue(property).compareTo(currentState.getValue(property)) == 0;
    }

    private <T extends Comparable<T>> BlockState setValue(BlockState currentState, Property<T> pProperty, String pValue) {
        Optional<T> optional = pProperty.getValue(pValue);
        if (optional.isPresent()) {
            currentState = currentState.setValue(pProperty, optional.get());
        }

        return currentState;
    }

    public boolean isValid(BlockState blockState) {
        if (!this.block.getRegistryName().equals(blockState.getBlock().getRegistryName())) return false;
        return isSame(blockState, this.blockState);
    }

    private boolean isSame(BlockState state1, BlockState state2) {
        for (Property<?> property : state1.getProperties()) {
            if (!isSame(property, state1, state2)) {
                return false;
            }
        }

        return true;
    }

    public static List<BlockStateExpEntry> fromBlockState(String type, float exp, BlockState... blockStates) {
        ArrayList<BlockStateExpEntry> list = new ArrayList<>();
        for (BlockState blockState : blockStates) {
            list.add(new BlockStateExpEntry(type, exp, blockState.getBlock(), blockState));
        }
        return list;
    }
}
