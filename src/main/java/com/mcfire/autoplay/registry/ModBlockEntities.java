package com.mcfire.autoplay.registry;

import com.mcfire.autoplay.AutoPlay;
import com.mcfire.autoplay.entity.blockentity.RefinedLubricatingOilPurifierEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES=DeferredRegister
            .create(ForgeRegistries.BLOCK_ENTITY_TYPES, AutoPlay.MODID);
    public static final RegistryObject<BlockEntityType<RefinedLubricatingOilPurifierEntity>>
            REFINED_LUBRICATING_OIL_PURIFIER=BLOCK_ENTITIES.register("refined_lubricating_oil_purifier",
            ()->BlockEntityType.Builder.of(RefinedLubricatingOilPurifierEntity::new, ModBlocks.REFINED_LUBRICATING_OIL_PURIFIER_BLOCK.get()).build(null));
}
