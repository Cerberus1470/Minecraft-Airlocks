package com.cerebot.airlocks.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

//import static com.cerebot.airlocks.blocks.BlockCanvas.CANVAS_SIGNAL;

@SuppressWarnings({"NullableProblems", "deprecation"})
public class BlockWalkway extends BlockBase {

    private static final AxisAlignedBB Walkway_AABB = new AxisAlignedBB(0D,0.625D,0D,1D,1D,1D);
    private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    public BlockWalkway(String name, Material material) {
        super(name, material);

        setSoundType(SoundType.METAL);
        setHardness(5.0F);
        setResistance(20.0F);
        setHarvestLevel("pickaxe", 2);
        setLightLevel(0.9F);
        setDefaultState(this.blockState.getBaseState()
                .withProperty(FACING, EnumFacing.NORTH)
                .withProperty(POWERED, false));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
//        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

//    @Override
//    public boolean canPlaceBlockAt(World p_176196_1_, BlockPos p_176196_2_) {
//        return true;
//    }
//
//    @Override
//    public boolean canPlaceBlockOnSide(World p_176198_1_, BlockPos p_176198_2_, EnumFacing p_176198_3_) {
//        return true;
//    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((state.getValue(POWERED) ? 1 : 0) * 4 +
                ((state.getValue(FACING) == EnumFacing.DOWN || state.getValue(FACING) == EnumFacing.UP) ? EnumFacing.NORTH : state.getValue(FACING)).getHorizontalIndex());
//        return ((state.getValue(POWERED) ? 1 : 0) * 4) +
//                (((state.getValue(FACING) == EnumFacing.DOWN || state.getValue(FACING) == EnumFacing.UP) ? EnumFacing.NORTH : state.getValue(FACING)).getHorizontalIndex());
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean powered = (meta / 4) == 1;
        EnumFacing facing = EnumFacing.getHorizontal(meta % 4);
        return getDefaultState().withProperty(FACING, facing)
                                .withProperty(POWERED, powered);
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
    public void observedNeighborChange(IBlockState observer_state, World world, BlockPos observer_pos, Block observed, BlockPos observed_pos) {
        IBlockState observed_state = world.getBlockState(observed_pos);
        observed = observed_state.getBlock();
//        if (observed instanceof BlockCanvas) {
//            if (observed_state.getValue(CANVAS_SIGNAL) % 2 == 1) {
//                world.setBlockState(observer_pos, observer_state.withProperty(POWERED, true));
//            } else {
//                world.setBlockState(observer_pos, observer_state.withProperty(POWERED, false));
//            }
//        }
        if (observed instanceof BlockWalkway) {
            world.setBlockState(observer_pos, observer_state.withProperty(POWERED, observed_state.getValue(POWERED)));
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState p_hasTileEntity_1_) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World p_createTileEntity_1_, IBlockState p_createTileEntity_2_) {
        return new WalkwayTE();
    }

    private static void spawnParticles(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        float particlePosX = pos.getX();
        float particlePosY = pos.getY();
        float particlePosZ = pos.getZ();

        if (state.getValue(FACING) == EnumFacing.EAST || state.getValue(FACING) == EnumFacing.WEST) {
            world.spawnParticle(EnumParticleTypes.CLOUD, particlePosX + 0.5F, particlePosY + 1.01F, particlePosZ, 0.0D, 0.5D, 0.3D);
            world.spawnParticle(EnumParticleTypes.CLOUD, particlePosX + 0.5F, particlePosY + 1.01F, particlePosZ + 1.0F, 0.0D, 0.5D, -0.3D);
        } else if (state.getValue(FACING) == EnumFacing.NORTH || state.getValue(FACING) == EnumFacing.SOUTH) {
            world.spawnParticle(EnumParticleTypes.CLOUD,  particlePosX, particlePosY + 1.01F, particlePosZ + 0.5F, 0.3D, 0.5D, 0.0D);
            world.spawnParticle(EnumParticleTypes.CLOUD, particlePosX + 1.0F, particlePosY + 1.01F, particlePosZ + 0.5F, -0.3D, 0.5D, 0.0D);
        }
    }

    public static class WalkwayTE extends TileEntity implements ITickable {
        public WalkwayTE() {}

        @Override
        public void update() {
            if (this.getBlockType() instanceof BlockWalkway && this.getWorld().getBlockState(this.getPos()).getValue(POWERED)) {
                BlockWalkway.spawnParticles(this.getWorld(), this.getPos());
            }
        }
    }
}
