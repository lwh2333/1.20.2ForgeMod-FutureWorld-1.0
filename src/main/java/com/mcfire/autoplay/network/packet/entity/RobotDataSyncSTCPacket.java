package com.mcfire.autoplay.network.packet.entity;

import com.mcfire.autoplay.entity.monster.RobotEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class RobotDataSyncSTCPacket {
    private final int skillTick;
    private final int id;
    public RobotDataSyncSTCPacket(int skillTick, int id) {
        this.skillTick=skillTick;
        this.id=id;
    }

    public RobotDataSyncSTCPacket(FriendlyByteBuf buf) {
        skillTick=buf.readInt();
        id=buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(skillTick);
        buf.writeInt(id);
    }

    public void handle(CustomPayloadEvent.Context context){
        context.enqueueWork(()->{
            if(Minecraft.getInstance().player==null) return;
            RobotEntity clientBot=(RobotEntity)(Minecraft.getInstance().player.level().getEntity(id)); // id -> client bot
            clientBot.skillTick=skillTick; // sync
        });
    }
}
