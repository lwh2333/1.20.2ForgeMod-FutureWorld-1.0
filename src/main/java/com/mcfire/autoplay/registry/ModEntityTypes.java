package com.mcfire.autoplay.registry;

import com.mcfire.autoplay.AutoPlay;
import com.mcfire.autoplay.entity.monster.RobotEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES=DeferredRegister
            .create(ForgeRegistries.ENTITY_TYPES, AutoPlay.MODID);
    public static final RegistryObject<EntityType<RobotEntity>> ROBOT=ENTITY_TYPES.register("robot",
            () -> EntityType.Builder.of(RobotEntity::new, MobCategory.MISC).sized(3F,12F).build(new ResourceLocation(AutoPlay.MODID,"robot").toString()));
}
