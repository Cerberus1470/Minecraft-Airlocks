package com.cerebot.airlocks.blocks;

import com.cerebot.airlocks.util.handlers.SoundsHandler;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Random;

//import static net.minecraft.block.BlockDoor.OPEN;

@SuppressWarnings({"NullableProblems", "deprecation"})
public class BlockAirlockConsole extends BlockBase {

    public static final AxisAlignedBB[] AIRLOCK_CONSOLE_AABB = {
            new AxisAlignedBB(0.1875D, 0.0625D, 0D, 0.8125D, 0.9375D, 0.0625D), //South
            new AxisAlignedBB(0.9375D, 0.0625D, 0.1875D, 1D, 0.9375D, 0.8125D), //West
            new AxisAlignedBB(0.1875D, 0.0625D, 0.9375D, 0.8125D, 0.9375D, 1D), //North
            new AxisAlignedBB(0D, 0.0625D, 0.1875D, 0.0625D, 0.9375D, 0.8125D) //East
    };


    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
//    public static final PropertyBool PRESSURIZED = PropertyBool.create("pressurized");
    public static final PropertyInteger ERROR = PropertyInteger.create("error", 0, 2);
    public static final PropertyInteger STATUS_PRESSURE = PropertyInteger.create("status_pressure", 0,3);

    public BlockAirlockConsole(String name, Material material) {
        super(name, material);

        setSoundType(SoundType.METAL);
        setHardness(5.0F);
        setResistance(20.0F);
        setHarvestLevel("pickaxe", 2);
        setLightLevel(0.8F);
        setDefaultState(this.blockState.getBaseState()
                .withProperty(FACING, EnumFacing.NORTH)
                .withProperty(STATUS_PRESSURE, 0)
                .withProperty(ERROR, 0)
        );
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, facing.getHorizontalIndex() == -1 ? placer.getHorizontalFacing().getOpposite() : facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(STATUS_PRESSURE) * 4) +
                (((state.getValue(FACING) == EnumFacing.DOWN || state.getValue(FACING) == EnumFacing.UP) ? EnumFacing.NORTH : state.getValue(FACING)).getHorizontalIndex());
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        int stat_press = meta / 4;
        EnumFacing facing = EnumFacing.getHorizontal(meta % 4);
        return getDefaultState()
                .withProperty(FACING, facing)
                .withProperty(STATUS_PRESSURE, stat_press)
                .withProperty(ERROR, 0);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, STATUS_PRESSURE, ERROR);
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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        state = state.withProperty(ERROR, 0);
        BlockPos connected_canvas = getConnectedCanvas(world, state, pos);
//        if (!(world.getBlockState(connected_canvas).getBlock() instanceof BlockCanvas)
////                && !world.getBlockState(connected_canvas).getValue(CANVAS_SAFE)
//        ) {
//            world.setBlockState(pos, state.withProperty(ERROR, 1));
        if (state.getValue(STATUS_PRESSURE) % 2 == 1) {
            return true;
        }
        if (connected_canvas == null) {
            state = state.withProperty(STATUS_PRESSURE, state.getValue(STATUS_PRESSURE) == 0 ? 1 : 3);
            this.playClickSound(playerIn, world, pos);
            world.scheduleUpdate(pos, this, this.tickRate(world));
        } else {
            if (!getConnectedDevices(world, connected_canvas, new ArrayList<>(), new ArrayList<>(), "BlockAirlockDoor").isEmpty()) {
                state = state.withProperty(ERROR, 1);
            } else {
                state = state.withProperty(STATUS_PRESSURE, state.getValue(STATUS_PRESSURE) == 0 ? 1 : 3);
                this.playClickSound(playerIn, world, pos);
                for (BlockPos connected_console_pos : getConnectedDevices(world, connected_canvas, new ArrayList<>(), new ArrayList<>(), "BlockAirlockConsole")) {
                    world.setBlockState(connected_console_pos, state.withProperty(FACING, world.getBlockState(connected_console_pos).getValue(FACING)));
                }
                for (BlockPos connected_walkway_pos : getConnectedDevices(world, connected_canvas, new ArrayList<>(), new ArrayList<>(), "BlockWalkway")) {
                    world.setBlockState(connected_walkway_pos, world.getBlockState(connected_walkway_pos).withProperty(BlockWalkway.POWERED, true));
                }
                world.scheduleUpdate(pos, this, this.tickRate(world));
//            if (world.getBlockState(connected_canvas).getBlock() instanceof BlockCanvas) {
//                world.setBlockState(connected_canvas, world.getBlockState(connected_canvas).withProperty(CANVAS_SAFE, true));
//
                System.out.println("Actually I need to check my schedule!");
            }
        }
        world.setBlockState(pos, state);
        return true;
    }

    protected void playClickSound(@Nullable EntityPlayer player, World worldIn, BlockPos pos) {
        worldIn.playSound(player, pos, SoundsHandler.BLOCK_CONSOLE_KEYPRESS, SoundCategory.BLOCKS, 10.0F, 1.0F);
        worldIn.playSound(player, pos, SoundsHandler.BLOCK_CONSOLE_PRESSURIZING, SoundCategory.BLOCKS, 15.0F, 1.0F);
    }

