package com.mcfire.autoplay.registry;

import com.mcfire.autoplay.cap.PowerfulItemInfoValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ModCapabilityRegistryHandler {
    public static Capability<PowerfulItemInfoValue> POWERFUL_ITEM_INFO_VALUE_CAP;
    @SubscribeEvent
    public void registerCaps(RegisterCapabilitiesEvent event){
        event.register(PowerfulItemInfoValue.class);
    }
}
