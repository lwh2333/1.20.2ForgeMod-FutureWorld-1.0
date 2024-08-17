package com.mcfire.autoplay.network.packet.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class UpdateFacingSTCPacket {
    private final float xRot;
    private final float yRot;
    public UpdateFacingSTCPacket(float xRot, float yRot) {
        this.xRot=xRot;
        this.yRot=yRot;
    }

    public UpdateFacingSTCPacket(FriendlyByteBuf buf){
        xRot=buf.readFloat();
        yRot=buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeFloat(xRot);
        buf.writeFloat(yRot);
    }

    public void handle(CustomPayloadEvent.Context context){
        context.enqueueWork(()->{
            LocalPlayer localPlayer= Minecraft.getInstance().player;
            if(localPlayer==null) return;
            localPlayer.setXRot(xRot);
            localPlayer.setYRot(yRot);
        });
    }
}
