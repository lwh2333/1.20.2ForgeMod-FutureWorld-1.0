package com.mcfire.autoplay.registry;

import com.mcfire.autoplay.AutoPlay;
import com.mcfire.autoplay.menu.RefinedLubricatingOilPurifierMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES=DeferredRegister
            .create(ForgeRegistries.MENU_TYPES, AutoPlay.MODID);
    public static final RegistryObject<MenuType<RefinedLubricatingOilPurifierMenu>> REFINED_LUBRICATING_OIL_PURIFIER_MENU=
            registerMenuType(RefinedLubricatingOilPurifierMenu::new, "refined_lubricating_oil_purifier_menu");
    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory factory, String name){
        return MENU_TYPES.register(name, ()-> IForgeMenuType.create(factory));
    }
}
