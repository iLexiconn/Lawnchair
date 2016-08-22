package net.ilexiconn.lawnchair.server.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class BlockItem extends ItemBlock {
    public BlockItem(Block block) {
        super(block);
        this.setRegistryName(block.getRegistryName());
    }
}
