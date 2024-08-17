package com.mcfire.autoplay.registry;

import com.mcfire.autoplay.AutoPlay;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AutoPlay.MODID);
    public static final RegistryObject<CreativeModeTab> POWERFUL_TAB =CREATIVE_MODE_TABS.register("powerful_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("item_group."+AutoPlay.MODID+".powerful_tab"))
                    .icon(() -> new ItemStack(Items.IRON_SWORD))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.POWERFUL_ARM.get());
                        output.accept(ModItems.POWERFUL_ARM_DATA_BASE.get());
                        output.accept(ModItems.POWERFUL_LEG.get());
                        output.accept(ModItems.POWERFUL_LEG_DATA_BASE.get());
                        output.accept(ModItems.EMPTY_KNOWLEDGE_BOTTLE.get());
                        output.accept(ModItems.KNOWLEDGE_BOTTLE.get());
                        output.accept(ModItems.USB_DISK.get());
                        output.accept(ModItems.ICHARGE.get());
                        output.accept(ModItems.LUBRICATING_OIL.get());
                        output.accept(ModItems.ROBOT_DATA.get());
                        output.accept(ModItems.FULL_ROBOT_DATA.get());
                        output.accept(ModItems.ROBOT_DATA_BASE.get());
                        output.accept(ModBlocks.REFINED_LUBRICATING_OIL_PURIFIER_BLOCK.get());
                        output.accept(ModBlocks.CHAT_GPT_DATA_BASE.get());
                        output.accept(ModBlocks.PROTECTOR.get());
                    })
                    .build());
}
