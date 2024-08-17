package com.mcfire.autoplay.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.ArrayUtils;

@Mod.EventBusSubscriber
public class ModKeys {
    public static final KeyMapping FIRST_SKILL = register(new KeyMapping("key.first_skill",InputConstants.Type.KEYSYM,265,KeyMapping.CATEGORY_GAMEPLAY));
    public static final KeyMapping SECOND_SKILL = register(new KeyMapping("key.second_skill",InputConstants.Type.KEYSYM,263,KeyMapping.CATEGORY_GAMEPLAY));
    public static final KeyMapping THIRD_SKILL = register(new KeyMapping("key.third_skill",InputConstants.Type.KEYSYM,264,KeyMapping.CATEGORY_GAMEPLAY));
    public static final KeyMapping FOURTH_SKILL = register(new KeyMapping("key.fourth_skill",InputConstants.Type.KEYSYM,262,KeyMapping.CATEGORY_GAMEPLAY));

    private static KeyMapping register(KeyMapping keyMapping){
        Minecraft.getInstance().options.keyMappings=ArrayUtils.add(Minecraft.getInstance().options.keyMappings,keyMapping);
        KeyConflictContext inGame = KeyConflictContext.IN_GAME;
        keyMapping.setKeyConflictContext(inGame);
        return keyMapping;
    }
}