//    @Override
//    protected void playReleaseSound(World worldIn, BlockPos pos) {
//        worldIn.playSound(null, pos, SoundsHandler.BLOCK_CONSOLE_COMPLETE, SoundCategory.BLOCKS, 1.0F, 1.0F);
//    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (state.getValue(ERROR) == 0) {
            state = state.withProperty(STATUS_PRESSURE, state.getValue(STATUS_PRESSURE) == 1 ? 2 : 0);
            world.playSound(null, pos, SoundsHandler.BLOCK_CONSOLE_COMPLETE, SoundCategory.BLOCKS, 1.0F, 1.0F);
        } else {
            state = state.withProperty(STATUS_PRESSURE, 0);
        }
        BlockPos connected_canvas = getConnectedCanvas(world, state, pos);
        if (connected_canvas != null) {
            for (BlockPos receiver_pos : getConnectedDevices(world, connected_canvas, new ArrayList<>(), new ArrayList<>(), "BlockAirlockConsole")) {
                world.setBlockState(receiver_pos, state.withProperty(FACING, world.getBlockState(receiver_pos).getValue(FACING)));
            }
            for (BlockPos connected_walkway_pos : getConnectedDevices(world, connected_canvas, new ArrayList<>(), new ArrayList<>(), "BlockWalkway")) {
                world.setBlockState(connected_walkway_pos, world.getBlockState(connected_walkway_pos).withProperty(BlockWalkway.POWERED, false));
            }
        }
        world.setBlockState(pos, state);
    }

//    @Override
//    public void observedNeighborChange(IBlockState observer_state, World world, BlockPos observer_pos, Block observed, BlockPos observed_pos) {
//        IBlockState observed_state = world.getBlockState(observed_pos);
//        observed = observed_state.getBlock();
//        if (observed instanceof BlockCanvas) {
//            observer_state = observer_state.withProperty(ERROR, 0);
//            System.out.println("The observed_pos is " + observed_pos + " and the adjacent block is " + this.getConnectedCanvas(observer, observer_pos));
//            if (observed_pos.equals(this.getConnectedCanvas(observer_state, observer_pos))) {
//                observer_state = observer_state
//                        .withProperty(STATUS_PRESSURE, observed_state.getValue(CANVAS_SIGNAL));
//                if (observed_state.getValue(CANVAS_SIGNAL)) {
//                    System.out.println("The observed canvas is transmitting!");
//                    super.onBlockActivated(world, observer_pos, observer, playerIn, hand, facing, hitX, hitY, hitZ);
//                    super.onBlockActivated(world, observer_pos, observer_state, null, null, null, 0.0F, 0.0F, 0.0F);
//                } else {
//                System.out.println("The observed canvas has stopped transmitting!");
//                    IBlockState final_state = world.getBlockState(observer_pos).withProperty(POWERED, false);
//                    final_state = final_state.withProperty(PRESSURIZED, !final_state.getValue(PRESSURIZED));
//                    if (final_state.getValue(PRESSURIZED)) {
//                        final_state = final_state.withProperty(PRESSURIZED, false);
//                    } else {
//                        final_state = final_state.withProperty(PRESSURIZED, true);
//                    }
//                    world.setBlockState(observer_pos, final_state);
//            } else if (!observed_state.getValue(CANVAS_SAFE)){
//                observer_state = observer_state.withProperty(ERROR, 1);
//            }
//        }
//        world.setBlockState(observer_pos, observer_state);
//        }

    public static BlockPos getConnectedCanvas(World world, IBlockState state, BlockPos observer_pos) {
        BlockPos[] adjacent_blocks = {observer_pos.south(), observer_pos.west(), observer_pos.north(), observer_pos.east()};
        if (state.getValue(FACING).getHorizontalIndex() == -1) {
            return null;
        } else {
            BlockPos connectedCanvas = adjacent_blocks[state.getValue(FACING).getOpposite().getHorizontalIndex()];
            if (world.getBlockState(connectedCanvas).getBlock() instanceof BlockCanvas) {
                return connectedCanvas;
            } else {
                return null;
            }
        }
    }

