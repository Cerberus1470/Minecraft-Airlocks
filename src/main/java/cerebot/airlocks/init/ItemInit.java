package cerebot.airlocks.init;

import java.util.ArrayList;
import java.util.List;

import cerebot.airlocks.objects.items.ItemBase;
import net.minecraft.item.Item;

public class ItemInit
{
    public static final List<Item> ITEMS = new ArrayList<Item>();

    public static final Item AIRLOCK_CONSOLE = new ItemBase("airlock_console");
}
