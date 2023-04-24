package com.cerebot.airlocks.blocks;


import com.cerebot.airlocks.Airlocks;
import com.cerebot.airlocks.init.ModBlocks;
import com.cerebot.airlocks.init.ModItems;
import com.cerebot.airlocks.util.IHasModel;
import net.minecraft.block.BlockButton;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Objects;

@SuppressWarnings("NullableProblems")
public class BlockButtonBase extends BlockButton implements IHasModel {

    public BlockButtonBase(String name, Material material) {
        super(false);
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

    @Override
    protected void playClickSound(@Nullable EntityPlayer player, World worldIn, BlockPos pos) {
        worldIn.playSound(player, pos, SoundEvents.AMBIENT_CAVE, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    protected void playReleaseSound(World worldIn, BlockPos pos) {
        worldIn.playSound(null, pos, SoundEvents.AMBIENT_CAVE, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }
}
