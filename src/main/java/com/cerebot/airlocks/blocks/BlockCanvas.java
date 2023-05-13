package com.cerebot.airlocks.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

@SuppressWarnings({"NullableProblems", "deprecation"})
public class BlockCanvas extends BlockBase {
//    public static final PropertyInteger CANVAS_SIGNAL = PropertyInteger.create("canvas_signal", 0, 3);
//    private static final PropertyEnum<CanvasType> CANVAS_TYPE = PropertyEnum.create("canvas_type", CanvasType.class);
//    public static final PropertyEnum<Corner_Half> HALF = PropertyEnum.create("half", Corner_Half.class);
//    public static final PropertyBool CANVAS_SAFE = PropertyBool.create("canvas_safe");
    private static final AxisAlignedBB CANVAS_FULL_AABB = new AxisAlignedBB(0.0D,0.0D,0.0D,1.0D,1.0D,1.0D);
    public BlockCanvas(String name, Material material) {
        super(name, material);

        setSoundType(SoundType.CLOTH);
        setHardness(5.0F);
        setResistance(20.0F);
        setHarvestLevel("pickaxe", 2);
        setDefaultState(this.blockState.getBaseState());
//                .withProperty(CANVAS_TYPE, BlockCanvas.CanvasType.BLOCK)
//                .withProperty(CANVAS_SIGNAL, 0)
//                .withProperty(CANVAS_SAFE, false));
        setLightLevel(0.0F);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
//        EnumFacing new_facing = EnumFacing.NORTH;
//        boolean corner = false;
//        if (world.getBlockState(pos.down()).getBlock() instanceof BlockCanvas && !world.getBlockState(pos.down()).getValue(CORNER)) {
//            if (world.getBlockState(pos.north()).getBlock() instanceof BlockCanvas && !world.getBlockState(pos.north()).getValue(CORNER) && placer.getHorizontalFacing() == EnumFacing.NORTH) {
//                corner = true;
//            } else if (world.getBlockState(pos.east()).getBlock() instanceof BlockCanvas && !world.getBlockState(pos.east()).getValue(CORNER) && placer.getHorizontalFacing() == EnumFacing.EAST) {
//                corner = true;
//                new_facing = EnumFacing.EAST;
//            } else if (world.getBlockState(pos.south()).getBlock() instanceof BlockCanvas && !world.getBlockState(pos.south()).getValue(CORNER) && placer.getHorizontalFacing() == EnumFacing.SOUTH) {
//                corner = true;
//                new_facing = EnumFacing.SOUTH;
//            } else if (world.getBlockState(pos.west()).getBlock() instanceof BlockCanvas && !world.getBlockState(pos.west()).getValue(CORNER) && placer.getHorizontalFacing() == EnumFacing.WEST) {
//                corner = true;
//                new_facing = EnumFacing.WEST;
//            }
//        }
        return this.getDefaultState();
//                .withProperty(CANVAS_SAFE, false)
//                .withProperty(FACING, new_facing)
//                .withProperty(CANVAS_SIGNAL, 0);
//                .withProperty(CORNER, corner);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
//        if (state.getValue(CORNER)) {
//            try {
//                return CANVAS_CORNER_AABB[state.getValue(FACING).getHorizontalIndex()];
//            } catch (IndexOutOfBoundsException e) {
//                return CANVAS_CORNER_AABB[0];
//            }
//        } else {
            return CANVAS_FULL_AABB;
//        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
//                (state.getValue(CANVAS_TYPE).getMeta() * 8) +
//                ((state.getValue(CANVAS_SAFE) ? 0 : 1) * 4) +
//                (state.getValue(CANVAS_SIGNAL));
//                ((state.getValue(CORNER) ? 1 : 0) * 4) +
//                (state.getValue(FACING).getHorizontalIndex());
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
//        int type = (meta / 8);
//        boolean safe = (meta / 4) == 1;
//        int canvas_signal = meta % 4;
//        boolean corner = (meta / 4) % 2 == 1;
//        EnumFacing facing = EnumFacing.getHorizontal(meta % 4);
        return this.getDefaultState();
//                .withProperty(CANVAS_TYPE, CanvasType.byMetadata(type))
//                .withProperty(CANVAS_SAFE, safe)
//                .withProperty(CANVAS_SIGNAL, canvas_signal);
//                .withProperty(CORNER, corner)
//                .withProperty(FACING, facing);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this);
    }

//    @Override
//    public void observedNeighborChange(IBlockState observer_state, World world, BlockPos observer_pos, Block observed, BlockPos observed_pos) {
//        IBlockState observed_state = world.getBlockState(observed_pos);
//        observed = observed_state.getBlock();
//        if (observed instanceof BlockCanvas) {
//            transmit(world, observer_pos, new ArrayList<>());
//            if (observed_state.getValue(CANVAS_SIGNAL)) {
//                System.out.println("The observed canvas is transmitting!");
//            System.out.println("The observed block is on instance: " + observed);
//            BlockPos[] connected_canvas = this.getConnectedCanvas(world, observer_pos);
//            for (BlockPos connectedCanvas : connected_canvas) {
//                world.setBlockState(connectedCanvas, world.getBlockState(connectedCanvas)
//                        .withProperty(CANVAS_SIGNAL, observed_state.getValue(CANVAS_SIGNAL))
//                        .withProperty(CANVAS_SAFE, observed_state.getValue(CANVAS_SAFE)));
//            }
//            world.setBlockState(observer_pos, observer_state.withProperty(CANVAS_SIGNAL, observed_state.getValue(CANVAS_SIGNAL))
//                    .withProperty(CANVAS_SAFE, observed_state.getValue(CANVAS_SAFE)));
//            } else {
//                System.out.println("The observed canvas has stopped transmitting!");
//                world.setBlockState(observer_pos, observer_state.withProperty(CANVAS_SIGNAL, false));
//            }
//        }
//        System.out.println("The observer is " + observer.getBlock() + "and the observed is " + observed);
//        if (observed instanceof BlockAirlockConsole) {
//            System.out.println("Console attached/changed!");
//            for (BlockPos pos : transmit(world, observer_pos, new ArrayList<>(), observed_state)) {
//                world.setBlockState(pos, observed_state.withProperty(FACING, world.getBlockState(pos).getValue(FACING)));
//            }
//            if (observer_pos.equals(((BlockAirlockConsole) observed).getConnectedCanvas(observed_state, observed_pos))) {
//                world.setBlockState(observer_pos, observer_state.withProperty(CANVAS_SIGNAL,
//                        (observed_state.getValue(STATUS_PRESSURE))));
//                world.setBlockState(observed_pos, observed_state.withProperty(ERROR, 0));
//            }
//        }
//        if (observed instanceof BlockDoorBase) {
//            world.setBlockState(observer_pos, observer_state.withProperty(CANVAS_SAFE, !observed_state.getValue(OPEN)));
//        }
//    }

//    public BlockPos[] getConnectedCanvas(World world, BlockPos pos) {
//        BlockPos[] adjacent_blocks = {pos.north(), pos.east(), pos.south(), pos.west(), pos.up(), pos.down()};
//        ArrayList<BlockPos> connected_canvas = new ArrayList<>();
//        for (BlockPos adjacentBlock : adjacent_blocks) {
//            if (world.getBlockState(adjacentBlock).getBlock() instanceof BlockCanvas) {
//                connected_canvas.add(adjacentBlock);
//            }
//        }
//        BlockPos[] final_connected_canvas = new BlockPos[connected_canvas.size()];
//        return connected_canvas.toArray(final_connected_canvas);
//    }

//    public ArrayList<BlockPos> transmit(World world, BlockPos transmitter, ArrayList<BlockPos> exclude, IBlockState data) {
//        BlockPos[] adjacent_blocks = {transmitter.north(), transmitter.east(), transmitter.up(),
//                transmitter.south(), transmitter.west(), transmitter.down()};
//        ArrayList<BlockPos> consoles = new ArrayList<>();
//        for (BlockPos adjacentBlock : adjacent_blocks) {
//            if (adjacentBlock != null && !exclude.contains(adjacentBlock)) {
//                if (world.getBlockState(adjacentBlock).getBlock() instanceof BlockCanvas ||
//                        world.getBlockState(adjacentBlock).getBlock() instanceof BlockCanvasCorner) {
////                    System.out.println("This is a canvas block being updated!");
//                    ArrayList<BlockPos> new_exclude = new ArrayList<>(exclude.size());
//                    new_exclude.addAll(exclude);
//                    new_exclude.add(transmitter);
//                    consoles.addAll(((BlockCanvas) world.getBlockState(adjacentBlock).getBlock()).transmit(world, adjacentBlock, new_exclude, data));
//                }
//                if (world.getBlockState(adjacentBlock).getBlock() instanceof BlockAirlockConsole) {
////                    System.out.println("This is an airlock console block being updated!");
//                    consoles.add(adjacentBlock);
////                    world.setBlockState(adjacentBlock, data
////                            .withProperty(FACING, world.getBlockState(adjacentBlock).getValue(FACING)) // Comment to unlock FACING
////                            .withProperty(ERROR, world.getBlockState(adjacentBlock).getValue(ERROR)) // Comment to unlock ERROR
////                            .withProperty(STATUS_PRESSURE, world.getBlockState(adjacentBlock).getValue(STATUS_PRESSURE)) // Comment to unlock STATUS_PRESSURE
////                            );
//                }
//            }
//        }
//        return consoles;
//    }

//    public static enum CanvasType implements IStringSerializable {
//        BLOCK("canvas_block", 0),
//        CORNER("canvas_corner", 1),
//        STAIR("canvas_stair", 2);
//
//        private final String name;
//        private final int meta;
//        private static final BlockCanvas.CanvasType[] META_LOOKUP = new BlockCanvas.CanvasType[values().length];
//        private CanvasType(String name, int meta) {
//            this.name = name;
//            this.meta = meta;
//        }
//
//        public int getMeta() {
//            return meta;
//        }
//        @Override
//        public String getName() {
//            return this.name;
//        }
//        public String getUnlocalizedName() {
//            return this.name;
//        }
//        public static BlockCanvas.CanvasType byMetadata(int meta) {
//            if (meta < 0 || meta >= META_LOOKUP.length) {
//                meta = 0;
//            }
//            return META_LOOKUP[meta];
//        }
//
//        static {
//            BlockCanvas.CanvasType[] var0 = values();
////            int var1 = var0.length;
//
//            for (CanvasType lvt_3_1_ : var0) {
//                META_LOOKUP[lvt_3_1_.getMeta()] = lvt_3_1_;
//            }
//        }
//    }
}
