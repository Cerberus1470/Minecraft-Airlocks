package com.cerebot.airlocks.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

@SuppressWarnings({"NullableProblems", "deprecation"})
public class BlockWalkway extends BlockButtonBase {

    private static final AxisAlignedBB Walkway_AABB = new AxisAlignedBB(0D,0.625D,0D,1D,1D,1D);
//    private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public BlockWalkway(String name, Material material) {
        super(name, material);

        setSoundType(SoundType.METAL);
        setHardness(5.0F);
        setResistance(20.0F);
        setHarvestLevel("pickaxe", 2);
        setLightLevel(0.9F);
        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
//        EnumFacing final_facing = facing;
//        if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
//            final_facing = EnumFacing.NORTH;
//        }
//        return this.getDefaultState().withProperty(FACING, final_facing);
    }

    @Override
    public boolean canPlaceBlockAt(World p_176196_1_, BlockPos p_176196_2_) {
        return true;
    }

    @Override
    public boolean canPlaceBlockOnSide(World p_176198_1_, BlockPos p_176198_2_, EnumFacing p_176198_3_) {
        return true;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((state.getValue(POWERED) ? 1 : 0) * 4) +
                (((state.getValue(FACING) == EnumFacing.DOWN || state.getValue(FACING) == EnumFacing.UP) ? EnumFacing.NORTH : state.getValue(FACING)).getHorizontalIndex());
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean powered = (meta / 4) == 1;
        EnumFacing facing = EnumFacing.getHorizontal(meta % 4);
        return getDefaultState().withProperty(FACING, facing).withProperty(POWERED, powered);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, POWERED);
    }

    @Override
    public boolean isOpaqueCube(IBlockState p_149662_1_) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState p_149730_1_) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_) {
        return Walkway_AABB;
    }

    @Override
    public int tickRate(World p_149738_1_) {
        return 80;
    }
    @Override
    public boolean canProvidePower(IBlockState state) {
        return false;
    }

    @Override
    public int getWeakPower(IBlockState p_180656_1_, IBlockAccess p_180656_2_, BlockPos p_180656_3_, EnumFacing p_180656_4_) {
        return 0;
    }

    @Override
    public int getStrongPower(IBlockState p_176211_1_, IBlockAccess p_176211_2_, BlockPos p_176211_3_, EnumFacing p_176211_4_) {
        return 0;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer placer, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        super.onBlockActivated(world, pos, state, placer, hand, facing, hitX, hitY, hitZ);
        this.spawnParticles(world, pos, state);

//        float particlePosX = pos.getX();
//        float particlePosY = pos.getY();
//        float particlePosZ = pos.getZ();
////        float particleSize0 = 0.52F;
////        float particleSize1 = 1.0F;
//        switch (state.getValue(FACING)) {
//            case NORTH:
//            case SOUTH:
//                world.spawnParticle(EnumParticleTypes.CLOUD, particlePosX, particlePosY + 1.01F, particlePosZ + 0.5F, 0.3D, 0.5D, 0.0D);
//                world.spawnParticle(EnumParticleTypes.CLOUD, particlePosX + 1.0F, particlePosY + 1.01F, particlePosZ + 0.5F, -0.3D, 0.5D, 0.0D);
//            case EAST:
//            case WEST:
//                world.spawnParticle(EnumParticleTypes.CLOUD, particlePosX + 0.5F, particlePosY + 1.01F, particlePosZ, 0.0D, 0.5D, 0.3D);
//                world.spawnParticle(EnumParticleTypes.CLOUD, particlePosX + 0.5F, particlePosY + 1.01F, particlePosZ + 1.0F, 0.0D, 0.5D, -0.3D);
//        }
//        world.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(true)), 3);
        return true;
    }

    private void spawnParticles(World world, BlockPos pos, IBlockState state) {
        float particlePosX = pos.getX();
        float particlePosY = pos.getY();
        float particlePosZ = pos.getZ();

        if (state.getValue(FACING) == EnumFacing.NORTH || state.getValue(FACING) == EnumFacing.SOUTH) {
            world.spawnParticle(EnumParticleTypes.CLOUD, particlePosX + 0.5F, particlePosY + 1.01F, particlePosZ, 0.0D, 0.5D, 0.3D);
            world.spawnParticle(EnumParticleTypes.CLOUD, particlePosX + 0.5F, particlePosY + 1.01F, particlePosZ + 1.0F, 0.0D, 0.5D, -0.3D);
        } else if (state.getValue(FACING) == EnumFacing.EAST || state.getValue(FACING) == EnumFacing.WEST) {
            world.spawnParticle(EnumParticleTypes.CLOUD, particlePosX, particlePosY + 1.01F, particlePosZ + 0.5F, 0.3D, 0.5D, 0.0D);
            world.spawnParticle(EnumParticleTypes.CLOUD, particlePosX + 1.0F, particlePosY + 1.01F, particlePosZ + 0.5F, -0.3D, 0.5D, 0.0D);
        }
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        world.setBlockState(pos, state.withProperty(POWERED, false));
        this.spawnParticles(world, pos, state);
    }
}
