package com.mcfire.autoplay.network.packet.energy;

import com.mcfire.autoplay.client.ClientEnergyData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class EnergyDataSyncSTCPacket {
    // sync energy
    private final int energy;

    public EnergyDataSyncSTCPacket(int energy) {
        this.energy = energy;
    }

    public EnergyDataSyncSTCPacket(FriendlyByteBuf buf){
        energy=buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(energy);
    }

    public void handle(CustomPayloadEvent.Context context){
        context.enqueueWork(()->{
            ClientEnergyData.setEnergy(energy);
        });
    }
}
