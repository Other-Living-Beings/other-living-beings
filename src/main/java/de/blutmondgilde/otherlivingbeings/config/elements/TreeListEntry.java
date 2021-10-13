package de.blutmondgilde.otherlivingbeings.config.elements;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public class TreeListEntry {
    public Float exp = 1F;
    public List<Block> blocks = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreeListEntry that = (TreeListEntry) o;
        if (!Objects.equals(that.exp, this.exp)) return false;

        boolean allTheSame = true;
        for (Block block : that.blocks) {
            if (!this.blocks.contains(block)) {
                allTheSame = false;
                break;
            }
        }
        return allTheSame;
    }
}
