package com.mcfire.autoplay.items.database;

import com.mcfire.autoplay.cap.PowerfulItemInfoValue;
import com.mcfire.autoplay.items.PowerfulItemInfo;
import com.mcfire.autoplay.registry.ModCapabilityRegistryHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.LazyOptional;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class DataBase extends Item {
    // make method invokes faster
    // (DataBase)item to invoke method
    public DataBase(Properties p_41383_) {
        super(p_41383_);
    }
    public abstract void first_skill(Player player);
    public abstract void second_skill(Player player);
    public abstract void third_skill(Player player);
    public abstract void fourth_skill(Player player);
    public abstract String getCapName();
    public abstract String getShortName();
    public static boolean isPlayerLevelSufficient(Player player, int level, String capName){
        // if the player can use skill
        LazyOptional<PowerfulItemInfoValue> cap=player
                .getCapability(ModCapabilityRegistryHandler.POWERFUL_ITEM_INFO_VALUE_CAP);
        AtomicBoolean ret = new AtomicBoolean(false);
        cap.ifPresent((I)->{
            List<PowerfulItemInfo> l_info=I.getPowerfulItemInfoValue();
            for(PowerfulItemInfo info:l_info){
                if(info.getName().equals(capName)&&info.getLevel()>=level){
                    ret.set(true);
                    return;
                }else if(info.getName().equals(capName)){
                    ret.set(false);
                    return;
                }
            }
        });
        return ret.get();
    }
    public static boolean isPlayerStageSufficient(Player player, int stage, String capName){
        // if the player can use skill
        LazyOptional<PowerfulItemInfoValue> cap=player
                .getCapability(ModCapabilityRegistryHandler.POWERFUL_ITEM_INFO_VALUE_CAP);
        AtomicBoolean ret = new AtomicBoolean(false);
        cap.ifPresent((I)->{
            List<PowerfulItemInfo> l_info=I.getPowerfulItemInfoValue();
            for(PowerfulItemInfo info:l_info){
                if(info.getName().equals(capName)&&info.getStage()>=stage){
                    ret.set(true);
                    return;
                }else if(info.getName().equals(capName)){
                    ret.set(false);
                    return;
                }
            }
        });
        return ret.get();
    }

    public boolean canStageUp(Player player, PowerfulItemInfoValue cap, String capName){
        for(PowerfulItemInfo info:cap.getPowerfulItemInfoValue()){
            if(info.getName().equals(capName)){
                if(info.getStage()>=2){
                    if(!cap.hasRobot()) {
                        player.displayClientMessage(Component.translatable("info.autoplay.need_robot"), false);
                        return false;
                    }
                }
                if(info.getStage()>=6){
                    if(cap.getProficiency()<100){
                        player.displayClientMessage(Component.translatable("info.autoplay.need_proficiency", cap.getProficiency()), false);
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
