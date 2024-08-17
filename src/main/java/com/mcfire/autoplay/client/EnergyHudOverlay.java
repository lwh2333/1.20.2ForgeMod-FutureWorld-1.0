package com.mcfire.autoplay.client;

import com.mcfire.autoplay.AutoPlay;
import com.mcfire.autoplay.network.ModMessages;
import com.mcfire.autoplay.network.packet.energy.EnergyDataSyncCTSPacket;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class EnergyHudOverlay { // render energy
    private static final ResourceLocation FILLED_ENERGY=new ResourceLocation(AutoPlay.MODID,
            "/textures/gui/hud/energy/filled_energy.png");
    private static final ResourceLocation EMPTY_ENERGY=new ResourceLocation(AutoPlay.MODID,
            "/textures/gui/hud/energy/empty_energy.png");
    public static final IGuiOverlay HUD_ENERGY= (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        ModMessages.sendToServer(new EnergyDataSyncCTSPacket()); // apply for sync
        int x=screenWidth/2;
        int energy=ClientEnergyData.getEnergy();
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(1.0f,1.0f,1.0f,1.0f);
        for(int i=0;i<10;i++){
            if(energy>((9-i)*10)){
                guiGraphics.blit(FILLED_ENERGY,x+82-i*8, screenHeight -gui.rightHeight,0,0,12,12,12,12);
            }else {
                guiGraphics.blit(EMPTY_ENERGY,x+82-i*8, screenHeight -gui.rightHeight,0,0,12,12,12,12);
            }
        }
    };
}