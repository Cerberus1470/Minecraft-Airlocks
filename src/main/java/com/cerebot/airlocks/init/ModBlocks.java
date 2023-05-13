package com.cerebot.airlocks.init;

import com.cerebot.airlocks.blocks.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {

    public static final List<Block> BLOCKS = new ArrayList<>();

    public static final Block BLOCK_AIRLOCK_CONSOLE = new BlockAirlockConsole("block_airlock_console", Material.IRON);

    public static final Block BLOCK_CANVAS = new BlockCanvas("block_canvas", Material.CLOTH);

    public static final Block BLOCK_CANVAS_CORNER = new BlockCanvasCorner("block_canvas_corner", Material.CLOTH);

    public static final Block BLOCK_WALKWAY = new BlockWalkway("block_walkway", Material.IRON);

    public static final Block BLOCK_AIRLOCK_DOOR = new BlockAirlockDoor("block_airlock_door", Material.ANVIL);

    public static final Block BLOCK_TESTING = new BlockTesting("block_testing", Material.ROCK);
}
