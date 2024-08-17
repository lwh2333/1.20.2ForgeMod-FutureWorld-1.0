package com.mcfire.autoplay.events;

import com.mcfire.autoplay.AutoPlay;
import com.mcfire.autoplay.client.EnergyHudOverlay;
import com.mcfire.autoplay.entity.renderer.RobotRenderer;
import com.mcfire.autoplay.registry.ModEntityTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= AutoPlay.MODID, value=Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {
    @SubscribeEvent
    public static void registerGuiOverlay(RegisterGuiOverlaysEvent event){
        event.registerAboveAll("energy", EnergyHudOverlay.HUD_ENERGY); // register hud of energy
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(ModEntityTypes.ROBOT.get(), RobotRenderer::new); // register robot renderer
    }
}
