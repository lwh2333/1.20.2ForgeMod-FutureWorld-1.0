package com.mcfire.autoplay.network.packet.skill.leg;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class LegThirdSkillParticleSTCPacket {
    private final Vec3 pos;
    public LegThirdSkillParticleSTCPacket(Vec3 pos) {
        this.pos=pos;
    }

    public LegThirdSkillParticleSTCPacket(FriendlyByteBuf buf){
        pos=buf.readVec3();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeVec3(pos);
    }

    public void handle(CustomPayloadEvent.Context context){
        context.enqueueWork(()->{
            LocalPlayer localPlayer= Minecraft.getInstance().player;
            if(localPlayer==null) return;
            double px=pos.x,py=pos.y,pz=pos.z;
            localPlayer.level().addParticle(ParticleTypes.SMOKE, px, py + 0.5D, pz, 0.0D, 0.0D, 0.0D);
        });
    }
}
