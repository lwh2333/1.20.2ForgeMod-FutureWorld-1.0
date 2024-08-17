package com.mcfire.autoplay.network.packet.skill;

import com.mcfire.autoplay.items.database.DataBase;
import com.mcfire.autoplay.network.ModMessages;
import com.mcfire.autoplay.network.packet.energy.EnergyDataSyncSTCPacket;
import com.mcfire.autoplay.registry.ModCapabilityRegistryHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class SkillCTSPacket {
    private final int skill;
    public SkillCTSPacket(int skill) {
        this.skill=skill;
    }

    public SkillCTSPacket(FriendlyByteBuf buf) {
        skill=buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(skill);
    }

    public void handle(CustomPayloadEvent.Context context){
        context.enqueueWork(()->{
            ServerPlayer player=context.getSender(); // get server player
            if(player==null) return;
            if(getHeldItem(player).getItem() instanceof DataBase){
                player
                        .getCapability(ModCapabilityRegistryHandler.POWERFUL_ITEM_INFO_VALUE_CAP)
                        .ifPresent((I)->{
                            ModMessages.sendToPlayer(new EnergyDataSyncSTCPacket(I.getEnergy()),player);
                            switch (skill) {
                                case 1 -> ((DataBase) getHeldItem(player).getItem()).first_skill(player);
                                case 2 -> ((DataBase) getHeldItem(player).getItem()).second_skill(player);
                                case 3 -> ((DataBase) getHeldItem(player).getItem()).third_skill(player);
                                case 4 -> ((DataBase) getHeldItem(player).getItem()).fourth_skill(player);
                            }
                        });
            }
        });
    }
    public static ItemStack getHeldItem(Player player){
        if(!(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof DataBase)) return player.getItemInHand(InteractionHand.OFF_HAND);
        else return player.getItemInHand(InteractionHand.MAIN_HAND);
    }
}
