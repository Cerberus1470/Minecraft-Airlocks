package com.cerebot.airlocks.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

@SuppressWarnings({"NullableProblems", "deprecation"})
public class BlockTesting extends BlockBase {

    private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    private static final PropertyBool POWERED = PropertyBool.create("powered");
//    private static int timer = 0;
//    private static World temp_world = null;
    private BlockPos pos = new BlockPos(0.0,0.0,0.0);

    public BlockTesting(String name, Material material) {
        super(name, material);
        setSoundType(SoundType.CLOTH);
        setHardness(5.0F);
        setResistance(20.0F);
        setHarvestLevel("pickaxe", 2);
        setDefaultState(this.blockState.getBaseState()
                .withProperty(FACING, EnumFacing.NORTH)
                .withProperty(POWERED, false));
        setLightLevel(0.0F);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase player, EnumHand hand) {
        this.pos = pos;
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, player, hand);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, POWERED);
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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
//        if (state.getValue(FACING) != EnumFacing.UP) {
//            world.setBlockState(pos, state.withProperty(FACING, EnumFacing.SOUTH), 3);
//            world.markBlockRangeForRenderUpdate(pos, pos);
//            this.playClickSound(player, world, pos);
//            this.notifyNeighbors(world, pos, state.getValue(FACING));
        world.scheduleUpdate(pos, this, 20);
        world.setBlockState(pos, state.withProperty(POWERED, true));
//        System.out.println("This block " + (this.hasTileEntity(state) ? "has" : "does not have") + "a tile entity");
//        world.spawnParticle(EnumParticleTypes.CLOUD, pos.getX() + 0.5F, pos.getY() + 1.01F, pos.getZ() + 1.0F, 0.0D, 0.5D, -0.3D);
//        }
        return true;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        world.setBlockState(pos, state.withProperty(POWERED, false));
//        this.pos = pos;
//        if (timer < 80) {
//            super.onBlockDestroyedByPlayer(world, pos, state);
//            world.scheduleUpdate(pos, this, 1);
////            System.out.println("World: " + world + ", temp world: " + temp_world + ", pos: " + pos + ", state: " + state);
//            world.setBlockState(pos, state.withProperty(FACING, state.getValue(FACING).getOpposite()));
//            timer += 1;
//        } else {
//            timer = 0;
//        }
    }

    private static void spawnParticles(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        float particlePosX = pos.getX();
        float particlePosY = pos.getY();
        float particlePosZ = pos.getZ();

        if (state.getValue(FACING) == EnumFacing.NORTH || state.getValue(FACING) == EnumFacing.SOUTH) {
            world.spawnParticle(EnumParticleTypes.CLOUD, particlePosX + 0.5F, particlePosY + 1.01F, particlePosZ, 0.0D, 0.5D, 0.3D);
            world.spawnParticle(EnumParticleTypes.CLOUD, particlePosX + 0.5F, particlePosY + 1.01F, particlePosZ + 1.0F, 0.0D, 0.5D, -0.3D);
        } else if (state.getValue(FACING) == EnumFacing.EAST || state.getValue(FACING) == EnumFacing.WEST) {
            world.spawnParticle(EnumParticleTypes.CLOUD, particlePosX, particlePosY + 1.01F, particlePosZ + 0.5F, 0.3D, 0.5D, 0.0D);
            world.spawnParticle(EnumParticleTypes.CLOUD, particlePosX + 1.0F, particlePosY + 1.01F, particlePosZ + 0.5F, -0.3D, 0.5D, 0.0D);
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TestingTileEntity(this, world, state);
    }

    public static class TestingTileEntity extends TileEntity implements ITickable {
//        private final World world;
//        private final IBlockState state;
//        public final BlockTesting block;
        public TestingTileEntity() {
//            this.world = null;
//            this.state = null;
//            this.block = null;
        }
        public TestingTileEntity(BlockTesting blockInit, World worldInit, IBlockState stateInit) {
//            super();
//            this.block = blockInit;
//            this.state = stateInit;
        }
//        @Override
//        public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
//            return super.writeToNBT(nbt);
//        }
//        @Override
//        public void readFromNBT(NBTTagCompound nbt) {
//            super.readFromNBT(nbt);
//        }

        @Override
        public void update() {
//            assert this.state != null;
//            System.out.println("I am a tile entity linked to " + this.block + " and I am ticking!");
            IBlockState block = this.getWorld().getBlockState(this.getPos());
            if (this.getBlockType() instanceof BlockTesting && block.getValue(POWERED)) {
                BlockTesting.spawnParticles(this.getWorld(), this.getPos());
            }
        }
    }
}
