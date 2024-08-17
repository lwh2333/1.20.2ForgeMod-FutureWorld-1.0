package com.mcfire.autoplay.items.database;

import com.mcfire.autoplay.cap.PowerfulItemInfoValue;
import com.mcfire.autoplay.items.PowerfulItemInfo;
import com.mcfire.autoplay.network.ModMessages;
import com.mcfire.autoplay.network.packet.skill.arm.ArmThirdSkillParticleSTCPacket;
import com.mcfire.autoplay.registry.ModCapabilityRegistryHandler;
import com.mcfire.autoplay.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;

import java.util.List;

public class PowerfulArmDataBase extends DataBase {
    public PowerfulArmDataBase(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public void first_skill(Player player) { // attack an entity in 10*4*10
        if(!DataBase.isPlayerLevelSufficient(player,25,"powerful_arm")) {
            player.displayClientMessage(Component.translatable("info.autoplay.need_lvl",25).withStyle(ChatFormatting.RED),false);
            return;
        }
        Level level=player.level();
        double x=player.getX(),y=player.getY(),z=player.getZ();
        List<Entity> entities=level.getEntities(null,new AABB(x+5,y+2,z+5,x-5,y-2,z-5));
        Entity entity= entities.stream().filter(entity1 -> entity1 instanceof Mob).findFirst().orElse(null);
        // find the first entity
        if(entity==null) return;
        try{
            LazyOptional<PowerfulItemInfoValue> cap=player
                    .getCapability(ModCapabilityRegistryHandler.POWERFUL_ITEM_INFO_VALUE_CAP);
            cap.ifPresent((I)->{
                if(I.getEnergy()<10) player.displayClientMessage(Component.translatable("info.autoplay.not_enough_energy").withStyle(ChatFormatting.RED),false);
                else{
                    if(entity.hurt(level.damageSources().playerAttack(player),(float)player.getAttributeValue(Attributes.ATTACK_DAMAGE))){
                        I.setEnergy(I.getEnergy()-10);
                        // if the mob hurts
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void second_skill(Player player) { // attack all entities in 6*6*6
        if(!DataBase.isPlayerLevelSufficient(player,50,"powerful_arm")) {
            player.displayClientMessage(Component.translatable("info.autoplay.need_lvl",50).withStyle(ChatFormatting.RED),false);
            return;
        }
        Level level=player.level();
        LazyOptional<PowerfulItemInfoValue> cap=player
                .getCapability(ModCapabilityRegistryHandler.POWERFUL_ITEM_INFO_VALUE_CAP);
        double x=player.getX(),y=player.getY(),z=player.getZ();
        // get entities (6*6*6)
        List<Entity> entities=level.getEntities(null,new AABB(x+3,y+3,z+3,x-3,y-3,z-3));
        cap.ifPresent((I)->{
            if(I.getEnergy()<30) {
                player.displayClientMessage(Component.translatable("info.autoplay.not_enough_energy").withStyle(ChatFormatting.RED),false);
                return;
            }
            boolean flag=false;
            for(Entity entity:entities){
                if(!(entity instanceof Mob)) continue;
                if(entity.hurt(level.damageSources().playerAttack(player),(float)player.getAttributeValue(Attributes.ATTACK_DAMAGE))){
                    flag=true;
                    // if at least one mob is hurt
                }
            }
            if(flag){
                I.setEnergy(I.getEnergy()-30);
            }
        });
    }

    @Override
    public void third_skill(Player player) { // fly backwards and summon particles. like an electromagnetic gun
        if(!DataBase.isPlayerLevelSufficient(player,75,"powerful_arm")) {
            player.displayClientMessage(Component.translatable("info.autoplay.need_lvl",75).withStyle(ChatFormatting.RED),false);
            return;
        }
        ServerPlayer serverPlayer=(ServerPlayer)player;
        LazyOptional<PowerfulItemInfoValue> cap=player
                .getCapability(ModCapabilityRegistryHandler.POWERFUL_ITEM_INFO_VALUE_CAP);
        cap.ifPresent((I)->{
            new Thread(()->{
                try{
                    if(I.getEnergy()<50) {
                        player.displayClientMessage(Component.translatable("info.autoplay.not_enough_energy").withStyle(ChatFormatting.RED),false);
                        return;
                    }
                    int originalEnergy=I.getEnergy(); // save the original energy
                    Vec3 originalPos=new Vec3(player.getX(), player.getY(), player.getZ());
                    I.setEnergy(-1); // disable charge
                    final double DISTANCE_TO_TELEPORT=0.2;
                    float xRot=player.getXRot();
                    float yRot=player.getYRot();
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,600,255));
                    for(double i=0;i<50;i+=0.2){ // i know that i can use deltaMovement. but it moves so fast
                        // get the pos for teleport
                        // ty means teleport_y
                        double ty=(DISTANCE_TO_TELEPORT*Math.sin(Math.toRadians(xRot)));
                        double tx=(DISTANCE_TO_TELEPORT*Math.cos(Math.toRadians(xRot))*Math.sin(Math.toRadians(yRot)));
                        double tz=-(DISTANCE_TO_TELEPORT*Math.cos(Math.toRadians(xRot))*Math.cos(Math.toRadians(yRot)));
                        player.teleportTo(player.getX()+tx,player.getY()+ty,player.getZ()+tz);
                        // u can try it, the sound is good :)
                        player.level().playSound(null,player.getX(),player.getY(),player.getZ(),SoundEvents.NOTE_BLOCK_BELL.value(),SoundSource.PLAYERS,(float)(i*0.2),(float)(i*0.05));
                        Thread.sleep(2);
                    }
                    for(int i=1;i<=1000;i++){ // I want to prevent players from falling from the sky. but idk how to do it.
                        player.teleportTo(player.getX(),player.getY(),player.getZ());
                        player.level().playSound(null,player.getX(),player.getY(),player.getZ(),SoundEvents.NOTE_BLOCK_BELL.value(),SoundSource.PLAYERS,10F,2.5F);
                    }
                    for(int i=-2;i<=2;i++){
                        for(int j=-2;j<=2;j++){
                            for(int cnt=1;cnt<=100;cnt++){
                                player.teleportTo(player.getX(),player.getY(),player.getZ()); // 666
                                // get the pos for summon particles
                                // py means particle_y
                                double py=player.getEyeY()-(cnt*Math.sin(Math.toRadians(xRot)));
                                double px=((player.getBoundingBox().minX+player.getBoundingBox().maxX)/2)-(cnt*Math.cos(Math.toRadians(xRot))*Math.sin(Math.toRadians(yRot)))+i;
                                double pz=((player.getBoundingBox().minZ+player.getBoundingBox().maxZ)/2)+(cnt*Math.cos(Math.toRadians(xRot))*Math.cos(Math.toRadians(yRot)))+j;
                                // render a particle rect
                                if((i==-2||i==2)||(j==-2||j==2)&&cnt>=20){
                                    // send a packet to client to render particle
                                    ModMessages.sendToPlayer(new ArmThirdSkillParticleSTCPacket(new Vec3(px,py,pz), cnt), serverPlayer);
                                }
                                player.level().destroyBlock(new BlockPos((int)px,(int)py,(int)pz),true,null);
                                for(Entity entity:player.level().getEntities(null,new AABB(px+0.5,py+0.5,pz+0.5,px-0.5,py-0.5,pz-0.5))){
                                    if(entity instanceof LivingEntity){
                                        entity.hurt(player.level().damageSources().playerAttack(player),50F);
                                    }
                                }
                            }
                        }
                    }
                    player.level().playSound(null,player.getX(),player.getY(),player.getZ(),SoundEvents.GENERIC_EXPLODE,SoundSource.PLAYERS,20.0F,1.0F);
                    // enable charge
                    I.setEnergy(originalEnergy-50);
                    player.teleportTo(originalPos.x(), originalPos.y(), originalPos.z());
                } catch (Exception e){
                    e.printStackTrace();
                    // enable charge
                    I.setEnergy(0);
                }
            }).start();
        });
    }

    @Override
    public void fourth_skill(Player player) { // stage up
        if(!DataBase.isPlayerLevelSufficient(player,100,"powerful_arm")) {
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
                if(!PowerfulArmDataBase.this.canStageUp(player,I,getCapName())) return;
                if(I.getEnergy()<100) {
                    player.displayClientMessage(Component.translatable("info.autoplay.not_enough_energy").withStyle(ChatFormatting.RED),false);
                    return;
                }
                I.setEnergy(-1);
                for(PowerfulItemInfo info:I.getPowerfulItemInfoValue()){
                    if(info.getName().equals("powerful_arm")){
                        info.resetLevel();
                        player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                        player.displayClientMessage(Component.translatable("info.autoplay.reading_info"),false);
                        sleep(1000);
                        if(Math.random()>=0.9){ //10% failure
                            player.displayClientMessage(Component.translatable("info.autoplay.read_failed").withStyle(ChatFormatting.RED),false);
                            player.level().playSound(null,player.getX(),player.getY(),player.getZ(),SoundEvents.ANVIL_PLACE,SoundSource.PLAYERS,1.0F,1.0F);
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
    public String getCapName() { // get capability name
        return "powerful_arm";
    }

    @Override
    public String getShortName() {
        return "arm";
    }
}
