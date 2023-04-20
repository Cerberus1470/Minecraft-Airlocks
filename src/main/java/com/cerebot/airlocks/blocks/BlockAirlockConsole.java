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


    private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool PRESSURIZED = PropertyBool.create("pressurized");
//    public static final PropertyInteger STATUS_PRESSURE = PropertyInteger.create("status_pressure", 0,3);

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
        if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
            final_facing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(FACING, final_facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((state.getValue(PRESSURIZED) ? 0 : 1) * 4) + (state.getValue(FACING).getHorizontalIndex());
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean stat_press = (meta / 4) == 1;
        EnumFacing facing = EnumFacing.getHorizontal(meta % 4);
        return getDefaultState().withProperty(FACING, facing).withProperty(PRESSURIZED, stat_press);
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
        if (state.getValue(PRESSURIZED)) {
            world.setBlockState(pos, state.withProperty(PRESSURIZED, false));
        } else {
            world.setBlockState(pos, state.withProperty(PRESSURIZED, true));
        }
        return true;
//        state.cycleProperty(PRESSURIZED);
//        switch (state.getValue(STATUS_PRESSURE)) {
//            case 0:
//                world.setBlockState(pos, state.withProperty(STATUS_PRESSURE, 1));
//            case 1:
//                world.setBlockState(pos, state.withProperty(STATUS_PRESSURE, 2));
//            case 2:
//                world.setBlockState(pos, state.withProperty(STATUS_PRESSURE, 3));
//            case 3:
//                world.setBlockState(pos, state.withProperty(STATUS_PRESSURE, 0));
//        }
    }

    @Override
    public void observedNeighborChange(IBlockState observer, World world, BlockPos observer_pos, Block observed, BlockPos observed_pos) {
        if (observer.getBlock() instanceof BlockAirlockConsole && world.getBlockState(observed_pos).getBlock() instanceof BlockCanvas) {

            System.out.println("The observed_pos is " + observed_pos + " and the adjacent block is " + this.getConnectedCanvas(observer, observer_pos));
            if (observed_pos.equals(this.getConnectedCanvas(observer, observer_pos))) {
                if (world.getBlockState(observed_pos).getValue(CANVAS_SIGNAL)) {
//                System.out.println("The observed canvas is transmitting!");
                    world.setBlockState(observer_pos, world.getBlockState(observer_pos).withProperty(PRESSURIZED, true));
                } else {
//                System.out.println("The observed canvas has stopped transmitting!");
                    world.setBlockState(observer_pos, world.getBlockState(observer_pos).withProperty(PRESSURIZED, false));
                }
            }
        }
    }

    public BlockPos getConnectedCanvas(IBlockState state, BlockPos observer_pos) {
        BlockPos[] adjacent_blocks = {observer_pos.north(), observer_pos.east(), observer_pos.south(), observer_pos.west()};
        return adjacent_blocks[state.getValue(FACING).getHorizontalIndex()];
    }
}
