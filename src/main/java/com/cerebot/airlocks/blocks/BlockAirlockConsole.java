package com.cerebot.airlocks.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
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

import java.util.Random;

import static com.cerebot.airlocks.blocks.BlockCanvas.CANVAS_SIGNAL;

@SuppressWarnings({"NullableProblems"})
public class BlockAirlockConsole extends BlockButtonBase {

    public static final AxisAlignedBB[] AIRLOCK_CONSOLE_AABB = {
            new AxisAlignedBB(0.1875D, 0.0625D, 0D, 0.8125D, 0.9375D, 0.0625D), //South
            new AxisAlignedBB(0.9375D, 0.0625D, 0.1875D, 1D, 0.9375D, 0.8125D), //West
            new AxisAlignedBB(0.1875D, 0.0625D, 0.9375D, 0.8125D, 0.9375D, 1D), //North
            new AxisAlignedBB(0D, 0.0625D, 0.1875D, 0.0625D, 0.9375D, 0.8125D) //East
    };


//    private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool PRESSURIZED = PropertyBool.create("pressurized");
//    public static final PropertyInteger STATUS_PRESSURE = PropertyInteger.create("status_pressure", 0,3);

    public BlockAirlockConsole(String name, Material material) {
        super(name, material);

        setSoundType(SoundType.METAL);
        setHardness(5.0F);
        setResistance(20.0F);
        setHarvestLevel("pickaxe", 2);
        setLightLevel(0.8F);
        setDefaultState(this.blockState.getBaseState().withProperty(POWERED, false).withProperty(PRESSURIZED, false));
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
        return ((state.getValue(POWERED) ? 1 : 0) * 8) + ((state.getValue(PRESSURIZED) ? 1 : 0) * 4) +
                (((state.getValue(FACING) == EnumFacing.DOWN || state.getValue(FACING) == EnumFacing.UP) ? EnumFacing.NORTH : state.getValue(FACING)).getHorizontalIndex());
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean powered = (meta / 8) == 1;
        boolean stat_press = (meta / 4) == 1;
        EnumFacing facing = EnumFacing.getHorizontal(meta % 4);
        return getDefaultState().withProperty(FACING, facing).withProperty(PRESSURIZED, stat_press).withProperty(POWERED, powered);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, POWERED, PRESSURIZED);
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
    public int tickRate(World world) {
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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        super.onBlockActivated(world, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
//        if (state.getValue(POWERED)) {
//            return true;
//        } else {
//            state = state.withProperty(POWERED, true);
//        }
        System.out.println("Actually I need to check my schedule!");
//        world.setBlockState(pos, state);
        return true;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        IBlockState final_state = state.withProperty(POWERED, false);
        if (state.getValue(PRESSURIZED)) {
            final_state = final_state.withProperty(PRESSURIZED, false);
        } else {
            final_state = final_state.withProperty(PRESSURIZED, true);
        }
        world.setBlockState(pos, final_state);
    }

    @Override
    public void observedNeighborChange(IBlockState observer, World world, BlockPos observer_pos, Block observed, BlockPos observed_pos) {
        if (observer.getBlock() instanceof BlockAirlockConsole && world.getBlockState(observed_pos).getBlock() instanceof BlockCanvas) {

            System.out.println("The observed_pos is " + observed_pos + " and the adjacent block is " + this.getConnectedCanvas(observer, observer_pos));
            if (observed_pos.equals(this.getConnectedCanvas(observer, observer_pos))) {
                if (world.getBlockState(observed_pos).getValue(CANVAS_SIGNAL)) {
//                System.out.println("The observed canvas is transmitting!");
//                    super.onBlockActivated(world, observer_pos, observer, playerIn, hand, facing, hitX, hitY, hitZ);
                    world.setBlockState(observer_pos, world.getBlockState(observer_pos).withProperty(POWERED, true));
                } else {
//                System.out.println("The observed canvas has stopped transmitting!");
                    world.setBlockState(observer_pos, world.getBlockState(observer_pos).withProperty(POWERED, false));
                }
            }
        }
    }

    public BlockPos getConnectedCanvas(IBlockState state, BlockPos observer_pos) {
        BlockPos[] adjacent_blocks = {observer_pos.north(), observer_pos.east(), observer_pos.south(), observer_pos.west()};
        return adjacent_blocks[state.getValue(FACING).getHorizontalIndex()];
    }
}
