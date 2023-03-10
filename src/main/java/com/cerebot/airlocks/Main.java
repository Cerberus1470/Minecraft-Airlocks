package com.cerebot.airlocks;

import com.cerebot.airlocks.proxy.CommonProxy;
import com.cerebot.airlocks.tabs.AirlocksTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import com.cerebot.airlocks.util.Reference;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION)
public class Main {

    @Mod.Instance
    public static Main instance;

    public static final CreativeTabs airlocks_tab = new AirlocksTab("airlocks_tab");

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public static void PreInit(FMLPreInitializationEvent event) {}
    @Mod.EventHandler
    public static void init(FMLInitializationEvent event) {}
    @Mod.EventHandler
    public static void PostInit(FMLPostInitializationEvent event) {}
}
