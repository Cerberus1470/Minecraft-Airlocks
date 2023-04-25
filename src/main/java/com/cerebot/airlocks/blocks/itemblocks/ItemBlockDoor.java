package com.cerebot.airlocks.blocks.itemblocks;

import com.cerebot.airlocks.blocks.BlockDoorBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings("NullableProblems")
public class ItemBlockDoor extends ItemBlock {
    public ItemBlockDoor(Block block) {
        super(block);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
                                      float hitX, float hitY, float hitZ) {
        if (facing != EnumFacing.UP) return EnumActionResult.FAIL;
        else {
            IBlockState bottomDoorState = world.getBlockState(pos);
            Block bottomDoorBlock = bottomDoorState.getBlock();
            if (!block.isReplaceable(world, pos)) pos = pos.offset(facing);

            ItemStack stack = player.getHeldItem(hand);
            if (player.canPlayerEdit(pos, facing, stack) && this.block.canPlaceBlockAt(world, pos)) {
                EnumFacing playerFacing = EnumFacing.fromAngle(player.rotationYaw);
                int x = playerFacing.getFrontOffsetX();
                int z = playerFacing.getFrontOffsetZ();
                boolean flag = x < 0 && hitZ < 0.5F || x > 0 && hitZ > 0.5F || z < 0 && hitX < 0.5F || z > 0 && hitX > 0.5F;
                placeDoor(world, pos, playerFacing, this.block, flag);

                SoundType sound = block.getSoundType(bottomDoorState, world, pos, player);
                world.playSound(player, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, 0.5F, 0.5F);

                stack.shrink(1);
                return EnumActionResult.SUCCESS;
            }
            else return EnumActionResult.FAIL;
        }
    }

    private static void placeDoor(World world, BlockPos bottomDoorPos, EnumFacing playerFacing, Block door, boolean isRightHinge) {
        BlockPos posYClockwise = bottomDoorPos.offset(playerFacing.rotateY());
        BlockPos posYAntiClockwise = bottomDoorPos.offset(playerFacing.rotateYCCW());

        int i = (world.getBlockState(posYAntiClockwise).isNormalCube() ? 1 : 0) +
                (world.getBlockState(posYAntiClockwise.up()).isNormalCube() ? 1 : 0);
        int j = (world.getBlockState(posYClockwise).isNormalCube() ? 1 : 0) +
                (world.getBlockState(posYClockwise.up()).isNormalCube() ? 1 : 0);
        boolean flag = world.getBlockState(posYAntiClockwise).getBlock() == door || world.getBlockState(posYAntiClockwise.up()).getBlock() == door;
        boolean flag1 = world.getBlockState(posYClockwise).getBlock() == door || world.getBlockState(posYClockwise.up()).getBlock() == door;

        if ((!flag || flag1) && j <= i) {
            if (flag1 && !flag || j < i) isRightHinge = false;
            else isRightHinge = true;
        }
        BlockPos topDoorPos = bottomDoorPos.up();
        boolean powered = world.isBlockPowered(bottomDoorPos) || world.isBlockPowered(topDoorPos);
        IBlockState doorState = door.getDefaultState()
                .withProperty(BlockDoor.FACING, playerFacing)
                .withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT);
//                .withProperty(BlockDoor.POWERED, powered).withProperty(BlockDoor.OPEN, powered);
        world.setBlockState(bottomDoorPos, doorState.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER));
        world.setBlockState(topDoorPos, doorState.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER));

        world.notifyNeighborsOfStateChange(bottomDoorPos, door, false);
        world.notifyNeighborsOfStateChange(topDoorPos, door, false);
    }
}
