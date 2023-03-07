package com.cerebot.airlocks.init;

import com.cerebot.airlocks.blocks.AirlockConsoleBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {

    public static final List<Block> BLOCKS = new ArrayList<>();

    public static final Block AIRLOCK_CONSOLE_BLOCK = new AirlockConsoleBlock("airlock_console_block", Material.IRON);
}
