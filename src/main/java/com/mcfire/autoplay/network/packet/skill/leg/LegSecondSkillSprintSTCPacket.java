package com.mcfire.autoplay.network.packet.skill.leg;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class LegSecondSkillSprintSTCPacket {
    public LegSecondSkillSprintSTCPacket() {

    }

    public LegSecondSkillSprintSTCPacket(FriendlyByteBuf buf){

    }

    public void toBytes(FriendlyByteBuf buf){

    }

    public void handle(CustomPayloadEvent.Context context){
        context.enqueueWork(()->{
            LocalPlayer localPlayer= Minecraft.getInstance().player;
            if(localPlayer==null) return;
            Vec3 vec3 = localPlayer.getDeltaMovement();
            float xRot=localPlayer.getXRot();
            float yRot=localPlayer.getYRot();
            double x=-(5*toDouble(6,toDouble(6,Math.sin(toRad(yRot)))));
            double z=(5*toDouble(6,toDouble(6,Math.cos(toRad(yRot)))));

            localPlayer.setDeltaMovement(0.42F*x, vec3.y, 0.42F*z);
            localPlayer.hasImpulse = true;
        });
    }
    public static double toDouble(int bit,double num){
        return ((int)(num*Math.pow(10,bit)+0.5))/Math.pow(10,bit);
    }
    public static double toRad(double tr){
        return Math.PI/(180/tr);
    }
}
