package cerebot.airlocks.objects.items;

import cerebot.airlocks.Airlocks;
import cerebot.airlocks.init.ItemInit;
import cerebot.airlocks.util.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {

    public ItemBase(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabs.REDSTONE);

        ItemInit.ITEMS.add(this);
    }

    @Override
    public void registerModels() {
        Airlocks.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
