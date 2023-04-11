package com.cerebot.airlocks.items;

import com.cerebot.airlocks.Airlocks;
import com.cerebot.airlocks.init.ModItems;
import com.cerebot.airlocks.util.IHasModel;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {


    public ItemBase(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(Airlocks.airlocks_tab);

        ModItems.ITEMS.add(this);
    }

    @Override
    public void registerModels() {
        Airlocks.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
