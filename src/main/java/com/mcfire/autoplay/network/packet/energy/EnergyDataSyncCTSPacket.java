package com.mcfire.autoplay.network.packet.energy;

import com.mcfire.autoplay.network.ModMessages;
import com.mcfire.autoplay.registry.ModCapabilityRegistryHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class EnergyDataSyncCTSPacket {
    // sync energy
    public EnergyDataSyncCTSPacket() {

    }

    public EnergyDataSyncCTSPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf){

    }

    public void handle(CustomPayloadEvent.Context context){
        context.enqueueWork(()->{
            ServerPlayer player=context.getSender();
            if(player==null) return;
            player
                    .getCapability(ModCapabilityRegistryHandler.POWERFUL_ITEM_INFO_VALUE_CAP)
                    .ifPresent((I)->
                            ModMessages.sendToPlayer(new EnergyDataSyncSTCPacket(I.getEnergy()),player));
        });
    }
}
