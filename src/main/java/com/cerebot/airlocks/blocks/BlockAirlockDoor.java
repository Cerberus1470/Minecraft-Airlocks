package com.cerebot.airlocks.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.cerebot.airlocks.blocks.BlockCanvas.CANVAS_SIGNAL;

@SuppressWarnings({"NullableProblems", "DataFlowIssue"})
public class BlockAirlockDoor extends BlockDoorBase {

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
    public void observedNeighborChange(IBlockState observer, World world, BlockPos observer_pos, Block observed, BlockPos observed_pos) {
        if (observed instanceof BlockCanvas && world.getBlockState(observed_pos).getValue(CANVAS_SIGNAL)) {
            if (observer.getValue(OPEN)) {
                super.onBlockActivated(world, observer_pos, observer, null, null, null, 0.0F, 0.0F, 0.0F);
            }
            System.out.println("There is a canvas block attempting to pressurize next to me! Coordinates are " + observed_pos);
        }
    }
}
