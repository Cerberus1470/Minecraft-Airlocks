package com.cerebot.airlocks.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

//import static com.cerebot.airlocks.blocks.BlockCanvas.CANVAS_SAFE;
//import static com.cerebot.airlocks.blocks.BlockCanvas.CANVAS_SIGNAL;

public class BlockAirlockDoor extends BlockDoorBase {

//    private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
//    private static final PropertyBool OPEN = PropertyBool.create("open");

    public BlockAirlockDoor(String name, Material material) {
        super(name, material);
        setSoundType(SoundType.METAL);
        setHardness(5.0F);
        setResistance(20.0F);
        setHarvestLevel("pickaxe", 2);
        setLightLevel(0.0F);
        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(OPEN, false));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
//        if (state.getValue(HALF).equals(EnumDoorHalf.UPPER)) {
//            state = worldIn.getBlockState(pos.down());
//            pos = pos.down();
//        }
//        BlockPos[] positions = {pos.north(), pos.east(), pos.south(), pos.west()};
//        boolean flag = false;
//        for (BlockPos position : positions) {
//            if (worldIn.getBlockState(position).getBlock() instanceof BlockCanvas) {
//                flag |= worldIn.getBlockState(position).getValue(CANVAS_SIGNAL) % 2 == 1;
//                flag |= ((!worldIn.getBlockState(position).getValue(CANVAS_SAFE)) && (!state.getValue(OPEN)));
//            }
//        }
//        if (false) {
//            return true;
//        } else {
            super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
//        }
        if (world.getBlockState(pos).getValue(HALF) == EnumDoorHalf.UPPER) {
            pos = pos.down();
        }
        state = world.getBlockState(pos);
        BlockPos connected_canvas = getConnectedCanvas(world, state, pos);
        if (connected_canvas != null) {
            for (BlockPos connected_console_pos : BlockAirlockConsole.getConnectedDevices(world, connected_canvas, new ArrayList<>(), new ArrayList<>(), "BlockAirlockConsole")) {
                IBlockState console_state = world.getBlockState(connected_console_pos);
                world.setBlockState(connected_console_pos, console_state
                        .withProperty(BlockAirlockConsole.ERROR, BlockAirlockConsole.getConnectedDevices(world, connected_canvas, new ArrayList<>(), new ArrayList<>(), "BlockAirlockDoor").isEmpty() ? 0 : console_state.getValue(BlockAirlockConsole.ERROR) == 1 ? 2 : 1)
                        .withProperty(BlockAirlockConsole.STATUS_PRESSURE,
                                console_state.getValue(BlockAirlockConsole.ERROR) == 2 ? 0 :
                                console_state.getValue(BlockAirlockConsole.STATUS_PRESSURE) % 2 == 1 ? 0 :
                                console_state.getValue(BlockAirlockConsole.STATUS_PRESSURE)
                        ));
            }
            for (BlockPos connected_walkway_pos : BlockAirlockConsole.getConnectedDevices(world, connected_canvas, new ArrayList<>(), new ArrayList<>(), "BlockWalkway")) {
                world.setBlockState(connected_walkway_pos, world.getBlockState(connected_walkway_pos).withProperty(BlockWalkway.POWERED, false));
            }
//            MinecraftServer.getServer().getCommandManager().executeCommand(player, String.format("stopsound %s block airlocks:block.console.pressurizing", player.getName()));
//            Minecraft.getMinecraft().player.sendChatMessage(String.format("/stopsound %s block airlocks:block.console.pressurizing", player.getName()));
        }
        return true;
    }
//
//    @Override
//    public void observedNeighborChange(IBlockState observer, World world, BlockPos observer_pos, Block observed, BlockPos observed_pos) {
//        IBlockState observed_state = world.getBlockState(observed_pos);
//        observed = observed_state.getBlock();
//        if (observed instanceof BlockCanvas && observed_state.getValue(CANVAS_SIGNAL) % 2 == 1) {
//            if (observer.getValue(OPEN)) {
//                super.onBlockActivated(world, observer_pos, observer, null, null, null, 0.0F, 0.0F, 0.0F);
//            }
//            System.out.println("There is a canvas block attempting to pressurize next to me! Coordinates are " + observed_pos);
//        }
//    }

    public BlockPos getConnectedCanvas(World world, IBlockState state, BlockPos door_pos) {
        switch (state.getValue(FACING)) {
            case NORTH: {
                if (world.getBlockState(door_pos.east()).getBlock() instanceof BlockCanvas) {
                    return door_pos.east();
                } else if (world.getBlockState(door_pos.west()).getBlock() instanceof BlockCanvas) {
                    return door_pos.west();
                }
            }
            case EAST: {
                if (world.getBlockState(door_pos.south()).getBlock() instanceof BlockCanvas) {
                    return door_pos.south();
                } else if (world.getBlockState(door_pos.north()).getBlock() instanceof BlockCanvas) {
                    return door_pos.north();
                }
            }
            case SOUTH: {
                if (world.getBlockState(door_pos.west()).getBlock() instanceof BlockCanvas) {
                    return door_pos.west();
                } else if (world.getBlockState(door_pos.east()).getBlock() instanceof BlockCanvas) {
                    return door_pos.east();
                }
            }
            case WEST: {
                if (world.getBlockState(door_pos.north()).getBlock() instanceof BlockCanvas) {
                    return door_pos.north();
                } else if (world.getBlockState(door_pos.south()).getBlock() instanceof BlockCanvas) {
                    return door_pos.south();
                }
            }
        }
        return null;
    }
}
