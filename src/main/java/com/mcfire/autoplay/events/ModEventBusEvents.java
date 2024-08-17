package com.mcfire.autoplay.events;

import com.mcfire.autoplay.AutoPlay;
import com.mcfire.autoplay.entity.monster.RobotEntity;
import com.mcfire.autoplay.registry.ModEntityTypes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AutoPlay.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerEntityAttributeEvent(EntityAttributeCreationEvent event){
        event.put(ModEntityTypes.ROBOT.get(), RobotEntity.setAttributes());
    }
}
