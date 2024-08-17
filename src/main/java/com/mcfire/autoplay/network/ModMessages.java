package com.mcfire.autoplay.network;

import com.mcfire.autoplay.AutoPlay;
import com.mcfire.autoplay.network.packet.energy.EnergyDataSyncCTSPacket;
import com.mcfire.autoplay.network.packet.energy.EnergyDataSyncSTCPacket;
import com.mcfire.autoplay.network.packet.entity.RobotDataSyncSTCPacket;
import com.mcfire.autoplay.network.packet.player.UpdateFacingSTCPacket;
import com.mcfire.autoplay.network.packet.skill.SkillCTSPacket;
import com.mcfire.autoplay.network.packet.skill.arm.ArmThirdSkillParticleSTCPacket;
import com.mcfire.autoplay.network.packet.skill.leg.LegFirstSkillJumpSTCPacket;
import com.mcfire.autoplay.network.packet.skill.leg.LegSecondSkillSprintSTCPacket;
import com.mcfire.autoplay.network.packet.skill.leg.LegThirdSkillFlySTCPacket;
import com.mcfire.autoplay.network.packet.skill.leg.LegThirdSkillParticleSTCPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.*;

public class ModMessages {
    private static int packetId=0;
    private static SimpleChannel INSTANCE;
    public static int id(){
        return packetId++;
    }
    public static void register(){
        SimpleChannel net = ChannelBuilder
                .named(new ResourceLocation(AutoPlay.MODID,"messages"))
                .networkProtocolVersion(1)
                .clientAcceptedVersions((s,t)->true)
                .serverAcceptedVersions((s,t)->true)
                .simpleChannel();
        INSTANCE=net;
        net.messageBuilder(EnergyDataSyncSTCPacket.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(EnergyDataSyncSTCPacket::new)
                .encoder(EnergyDataSyncSTCPacket::toBytes)
                .consumerMainThread(EnergyDataSyncSTCPacket::handle)
                .add();
        net.messageBuilder(ArmThirdSkillParticleSTCPacket.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ArmThirdSkillParticleSTCPacket::new)
                .encoder(ArmThirdSkillParticleSTCPacket::toBytes)
                .consumerMainThread(ArmThirdSkillParticleSTCPacket::handle)
                .add();
        net.messageBuilder(LegFirstSkillJumpSTCPacket.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(LegFirstSkillJumpSTCPacket::new)
                .encoder(LegFirstSkillJumpSTCPacket::toBytes)
                .consumerMainThread(LegFirstSkillJumpSTCPacket::handle)
                .add();
        net.messageBuilder(LegSecondSkillSprintSTCPacket.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(LegSecondSkillSprintSTCPacket::new)
                .encoder(LegSecondSkillSprintSTCPacket::toBytes)
                .consumerMainThread(LegSecondSkillSprintSTCPacket::handle)
                .add();
        net.messageBuilder(LegThirdSkillFlySTCPacket.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(LegThirdSkillFlySTCPacket::new)
                .encoder(LegThirdSkillFlySTCPacket::toBytes)
                .consumerMainThread(LegThirdSkillFlySTCPacket::handle)
                .add();
        net.messageBuilder(LegThirdSkillParticleSTCPacket.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(LegThirdSkillParticleSTCPacket::new)
                .encoder(LegThirdSkillParticleSTCPacket::toBytes)
                .consumerMainThread(LegThirdSkillParticleSTCPacket::handle)
                .add();
        net.messageBuilder(RobotDataSyncSTCPacket.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(RobotDataSyncSTCPacket::new)
                .encoder(RobotDataSyncSTCPacket::toBytes)
                .consumerMainThread(RobotDataSyncSTCPacket::handle)
                .add();
        net.messageBuilder(UpdateFacingSTCPacket.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(UpdateFacingSTCPacket::new)
                .encoder(UpdateFacingSTCPacket::toBytes)
                .consumerMainThread(UpdateFacingSTCPacket::handle)
                .add();
        net.messageBuilder(SkillCTSPacket.class,id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SkillCTSPacket::new)
                .encoder(SkillCTSPacket::toBytes)
                .consumerMainThread(SkillCTSPacket::handle)
                .add();
        net.messageBuilder(EnergyDataSyncCTSPacket.class,id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(EnergyDataSyncCTSPacket::new)
                .encoder(EnergyDataSyncCTSPacket::toBytes)
                .consumerMainThread(EnergyDataSyncCTSPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG msg){
        if(
                Minecraft.getInstance().getConnection()!=null
        ){
            INSTANCE.send(msg, Minecraft.getInstance().getConnection().getConnection());
        }
    }

    public static <MSG> void sendToPlayer(MSG msg, ServerPlayer player){
        INSTANCE.send(msg, PacketDistributor.PLAYER.with(player));
    }
}
