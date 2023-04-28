package com.cerebot.airlocks.blocks;

import com.cerebot.airlocks.Airlocks;
import com.cerebot.airlocks.blocks.itemblocks.ItemBlockDoor;
import com.cerebot.airlocks.init.ModBlocks;
import com.cerebot.airlocks.init.ModItems;
import com.cerebot.airlocks.util.IHasModel;
import com.cerebot.airlocks.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Random;

@SuppressWarnings("NullableProblems")
public class BlockDoorBase extends BlockDoor implements IHasModel
{

    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.25D);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.75D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.75D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.25D, 1.0D, 1.0D);
    public BlockDoorBase(String name, Material material) {
        super(material);
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(Airlocks.airlocks_tab);

        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlockDoor(this).setRegistryName(Objects.requireNonNull(this.getRegistryName())));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        state = state.getActualState(source, pos);
        EnumFacing enumfacing = state.getValue(FACING);
        boolean flag = !state.getValue(OPEN);
        boolean flag1 = state.getValue(HINGE) == BlockDoor.EnumHingePosition.RIGHT;

        switch (enumfacing)
        {
            case EAST:
            default:
                return flag ? EAST_AABB : (flag1 ? NORTH_AABB : SOUTH_AABB);
            case SOUTH:
                return flag ? SOUTH_AABB : (flag1 ? EAST_AABB : WEST_AABB);
            case WEST:
                return flag ? WEST_AABB : (flag1 ? SOUTH_AABB : NORTH_AABB);
            case NORTH:
                return flag ? NORTH_AABB : (flag1 ? WEST_AABB : EAST_AABB);
        }
    }

    @Override
    public Item getItemDropped(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_) {
        return Item.getItemFromBlock(this);
    }

    @Override
    public ItemStack getPickBlock(IBlockState p_getPickBlock_1_, RayTraceResult p_getPickBlock_2_, World p_getPickBlock_3_, BlockPos p_getPickBlock_4_, EntityPlayer p_getPickBlock_5_) {
        return new ItemStack(this);
    }

    @Override
    public void registerModels() {
        Airlocks.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    @Override
    public boolean isOpaqueCube(IBlockState p_149662_1_) {
        return false;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    public SoundEvent getNewOpenSound() {
        return SoundsHandler.BLOCK_DOOR_OPEN;
    }
    public SoundEvent getNewCloseSound() {
        return SoundsHandler.BLOCK_DOOR_CLOSE;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (this.blockMaterial == Material.IRON)
        {
            return false;
        }
        else
        {
            BlockPos blockpos = state.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER ? pos : pos.down();
            IBlockState iblockstate = pos.equals(blockpos) ? state : worldIn.getBlockState(blockpos);

            if (iblockstate.getBlock() != this)
            {
                return false;
            }
            else
            {
                state = iblockstate.cycleProperty(OPEN);
                worldIn.setBlockState(blockpos, state, 10);
                worldIn.markBlockRangeForRenderUpdate(blockpos, pos);
                worldIn.playSound(playerIn, pos, state.getValue(OPEN) ? this.getNewOpenSound() : this.getNewCloseSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                return true;
            }
        }
    }

    @Override
    public void toggleDoor(World worldIn, BlockPos pos, boolean open)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);

        if (iblockstate.getBlock() == this)
        {
            BlockPos blockpos = iblockstate.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER ? pos : pos.down();
            IBlockState state1 = pos == blockpos ? iblockstate : worldIn.getBlockState(blockpos);

            if (state1.getBlock() == this && state1.getValue(OPEN) != open)
            {
                worldIn.setBlockState(blockpos, state1.withProperty(OPEN, open), 10);
                worldIn.markBlockRangeForRenderUpdate(blockpos, pos);
                worldIn.playSound(null, pos, open ? this.getNewOpenSound() : this.getNewCloseSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER)
        {
            BlockPos blockpos = pos.down();
            IBlockState iblockstate = worldIn.getBlockState(blockpos);

            if (iblockstate.getBlock() != this)
            {
                worldIn.setBlockToAir(pos);
            }
            else if (blockIn != this)
            {
                iblockstate.neighborChanged(worldIn, blockpos, blockIn, fromPos);
            }
        }
        else
        {
            boolean flag1 = false;
            BlockPos pos1 = pos.up();
            IBlockState state1 = worldIn.getBlockState(pos1);

            if (state1.getBlock() != this)
            {
                worldIn.setBlockToAir(pos);
                flag1 = true;
            }

            if (!worldIn.getBlockState(pos.down()).isSideSolid(worldIn,  pos.down(), EnumFacing.UP))
            {
                worldIn.setBlockToAir(pos);
                flag1 = true;

                if (state1.getBlock() == this)
                {
                    worldIn.setBlockToAir(pos1);
                }
            }

            if (flag1)
            {
                if (!worldIn.isRemote)
                {
                    this.dropBlockAsItem(worldIn, pos, state, 0);
                }
            }
            else
            {
                boolean flag = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos1);

                if (blockIn != this && (flag || blockIn.getDefaultState().canProvidePower()) && flag != state1.getValue(POWERED))
                {
                    worldIn.setBlockState(pos1, state1.withProperty(POWERED, flag), 2);

                    if (flag != state.getValue(OPEN))
                    {
                        worldIn.setBlockState(pos, state.withProperty(OPEN, flag), 2);
                        worldIn.markBlockRangeForRenderUpdate(pos, pos);
                        worldIn.playSound(null, pos, flag ? this.getNewOpenSound() : this.getNewCloseSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                    }
                }
            }
        }
    }
}
