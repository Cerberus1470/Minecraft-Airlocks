package com.cerebot.airlocks.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
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

import java.util.ArrayList;
//import static com.cerebot.airlocks.blocks.BlockCanvas.CANVAS_SAFE;
//import static com.cerebot.airlocks.blocks.BlockCanvas.CANVAS_SIGNAL;

@SuppressWarnings({"NullableProblems", "deprecation"})
public class BlockCanvasCorner extends BlockBase {

    private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    private static final AxisAlignedBB[] CANVAS_CORNER_AABB = {new AxisAlignedBB(0.0D,0.0D,0.5D,1.0D,0.5D,1.0D), // SOUTH
            new AxisAlignedBB(0.0D,0.0D,0.0D,0.5D,0.5D,1.0D), // WEST
            new AxisAlignedBB(0.0D,0.0D,0.0D,1.0D,0.5D,0.5D), // NORTH
            new AxisAlignedBB(0.5D,0.0D,0.0D,1.0D,0.5D,1.0D) // EAST
    };

    public BlockCanvasCorner(String name, Material material) {
        super(name, material);
        setSoundType(SoundType.CLOTH);
        setHardness(5.0F);
        setResistance(20.0F);
        setHarvestLevel("pickaxe", 2);
        setDefaultState(this.blockState.getBaseState()
                .withProperty(FACING, EnumFacing.NORTH));
        setLightLevel(0.0F);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase player, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, player.getHorizontalFacing());
    }

    @Override
    public boolean isOpaqueCube(IBlockState p_149662_1_) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState p_149686_1_) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        try {
            return CANVAS_CORNER_AABB[state.getValue(FACING).getHorizontalIndex()];
        } catch (IndexOutOfBoundsException e) {
            return CANVAS_CORNER_AABB[0];
        }
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
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public void observedNeighborChange(IBlockState observer_state, World world, BlockPos observer_pos, Block observed, BlockPos observed_pos) {
        IBlockState observed_state = world.getBlockState(observed_pos);
        observed = observed_state.getBlock();
        BlockPos[] adjacent_blocks = {observer_pos.south(), observer_pos.west(), observer_pos.north(), observer_pos.east()};
        if (observed instanceof BlockCanvas && (!observed_pos.equals(adjacent_blocks[observer_state.getValue(FACING).getOpposite().getHorizontalIndex()]) && !observed_pos.equals(observer_pos.up()))) {

            BlockPos[] connected_canvas = this.getConnectedCanvas(world, observer_pos);
            for (BlockPos connectedCanvas : connected_canvas) {
                world.setBlockState(connectedCanvas, world.getBlockState(connectedCanvas));
//                        .withProperty(CANVAS_SIGNAL, observed_state.getValue(CANVAS_SIGNAL))
//                        .withProperty(CANVAS_SAFE, observed_state.getValue(CANVAS_SAFE)));
            }
        }
//        super.observedNeighborChange(p_observedNeighborChange_1_, p_observedNeighborChange_2_, p_observedNeighborChange_3_, p_observedNeighborChange_4_, p_observedNeighborChange_5_);
    }

    public BlockPos[] getConnectedCanvas(World world, BlockPos pos) {
        BlockPos[] adjacent_blocks = {pos.south(), pos.west(), pos.north(), pos.east(), pos.down()};
        adjacent_blocks[world.getBlockState(pos).getValue(FACING).getOpposite().getHorizontalIndex()] = null;
        ArrayList<BlockPos> connected_canvas = new ArrayList<>();
        for (BlockPos adjacentBlock : adjacent_blocks) {
            if (adjacentBlock != null && world.getBlockState(adjacentBlock).getBlock() instanceof BlockCanvas) {
                connected_canvas.add(adjacentBlock);
            }
        }
        BlockPos[] final_connected_canvas = new BlockPos[connected_canvas.size()];
        return connected_canvas.toArray(final_connected_canvas);
    }

//    public void transmit(World world, BlockPos transmitter, ArrayList<BlockPos> exclude, IBlockState data) {
//        BlockPos[] adjacent_blocks = {transmitter.north(), transmitter.east(), transmitter.up(),
//                transmitter.south(), transmitter.west(), transmitter.down()};
//        for (BlockPos adjacentBlock : adjacent_blocks) {
//            if (adjacentBlock != null && !exclude.contains(adjacentBlock)) {
//                if (world.getBlockState(adjacentBlock).getBlock() instanceof BlockCanvas
//                        || world.getBlockState(adjacentBlock).getBlock() instanceof BlockCanvasCorner
//                ) {
//                    System.out.println("This is a canvas block being updated!");
//                    ArrayList<BlockPos> new_exclude = new ArrayList<>(exclude.size());
//                    new_exclude.addAll(exclude);
//                    new_exclude.add(transmitter);
//                    ((BlockCanvasCorner) world.getBlockState(adjacentBlock).getBlock()).transmit(world, adjacentBlock, new_exclude, data);
//                }
//                if (world.getBlockState(adjacentBlock).getBlock() instanceof BlockAirlockConsole) {
//                    System.out.println("This is a airlock console block being updated!");
//                    world.setBlockState(adjacentBlock, data
//                                    .withProperty(FACING, world.getBlockState(adjacentBlock).getValue(FACING)) // Comment to unlock FACING
//                            .withProperty(ERROR, world.getBlockState(adjacentBlock).getValue(ERROR)) // Comment to unlock ERROR
//                            .withProperty(STATUS_PRESSURE, world.getBlockState(adjacentBlock).getValue(STATUS_PRESSURE)) // Comment to unlock STATUS_PRESSURE
//                    );
//                }
//            }
//        }
//    }
//
//
}
