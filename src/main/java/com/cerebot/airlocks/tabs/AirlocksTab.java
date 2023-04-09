package com.cerebot.airlocks.tabs;

import com.cerebot.airlocks.init.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

@SuppressWarnings("NullableProblems")
public class AirlocksTab extends CreativeTabs {

    public AirlocksTab(String label) {
        super(label);

        this.setBackgroundImageName("airlocks.png");
    }
    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ModBlocks.BLOCK_AIRLOCK_CONSOLE);
    }
}
