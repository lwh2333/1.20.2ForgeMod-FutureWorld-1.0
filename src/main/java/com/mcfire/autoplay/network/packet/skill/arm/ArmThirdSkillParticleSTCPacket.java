package com.mcfire.autoplay.network.packet.skill.arm;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class ArmThirdSkillParticleSTCPacket {
    // particles can only be summoned in the client
    private final Vec3 pos;
    private final int cnt;
    public ArmThirdSkillParticleSTCPacket(Vec3 pos, int cnt) {
        this.pos=pos;
        this.cnt=cnt;
    }

    public ArmThirdSkillParticleSTCPacket(FriendlyByteBuf buf){
        pos=buf.readVec3();
        cnt=buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeVec3(pos);
        buf.writeInt(cnt);
    }

    public void handle(CustomPayloadEvent.Context context){
        context.enqueueWork(()->{
            LocalPlayer localPlayer=Minecraft.getInstance().player;
            if(localPlayer==null) return;
            double px=pos.x,py=pos.y,pz=pos.z;
            // witch is purple; snowflake is white
            if(cnt%2==0) localPlayer.level().addParticle(ParticleTypes.WITCH,px,py,pz,0,0,0);
            else localPlayer.level().addParticle(ParticleTypes.SNOWFLAKE,px,py,pz,0,0,0);
        });
    }
}
