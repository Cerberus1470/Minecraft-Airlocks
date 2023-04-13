package com.cerebot.airlocks.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.cerebot.airlocks.blocks.BlockAirlockConsole.PRESSURIZED;

@SuppressWarnings({"NullableProblems", "deprecation"})
public class BlockCanvas extends BlockBase {

    public static final PropertyBool CANVAS_SIGNAL = PropertyBool.create("canvas_signal");

    public BlockCanvas(String name, Material material) {
        super(name, material);

        setSoundType(SoundType.CLOTH);
        setHardness(5.0F);
        setResistance(20.0F);
        setHarvestLevel("pickaxe", 2);
        setDefaultState(this.blockState.getBaseState().withProperty(CANVAS_SIGNAL, false));
        setLightLevel(0.0F);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
//        System.out.println(world.getBlockState(pos.up()).getBlock().getLocalizedName().toLowerCase());
//        if ((world.getBlockState(pos.up())).getBlock().getLocalizedName().toLowerCase().contains("canvas")) {
//            CONNECTED_CANVAS.add(pos.up());
//        } if ((world.getBlockState(pos.down())).getBlock().getLocalizedName().toLowerCase().contains("canvas")) {
//            CONNECTED_CANVAS.add(pos.down());
//        } if ((world.getBlockState(pos.north())).getBlock().getLocalizedName().toLowerCase().contains("canvas")) {
//            CONNECTED_CANVAS.add(pos.north());
//        } if ((world.getBlockState(pos.south())).getBlock().getLocalizedName().toLowerCase().contains("canvas")) {
//            CONNECTED_CANVAS.add(pos.south());
//        } if ((world.getBlockState(pos.east())).getBlock().getLocalizedName().toLowerCase().contains("canvas")) {
//            CONNECTED_CANVAS.add(pos.east());
//        } if ((world.getBlockState(pos.west())).getBlock().getLocalizedName().toLowerCase().contains("canvas")) {
//            CONNECTED_CANVAS.add(pos.west());
//        }
        return this.getDefaultState().withProperty(CANVAS_SIGNAL, false);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(CANVAS_SIGNAL, meta == 1);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CANVAS_SIGNAL);
    }

    @Override
    public void observedNeighborChange(IBlockState observer, World world, BlockPos observer_pos, Block observed, BlockPos observed_pos) {
        if (observer.getBlock() instanceof BlockCanvas && world.getBlockState(observed_pos).getBlock() instanceof BlockCanvas) {
//            System.out.println(observer.getValue(CANVAS_SIGNAL));
//            System.out.println(world.getBlockState(observed_pos).getValue(CANVAS_SIGNAL));
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
            if (world.getBlockState(observed_pos).getValue(PRESSURIZED)) {
                world.setBlockState(observer_pos, world.getBlockState(observer_pos).withProperty(CANVAS_SIGNAL, true));
            } else {
                world.setBlockState(observer_pos, world.getBlockState(observer_pos).withProperty(CANVAS_SIGNAL, false));
            }
        }

//        super.observedNeighborChange(p_observedNeighborChange_1_, p_observedNeighborChange_2_, p_observedNeighborChange_3_, p_observedNeighborChange_4_, p_observedNeighborChange_5_);

    }
    //    public static void updateSignal(World world) {
//        for (int i=0;i<CONNECTED_CANVAS.toArray().length;i++) {
//            world.setBlockState(CONNECTED_CANVAS.get(i), world.getBlockState(CONNECTED_CANVAS.get(i)).withProperty(CANVAS_SIGNAL, true));
//        }
//    }
}
