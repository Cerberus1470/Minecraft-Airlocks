package com.cerebot.airlocks.util.handlers;

import com.cerebot.airlocks.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SoundsHandler {
    public static SoundEvent BLOCK_CONSOLE_KEYPRESS, BLOCK_CONSOLE_PRESSURIZING, BLOCK_CONSOLE_COMPLETE, BLOCK_DOOR_OPEN, BLOCK_DOOR_CLOSE;

    public static void registerSounds() {
        BLOCK_CONSOLE_KEYPRESS = registerSound("block.console.keypress");
        BLOCK_CONSOLE_PRESSURIZING = registerSound("block.console.pressurizing");
        BLOCK_CONSOLE_COMPLETE = registerSound("block.console.complete");
        BLOCK_DOOR_OPEN = registerSound("block.door.open");
        BLOCK_DOOR_CLOSE = registerSound("block.door.close");
    }

    private static SoundEvent registerSound(String name) {
        ResourceLocation resourceLocation = new ResourceLocation(Reference.MOD_ID, name);
        SoundEvent soundEvent = new SoundEvent(resourceLocation);
        soundEvent.setRegistryName(name);
        ForgeRegistries.SOUND_EVENTS.register(soundEvent);
        return soundEvent;
    }
}
