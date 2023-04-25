package com.cerebot.airlocks.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static net.minecraft.block.BlockButton.POWERED;

@SuppressWarnings({"NullableProblems", "deprecation"})
public class BlockCanvas extends BlockBase {

    private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool CANVAS_SIGNAL = PropertyBool.create("canvas_signal");
    private static final PropertyBool CORNER = PropertyBool.create("corner");
//    public static final PropertyEnum<Corner_Half> HALF = PropertyEnum.create("half", Corner_Half.class);
    private static final AxisAlignedBB CANVAS_FULL_AABB = new AxisAlignedBB(0.0D,0.0D,0.0D,1.0D,1.0D,1.0D);
    private static final AxisAlignedBB[] CANVAS_CORNER_AABB = {new AxisAlignedBB(0.0D,0.0D,0.5D,1.0D,0.5D,1.0D), // SOUTH
                                                            new AxisAlignedBB(0.0D,0.0D,0.0D,0.5D,0.5D,1.0D), // WEST
                                                            new AxisAlignedBB(0.0D,0.0D,0.0D,1.0D,0.5D,0.5D), // NORTH
                                                            new AxisAlignedBB(0.5D,0.0D,0.0D,1.0D,0.5D,1.0D) // EAST
    };
    public BlockCanvas(String name, Material material) {
        super(name, material);

        setSoundType(SoundType.CLOTH);
        setHardness(5.0F);
        setResistance(20.0F);
        setHarvestLevel("pickaxe", 2);
        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(CANVAS_SIGNAL, false).withProperty(CORNER, false));
        setLightLevel(0.0F);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        EnumFacing new_facing = EnumFacing.NORTH;
        boolean corner = false;
        if (world.getBlockState(pos.down()).getBlock() instanceof BlockCanvas) {
            if (world.getBlockState(pos.north()).getBlock() instanceof BlockCanvas && placer.getHorizontalFacing() == EnumFacing.NORTH) {
                corner = true;
            } else if (world.getBlockState(pos.east()).getBlock() instanceof BlockCanvas&& placer.getHorizontalFacing() == EnumFacing.EAST) {
                corner = true;
                new_facing = EnumFacing.EAST;
            } else if (world.getBlockState(pos.south()).getBlock() instanceof BlockCanvas&& placer.getHorizontalFacing() == EnumFacing.SOUTH) {
                corner = true;
                new_facing = EnumFacing.SOUTH;
            } else if (world.getBlockState(pos.west()).getBlock() instanceof BlockCanvas&& placer.getHorizontalFacing() == EnumFacing.WEST) {
                corner = true;
                new_facing = EnumFacing.WEST;
            }
        }
        return this.getDefaultState().withProperty(FACING, new_facing).withProperty(CANVAS_SIGNAL, false).withProperty(CORNER, corner);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (state.getValue(CORNER)) {
            try {
                return CANVAS_CORNER_AABB[state.getValue(FACING).getHorizontalIndex()];
            } catch (IndexOutOfBoundsException e) {
                return CANVAS_CORNER_AABB[0];
            }
        } else {
            return CANVAS_FULL_AABB;
        }
    }

    @Override
    public boolean isFullCube(IBlockState p_149686_1_) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState p_149662_1_) {
        return false;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((state.getValue(CANVAS_SIGNAL) ? 1 : 0) * 8) + ((state.getValue(CORNER) ? 1 : 0) * 4) + (state.getValue(FACING).getHorizontalIndex());
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        Boolean canvas_signal = (meta / 8) == 1;
        Boolean corner = ((meta / 4) == 1) || ((meta / 4) == 3);
        EnumFacing facing = EnumFacing.getHorizontal(meta % 4);
        return this.getDefaultState().withProperty(CANVAS_SIGNAL, canvas_signal).withProperty(CORNER, corner).withProperty(FACING, facing);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, CANVAS_SIGNAL, CORNER);
    }

    @Override
    public void observedNeighborChange(IBlockState observer, World world, BlockPos observer_pos, Block observed, BlockPos observed_pos) {
        if (observer.getBlock() instanceof BlockCanvas && world.getBlockState(observed_pos).getBlock() instanceof BlockCanvas) {
            if (world.getBlockState(observed_pos).getValue(CANVAS_SIGNAL)) {
//                System.out.println("The observed canvas is transmitting!");
                world.setBlockState(observer_pos, world.getBlockState(observer_pos).withProperty(CANVAS_SIGNAL, true));
            } else {
//                System.out.println("The observed canvas has stopped transmitting!");
                world.setBlockState(observer_pos, world.getBlockState(observer_pos).withProperty(CANVAS_SIGNAL, false));
            }
        }
//        System.out.println("The observer is " + observer.getBlock() + "and the observed is " + observed);
        if (observer.getBlock() instanceof BlockCanvas && world.getBlockState(observed_pos).getBlock() instanceof BlockAirlockConsole) {
            System.out.println("Console attached/changed!");
            if (observer_pos.equals(((BlockAirlockConsole) world.getBlockState(observed_pos).getBlock()).getConnectedCanvas(world.getBlockState(observed_pos), observed_pos))) {
                if (world.getBlockState(observed_pos).getValue(POWERED)) {
                    world.setBlockState(observer_pos, world.getBlockState(observer_pos).withProperty(CANVAS_SIGNAL, true));
                } else {
                    world.setBlockState(observer_pos, world.getBlockState(observer_pos).withProperty(CANVAS_SIGNAL, false));
                }
            }
        }
    }
}
