package com.cerebot.airlocks.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
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

@SuppressWarnings({"NullableProblems", "deprecation"})
public class BlockWalkway extends BlockBase {

    private static final AxisAlignedBB Walkway_AABB = new AxisAlignedBB(0D,0.625D,0D,1D,1D,1D);
    private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public BlockWalkway(String name, Material material) {
        super(name, material);

        setSoundType(SoundType.METAL);
        setHardness(5.0F);
        setResistance(20.0F);
        setHarvestLevel("pickaxe", 2);
        setLightLevel(0.9F);
        setDefaultState(this.blockState.getBaseState());
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer placer, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        float particlePosX = pos.getX();
        float particlePosY = pos.getY();
        float particlePosZ = pos.getZ();
//        float particleSize0 = 0.52F;
//        float particleSize1 = 1.0F;
        for (int i=0;i<50;i++) {
            switch (state.getValue(FACING)) {
                case NORTH:
                case SOUTH:
                    world.spawnParticle(EnumParticleTypes.CLOUD, particlePosX, particlePosY + 1.01F, particlePosZ + 0.5F, 0.3D, 0.5D, 0.0D);
                    world.spawnParticle(EnumParticleTypes.CLOUD, particlePosX + 1.0F, particlePosY + 1.01F, particlePosZ + 0.5F, -0.3D, 0.5D, 0.0D);
                case EAST:
                case WEST:
                    world.spawnParticle(EnumParticleTypes.CLOUD, particlePosX + 0.5F, particlePosY + 1.01F, particlePosZ, 0.0D, 0.5D, 0.3D);
                    world.spawnParticle(EnumParticleTypes.CLOUD, particlePosX + 0.5F, particlePosY + 1.01F, particlePosZ + 1.0F, 0.0D, 0.5D, -0.3D);
            }
        }
        return true;
    }
}
