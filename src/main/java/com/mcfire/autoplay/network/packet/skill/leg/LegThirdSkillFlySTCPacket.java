package com.mcfire.autoplay.network.packet.skill.leg;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class LegThirdSkillFlySTCPacket {
    Vec3 vec3;
    public LegThirdSkillFlySTCPacket(Vec3 vec3) {
        this.vec3=vec3;
    }

    public LegThirdSkillFlySTCPacket(FriendlyByteBuf buf){
        vec3=buf.readVec3();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeVec3(vec3);
    }

    public void handle(CustomPayloadEvent.Context context){
        context.enqueueWork(()->{
            LocalPlayer localPlayer=Minecraft.getInstance().player;
            if(localPlayer==null) return;
            localPlayer.setDeltaMovement(localPlayer.getDeltaMovement().add(vec3));
            Vec3 vec31 = Minecraft.getInstance().player.getDeltaMovement();
            localPlayer.setDeltaMovement(localPlayer.getDeltaMovement().add(vec31.x, Minecraft.getInstance().player.onGround() ? 0.0D : vec31.y, vec31.z));
        });
    }
}