//    public ArrayList<BlockPos> getConnectedConsoles(World world, BlockPos source_pos) {
//        System.out.println("I am searching for connected Consoles!");
//        BlockPos canvas_pos = this.getConnectedCanvas(world.getBlockState(source_pos), source_pos);
//        ArrayList<BlockPos> connected_consoles = new ArrayList<>();
//        boolean searching = true;
//        while (searching) {
//            BlockPos[] adjacent_blocks = {canvas_pos.north(), canvas_pos.east(), canvas_pos.up(),
//                    canvas_pos.south(), canvas_pos.west(), canvas_pos.down()};
//            for (BlockPos adjacent_block : adjacent_blocks) {
//                if (world.getBlockState(adjacent_block).getBlock() instanceof BlockCanvas) {
//                    canvas_pos = adjacent_block;
//                    break;
//                }
//            }
//        }
//        return connected_consoles;
//    }

    public static ArrayList<BlockPos> getConnectedDevices(World world, BlockPos transmitter, ArrayList<BlockPos> exclude, ArrayList<BlockPos> devices, String device) {
        BlockPos[] adjacent_blocks = {transmitter.north(), transmitter.east(), transmitter.up(),
                transmitter.south(), transmitter.west(), transmitter.down()};
//        Class device_class = device.equals("console") ? BlockAirlockConsole.class : device.equals("door") ? BlockAirlockDoor.class : device.equals("walkway") ? BlockWalkway.class : null;
        for (BlockPos adjacentBlock : adjacent_blocks) {
            if (adjacentBlock != null && !exclude.contains(adjacentBlock)) {
                if (world.getBlockState(adjacentBlock).getBlock() instanceof BlockCanvas ||
                        world.getBlockState(adjacentBlock).getBlock() instanceof BlockCanvasCorner) {
//                    System.out.println("This is a canvas block being updated!");
//                    ArrayList<BlockPos> new_exclude = new ArrayList<>(exclude.size());
//                    new_exclude.addAll(exclude);
//                    new_exclude.add(transmitter);
                    exclude.add(transmitter);
                    getConnectedDevices(world, adjacentBlock, exclude, devices, device);
                }
                if (device.equals("BlockAirlockConsole") && world.getBlockState(adjacentBlock).getBlock() instanceof BlockAirlockConsole) {
//                    System.out.println("This is an airlock console block being updated!");
                    if (!devices.contains(adjacentBlock) && transmitter.equals(getConnectedCanvas(world, world.getBlockState(adjacentBlock), adjacentBlock))) {
                        devices.add(adjacentBlock);
                    }
//                    world.setBlockState(adjacentBlock, data
//                            .withProperty(FACING, world.getBlockState(adjacentBlock).getValue(FACING)) // Comment to unlock FACING
//                            .withProperty(ERROR, world.getBlockState(adjacentBlock).getValue(ERROR)) // Comment to unlock ERROR
//                            .withProperty(STATUS_PRESSURE, world.getBlockState(adjacentBlock).getValue(STATUS_PRESSURE)) // Comment to unlock STATUS_PRESSURE
//                            );
                } else if (device.equals("BlockAirlockDoor") && world.getBlockState(adjacentBlock).getBlock() instanceof BlockAirlockDoor) {
                  System.out.println("There is an airlock door being checked!");
                    if (world.getBlockState(adjacentBlock).getValue(BlockDoor.OPEN)) {
                        devices.add(adjacentBlock);
                        return devices;
                    }
                } else if (device.equals("BlockWalkway") && world.getBlockState(adjacentBlock).getBlock() instanceof BlockWalkway) {
//                    System.out.println("This is a walkway block being updated!");
                    if (!devices.contains(adjacentBlock)) {
                        devices.add(adjacentBlock);
                    }
                }
            }
        }
        return devices;
    }

//    public static boolean isDoorOpen(World world, BlockPos transmitter, ArrayList<BlockPos> exclude) {
//        BlockPos[] adjacent_blocks = {transmitter.north(), transmitter.east(), transmitter.up(),
//                transmitter.south(), transmitter.west(), transmitter.down()};
//        for (BlockPos adjacentBlock : adjacent_blocks) {
//            if (adjacentBlock != null && !exclude.contains(adjacentBlock)) {
//                if (world.getBlockState(adjacentBlock).getBlock() instanceof BlockCanvas ||
//                        world.getBlockState(adjacentBlock).getBlock() instanceof BlockCanvasCorner) {
//                    System.out.println("This is a canvas block being updated!");
//                    exclude.add(transmitter);
//                    if (isDoorOpen(world, adjacentBlock, exclude)) {
//                        return true;
//                    }
//                }
//                if (world.getBlockState(adjacentBlock).getBlock() instanceof BlockAirlockDoor) {
//                    System.out.println("There is an airlock door being checked!");
//                    if (world.getBlockState(adjacentBlock).getValue(BlockDoor.OPEN)) {
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }
}
