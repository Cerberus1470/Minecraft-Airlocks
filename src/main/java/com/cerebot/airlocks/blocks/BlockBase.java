package com.cerebot.airlocks.blocks;

import com.cerebot.airlocks.Airlocks;
import com.cerebot.airlocks.init.ModBlocks;
import com.cerebot.airlocks.init.ModItems;
import com.cerebot.airlocks.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import java.util.Objects;

public class BlockBase extends Block implements IHasModel {

    public BlockBase(String name, Material material) {
        super(material);
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(Airlocks.airlocks_tab);

        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(Objects.requireNonNull(this.getRegistryName())));
    }

    @Override
    public void registerModels() {
        Airlocks.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
