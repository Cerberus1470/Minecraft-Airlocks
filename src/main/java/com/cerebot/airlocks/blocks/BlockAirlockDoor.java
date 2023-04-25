package com.cerebot.airlocks.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
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

@SuppressWarnings("NullableProblems")
public class BlockAirlockDoor extends BlockDoorBase {

    private static final AxisAlignedBB[] AIRLOCK_DOOR_AABB = {
            new AxisAlignedBB(0D,0D,0D,1D,2D,0.125D),
            new AxisAlignedBB(1D,0D,0D,0.875D,2D,1D),
            new AxisAlignedBB(1D,0D,1D,0D,2D,0.875D),
            new AxisAlignedBB(0D,0D,1D,0.125D,2D,0D)
    };

//    private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
//    private static final PropertyBool OPEN = PropertyBool.create("open");

    public BlockAirlockDoor(String name, Material material) {
        super(name, material);
        setSoundType(SoundType.METAL);
        setHardness(5.0F);
        setResistance(20.0F);
//        setHarvestLevel("pickaxe", 2);
        setLightLevel(0.0F);
        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(OPEN, false));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
//        System.out.println("The facing value is: " + facing);
        if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
            return this.getDefaultState().withProperty(FACING, EnumFacing.NORTH).withProperty(OPEN, false);
        }
        return this.getDefaultState().withProperty(FACING, facing).withProperty(OPEN, false);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta)).withProperty(OPEN, false);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, OPEN);
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
            return AIRLOCK_DOOR_AABB[state.getValue(FACING).getHorizontalIndex()];
        } catch (IndexOutOfBoundsException e) {
            return AIRLOCK_DOOR_AABB[0];
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
//        System.out.println("Hello, the airlock console has been changed to " + state.getValue(PRESSURIZED));
        if (state.getValue(OPEN)) {
            worldIn.setBlockState(pos, state.withProperty(OPEN, false));
        } else {
            worldIn.setBlockState(pos, state.withProperty(OPEN, true));
        }
        return true;
    }
}
