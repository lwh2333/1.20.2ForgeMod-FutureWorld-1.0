package com.mcfire.autoplay.menu;

import com.mcfire.autoplay.AutoPlay;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class RefinedLubricatingOilPurifierScreen extends AbstractContainerScreen<RefinedLubricatingOilPurifierMenu> {
    private static final ResourceLocation TEXTURE=new ResourceLocation(AutoPlay.MODID,"textures/gui/container/refined_lubricating_oil_purifier_gui.png");

    public RefinedLubricatingOilPurifierScreen(RefinedLubricatingOilPurifierMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }

    @Override
    protected void renderBg(GuiGraphics p_283065_, float p_97788_, int p_97789_, int p_97790_) {
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(1.0f,1.0f,1.0f,1.0f);
        p_283065_.blit(TEXTURE, (width-imageWidth)/2, (height-imageHeight)/2, 0, 0, imageWidth, imageHeight);
        renderProgressArrow(p_283065_, (width-imageWidth)/2, (height-imageHeight)/2);
        renderWLM(p_283065_, (width-imageWidth)/2, (height-imageHeight)/2);
    }

    private void renderProgressArrow(GuiGraphics g, int x, int y){
        if(menu.isCrafting()){
            g.blit(TEXTURE, x+166, y+12, 176, 0, 8, menu.getScaledProgress());
        }
    }

    private void renderWLM(GuiGraphics g, int x, int y){ // WLM means water, lava and milk
        g.blit(TEXTURE, x+55, y+15, 184, -(61-menu.getScaledEnergy(0)), 16, 61);
        g.blit(TEXTURE, x+86, y+15, 200, -(61-menu.getScaledEnergy(1)), 16, 61);
        g.blit(TEXTURE, x+117, y+15, 216, -(61-menu.getScaledEnergy(2)), 16, 61);
    }

    @Override
    public void render(GuiGraphics p_283479_, int p_283661_, int p_281248_, float p_281886_) {
        renderBackground(p_283479_, p_283661_, p_281248_, p_283661_);
        super.render(p_283479_, p_283661_, p_281248_, p_281886_);
        renderTooltip(p_283479_, p_283661_, p_281248_);
    }

    @Override
    protected void init() {
        super.init();
    }
}