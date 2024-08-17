package com.mcfire.autoplay.items.database;

import com.mcfire.autoplay.cap.PowerfulItemInfoValue;
import com.mcfire.autoplay.items.PowerfulItemInfo;
import com.mcfire.autoplay.network.ModMessages;
import com.mcfire.autoplay.network.packet.skill.leg.LegFirstSkillJumpSTCPacket;
import com.mcfire.autoplay.network.packet.skill.leg.LegSecondSkillSprintSTCPacket;
import com.mcfire.autoplay.network.packet.skill.leg.LegThirdSkillFlySTCPacket;
import com.mcfire.autoplay.network.packet.skill.leg.LegThirdSkillParticleSTCPacket;
import com.mcfire.autoplay.registry.ModCapabilityRegistryHandler;
import com.mcfire.autoplay.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingEvent;

public class PowerfulLegDataBase extends DataBase {
    public PowerfulLegDataBase(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public void first_skill(Player player) { // air jump
        if(!DataBase.isPlayerLevelSufficient(player,25,"powerful_leg")) {
            player.displayClientMessage(Component.translatable("info.autoplay.need_lvl",25).withStyle(ChatFormatting.RED),false);
            return;
        }
        if(player instanceof ServerPlayer serverPlayer){
            LazyOptional<PowerfulItemInfoValue> cap=serverPlayer
                    .getCapability(ModCapabilityRegistryHandler.POWERFUL_ITEM_INFO_VALUE_CAP);
            cap.ifPresent((I)->{
                if(I.getEnergy()<10) player.displayClientMessage(Component.translatable("info.autoplay.not_enough_energy").withStyle(ChatFormatting.RED),false);
                else {
                    serverPlayer.awardStat(Stats.JUMP);
                    ModMessages.sendToPlayer(new LegFirstSkillJumpSTCPacket(), serverPlayer);
                    I.setEnergy(I.getEnergy()-10);
                    MinecraftForge.EVENT_BUS.post(new LivingEvent.LivingJumpEvent(serverPlayer));
                }
            });
        }
    }

    @Override
    public void second_skill(Player player) { // sprint wherever
        if(!DataBase.isPlayerLevelSufficient(player,50,"powerful_leg")) {
            player.displayClientMessage(Component.translatable("info.autoplay.need_lvl",50).withStyle(ChatFormatting.RED),false);
            return;
        }
        if(player instanceof ServerPlayer serverPlayer){
            LazyOptional<PowerfulItemInfoValue> cap=serverPlayer
                    .getCapability(ModCapabilityRegistryHandler.POWERFUL_ITEM_INFO_VALUE_CAP);
            cap.ifPresent((I)->{
                if(I.getEnergy()<30) player.displayClientMessage(Component.translatable("info.autoplay.not_enough_energy").withStyle(ChatFormatting.RED),false);
                else {
                    // send a packet to client to invoke deltaMovement()
                    ModMessages.sendToPlayer(new LegSecondSkillSprintSTCPacket(), serverPlayer);
                    I.setEnergy(I.getEnergy()-30);
                }
            });
        }
    }

    @Override
    public void third_skill(Player player) { // like a rocket
        if(!DataBase.isPlayerLevelSufficient(player,75,"powerful_leg")) {
            player.displayClientMessage(Component.translatable("info.autoplay.need_lvl",75).withStyle(ChatFormatting.RED),false);
            return;
        }
        if(player instanceof ServerPlayer serverPlayer){
            LazyOptional<PowerfulItemInfoValue> cap=serverPlayer
                    .getCapability(ModCapabilityRegistryHandler.POWERFUL_ITEM_INFO_VALUE_CAP);
            cap.ifPresent((I)->{
                if(I.getEnergy()<50) player.displayClientMessage(Component.translatable("info.autoplay.not_enough_energy").withStyle(ChatFormatting.RED),false);
                else {
                    float xRot=serverPlayer.getXRot();
                    float yRot=serverPlayer.getYRot();
                    double y=-(-1*toDouble(6,Math.sin(toRad(xRot))));
                    double x=-(-1*toDouble(6,Math.cos(toRad(xRot)))*toDouble(6,Math.sin(toRad(yRot))));
                    double z=(-1*toDouble(6,Math.cos(toRad(xRot)))*toDouble(6,Math.cos(toRad(yRot))));
                    // explosion behind the player
                    serverPlayer.setInvulnerable(true);
                    serverPlayer.level().explode(null, serverPlayer.damageSources().explosion(null), null, serverPlayer.getX()+x, serverPlayer.getY(0.0625D)+y, serverPlayer.getZ()+z, 8.0F, false, Level.ExplosionInteraction.TNT);
                    serverPlayer.setInvulnerable(false);
                    // send a packet to client to summon particles(SMOKE)
                    ModMessages.sendToPlayer(new LegThirdSkillParticleSTCPacket(new Vec3(serverPlayer.getX()+x, serverPlayer.getY(0.0625D)+y, serverPlayer.getZ()+z)), serverPlayer);
                    // shoot the player like the snowball
                    float f = -Mth.sin(yRot * ((float)Math.PI / 180F)) * Mth.cos(xRot * ((float)Math.PI / 180F));
                    float f1 = -Mth.sin((xRot + 0.0F) * ((float)Math.PI / 180F));
                    float f2 = Mth.cos(yRot * ((float)Math.PI / 180F)) * Mth.cos(xRot * ((float)Math.PI / 180F));
                    Vec3 vec31 = (new Vec3(f, f1, f2)).normalize().scale(5D);
                    // send a packet to client to invoke deltaMovement()
                    ModMessages.sendToPlayer(new LegThirdSkillFlySTCPacket(vec31), serverPlayer);
                    I.setEnergy(I.getEnergy()-50);
                }
            });
        }
    }
    public static double toDouble(int bit,double num){
        return ((int)(num*Math.pow(10,bit)+0.5))/Math.pow(10,bit);
    }
    public static double toRad(double tr){
        return Math.PI/(180/tr);
    }
    @Override
    public void fourth_skill(Player player) { // stage up (jump to PowerfulArmDataBase.fourth_skill)
        if(!DataBase.isPlayerLevelSufficient(player,100,"powerful_leg")) {
            player.displayClientMessage(Component.translatable("info.autoplay.need_lvl",100).withStyle(ChatFormatting.RED),false);
            return;
        }
        if(!player.getItemInHand(InteractionHand.MAIN_HAND).is(ModItems.KNOWLEDGE_BOTTLE.get())){
            player.displayClientMessage(Component.translatable("info.autoplay.bottle_in_main").withStyle(ChatFormatting.GOLD),false);
            return;
        }
        new Thread(()->{
            LazyOptional<PowerfulItemInfoValue> cap=player
                    .getCapability(ModCapabilityRegistryHandler.POWERFUL_ITEM_INFO_VALUE_CAP);
            cap.ifPresent((I)->{
                if(!PowerfulLegDataBase.this.canStageUp(player,I,getCapName())) return;
                if(I.getEnergy()<100) {
                    player.displayClientMessage(Component.translatable("info.autoplay.not_enough_energy").withStyle(ChatFormatting.RED),false);
                    return;
                }
                I.setEnergy(-1);
                for(PowerfulItemInfo info:I.getPowerfulItemInfoValue()){
                    if(info.getName().equals("powerful_leg")){
                        info.resetLevel();
                        player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                        player.displayClientMessage(Component.translatable("info.autoplay.reading_info"),false);
                        sleep(1000);
                        if(Math.random()>=0.9){ //10% failure
                            player.displayClientMessage(Component.translatable("info.autoplay.read_failed").withStyle(ChatFormatting.RED),false);
                            player.level().playSound(null,player.getX(),player.getY(),player.getZ(), SoundEvents.ANVIL_PLACE, SoundSource.PLAYERS,1.0F,1.0F);
                            info.resetLevel();
                            I.setEnergy(0);
                            return;
                        }
                        player.displayClientMessage(Component.translatable("info.autoplay.read_successful").withStyle(ChatFormatting.GREEN),false);
                        player.level().playSound(null,player.getX(),player.getY(),player.getZ(),SoundEvents.PLAYER_LEVELUP,SoundSource.PLAYERS,1.0F,1.0F);
                        sleep(300);
                        player.displayClientMessage(Component.translatable("info.autoplay.writing_info"),false);
                        sleep(1000);
                        if(Math.random()>=0.9){
                            player.displayClientMessage(Component.translatable("info.autoplay.write_failed").withStyle(ChatFormatting.RED),false);
                            player.level().playSound(null,player.getX(),player.getY(),player.getZ(),SoundEvents.ANVIL_PLACE,SoundSource.PLAYERS,1.0F,1.0F);
                            info.resetLevel();
                            I.setEnergy(0);
                            return;
                        }
                        player.displayClientMessage(Component.translatable("info.autoplay.write_successful").withStyle(ChatFormatting.GREEN),false);
                        player.level().playSound(null,player.getX(),player.getY(),player.getZ(),SoundEvents.PLAYER_LEVELUP,SoundSource.PLAYERS,1.0F,1.0F);
                        sleep(300);
                        player.displayClientMessage(Component.translatable("info.autoplay.debugging_info"),false);
                        sleep(1000);
                        if(Math.random()>=0.9){
                            player.displayClientMessage(Component.translatable("info.autoplay.debug_failed").withStyle(ChatFormatting.RED),false);
                            player.level().playSound(null,player.getX(),player.getY(),player.getZ(),SoundEvents.ANVIL_PLACE,SoundSource.PLAYERS,1.0F,1.0F);
                            info.resetLevel();
                            I.setEnergy(0);
                            return;
                        }
                        player.displayClientMessage(Component.translatable("info.autoplay.debug_successful").withStyle(ChatFormatting.GREEN),false);
                        player.level().playSound(null,player.getX(),player.getY(),player.getZ(),SoundEvents.PLAYER_LEVELUP,SoundSource.PLAYERS,1.0F,1.0F);
                        if(info.stageUp()){
                            player.displayClientMessage(Component.translatable("info.autoplay."+getShortName()+"_stageup",info.getStage()).withStyle(ChatFormatting.GREEN),false);
                        }else {
                            player.displayClientMessage(Component.translatable("info.autoplay.cannot_stageup").withStyle(ChatFormatting.RED),false);
                        }
                        I.setEnergy(0);
                        return;
                    }
                }
            });
        }).start();
    }

    private static void sleep(long mills){
        try{
            Thread.sleep(mills);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String getCapName() {
        return "powerful_leg";
    }

    @Override
    public String getShortName() {
        return "leg";
    }
}
