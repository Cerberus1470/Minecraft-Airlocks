package com.cerebot.airlocks.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static com.cerebot.airlocks.blocks.BlockCanvas.CANVAS_SIGNAL;

@SuppressWarnings({"NullableProblems", "deprecation"})
public class BlockAirlockConsole extends BlockBase {

    public static final AxisAlignedBB[] AIRLOCK_CONSOLE_AABB = {
            new AxisAlignedBB(0.1875D, 0.0625D, 0D, 0.8125D, 0.9375D, 0.0625D), //South
            new AxisAlignedBB(0.9375D, 0.0625D, 0.1875D, 1D, 0.9375D, 0.8125D), //West
            new AxisAlignedBB(0.1875D, 0.0625D, 0.9375D, 0.8125D, 0.9375D, 1D), //North
            new AxisAlignedBB(0D, 0.0625D, 0.1875D, 0.0625D, 0.9375D, 0.8125D) //East
    };


    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool PRESSURIZED = PropertyBool.create("pressurized");

    public BlockAirlockConsole(String name, Material material) {
        super(name, material);

        setSoundType(SoundType.METAL);
        setHardness(5.0F);
        setResistance(20.0F);
        setHarvestLevel("pickaxe", 2);
        setLightLevel(0.8F);
        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(PRESSURIZED, false));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        System.out.println("The facing value is: " + facing + " and the opposite side is " + (facing.getHorizontalIndex() + 2));
        EnumFacing final_facing = facing;
//        int index = facing.getHorizontalIndex();
//        if (index == -1) {
//            index = 0;
//        }
//        int index = Math.max(facing.getHorizontalIndex(), 0);
//        BlockPos[] adjacent_blocks = {pos.north(),
//                                        pos.east(),
//                                        pos.south(),
//                                        pos.west()};
//        if (world.getBlockState(adjacent_blocks[index]).getBlock().getLocalizedName().toLowerCase().contains("canvas")) {
//            System.out.println("I am attached to a canvas block!");
//        }
        if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
            final_facing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(FACING, final_facing).withProperty(PRESSURIZED, false);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta)).withProperty(PRESSURIZED, false);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, PRESSURIZED);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        try {
            return AIRLOCK_CONSOLE_AABB[state.getValue(FACING).getHorizontalIndex()];
        } catch (IndexOutOfBoundsException e) {
            return AIRLOCK_CONSOLE_AABB[0];
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
//        super.onBlockActivated(world, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
//        int index = Math.max(facing.getHorizontalIndex(), 0);
//        BlockPos[] adjacent_blocks = {pos.north(),
//                pos.east(),
//                pos.south(),
//                pos.west()};
//        if (world.getBlockState(adjacent_blocks[index]).getBlock().getLocalizedName().toLowerCase().contains("canvas")) {
//            System.out.println("I am attached to a canvas block!");
//        }
//        System.out.println("Hello, the airlock console has been changed to " + state.getValue(PRESSURIZED));
        if (state.getValue(PRESSURIZED)) {
//            if (CONNECTED_CANVAS != null) {
//                world.setBlockState(CONNECTED_CANVAS, world.getBlockState(CONNECTED_CANVAS).withProperty(CANVAS_SIGNAL, true));
//                BlockCanvas.updateSignal(world);
//            }
            world.setBlockState(pos, state.withProperty(PRESSURIZED, false));
        } else {
//            if (CONNECTED_CANVAS != null) {
//                world.setBlockState(CONNECTED_CANVAS, world.getBlockState(CONNECTED_CANVAS).withProperty(CANVAS_SIGNAL, false));
//            }
            world.setBlockState(pos, state.withProperty(PRESSURIZED, true));
        }
        return true;
    }

    @Override
    public void observedNeighborChange(IBlockState observer, World world, BlockPos observer_pos, Block observed, BlockPos observed_pos) {
        if (observer.getBlock() instanceof BlockAirlockConsole && world.getBlockState(observed_pos).getBlock() instanceof BlockCanvas) {
            if (world.getBlockState(observed_pos).getValue(CANVAS_SIGNAL)) {
                System.out.println("The observed canvas is transmitting!");
                world.setBlockState(observer_pos, world.getBlockState(observer_pos).withProperty(PRESSURIZED, true));
            } else {
//                System.out.println("The observed canvas has stopped transmitting!");
                world.setBlockState(observer_pos, world.getBlockState(observer_pos).withProperty(PRESSURIZED, false));
            }
        }
    }
}
