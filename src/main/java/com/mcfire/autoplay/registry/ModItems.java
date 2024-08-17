package com.mcfire.autoplay.registry;

import com.mcfire.autoplay.AutoPlay;
import com.mcfire.autoplay.items.*;
import com.mcfire.autoplay.items.database.PowerfulLegDataBase;
import com.mcfire.autoplay.items.database.RobotDataBase;
import com.mcfire.autoplay.items.powerfulitem.PowerfulArm;
import com.mcfire.autoplay.items.database.PowerfulArmDataBase;
import com.mcfire.autoplay.items.powerfulitem.PowerfulLeg;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS=DeferredRegister.create(ForgeRegistries.ITEMS, AutoPlay.MODID);
    public static final RegistryObject<Item> POWERFUL_ARM=ITEMS.register("powerful_arm",
            () -> new PowerfulArm(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> POWERFUL_ARM_DATA_BASE=ITEMS.register("powerful_arm_data_base",
            () -> new PowerfulArmDataBase(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> POWERFUL_LEG=ITEMS.register("powerful_leg",
            () -> new PowerfulLeg(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> POWERFUL_LEG_DATA_BASE=ITEMS.register("powerful_leg_data_base",
            () -> new PowerfulLegDataBase(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> EMPTY_KNOWLEDGE_BOTTLE=ITEMS.register("empty_knowledge_bottle",
            () -> new EmptyKnowledgeBottle(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> KNOWLEDGE_BOTTLE=ITEMS.register("knowledge_bottle",
            () -> new KnowledgeBottle(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> USB_DISK=ITEMS.register("usb_disk",
            () -> new USBDisk(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ICHARGE=ITEMS.register("icharge",
            () -> new iCharge(new Item.Properties().stacksTo(5).food(
                    (new FoodProperties.Builder())
                            .alwaysEat()
                            .build())));
    public static final RegistryObject<Item> REFINED_LUBRICATING_OIL_PURIFIER_BLOCK=ITEMS.register("refined_lubricating_oil_purifier_block",
            () -> new BlockItem(ModBlocks.REFINED_LUBRICATING_OIL_PURIFIER_BLOCK.get(),new Item.Properties()));
    public static final RegistryObject<Item> CHAT_GPT_DATA_BASE=ITEMS.register("chat_gpt_data_base",
            () -> new BlockItem(ModBlocks.CHAT_GPT_DATA_BASE.get(),new Item.Properties()));
    public static final RegistryObject<Item> LUBRICATING_OIL=ITEMS.register("lubricating_oil",
            () -> new LubricatingOil(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ROBOT_DATA=ITEMS.register("robot_data",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FULL_ROBOT_DATA=ITEMS.register("full_robot_data",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ROBOT_DATA_BASE=ITEMS.register("robot_data_base",
            () -> new RobotDataBase(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> PROTECTOR=ITEMS.register("protector",
            () -> new BlockItem(ModBlocks.PROTECTOR.get(),new Item.Properties()));
}
