package com.mcfire.autoplay.cap;

import com.mcfire.autoplay.items.PowerfulItemInfo;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;

public class PowerfulItemInfoValue implements INBTSerializable<CompoundTag> {
    private List<PowerfulItemInfo> powerfulItemInfoValue;
    private int energy;
    private int proficiency;
    private boolean hasRobot;

    public PowerfulItemInfoValue(){
        powerfulItemInfoValue=new ArrayList<>();
        energy=100;
        proficiency=0;
        hasRobot=false;
    }
    public List<PowerfulItemInfo> getPowerfulItemInfoValue() {
        return powerfulItemInfoValue;
    }

    public void setPowerfulItemInfoValue(List<PowerfulItemInfo> powerfulItemInfoValue) {
        this.powerfulItemInfoValue = powerfulItemInfoValue;
    }

    public int getEnergy() {
        return energy;
    }


    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public boolean hasRobot() {
        return hasRobot;
    }

    public void setHasRobot(boolean hasRobot) {
        this.hasRobot = hasRobot;
    }

    public int getProficiency() {
        return proficiency;
    }

    public void setProficiency(int proficiency) {
        this.proficiency = proficiency;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt=new CompoundTag();
        CompoundTag subNbt_1=new CompoundTag(); // to save powerful item info
        CompoundTag subNbt_2=new CompoundTag(); // to save energy and robot
        for(int i=0;i<powerfulItemInfoValue.size();i++){
            CompoundTag subSubNbt=new CompoundTag();
            subSubNbt.putString("name",powerfulItemInfoValue.get(i).getName());
            subSubNbt.putInt("level",powerfulItemInfoValue.get(i).getLevel());
            subSubNbt.putInt("stage",powerfulItemInfoValue.get(i).getStage());
            subSubNbt.putInt("quality",powerfulItemInfoValue.get(i).getQuality());
            subNbt_1.put("pii_part"+i,subSubNbt);
        }
        nbt.put("powerful_item_info_value",subNbt_1);

        subNbt_2.putInt("energy",energy);
        subNbt_2.putInt("proficiency",proficiency);
        subNbt_2.putBoolean("has_robot",hasRobot);
        nbt.put("other",subNbt_2);
        return nbt;
    }
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        List<PowerfulItemInfo> powerfulItemInfoValue=new ArrayList<>();
        CompoundTag subNbt_1=nbt.getCompound("powerful_item_info_value");
        CompoundTag subNbt_2=nbt.getCompound("other");
        for(int i=0;i<nbt.size();i++){
            CompoundTag subSubNbt=subNbt_1.getCompound("pii_part"+i);
            if(subSubNbt.getString("name").isEmpty()||subSubNbt.getString("name").equals("")){
                continue;
            }
            powerfulItemInfoValue.add(new PowerfulItemInfo(
                    subSubNbt.getInt("level"),
                    subSubNbt.getInt("stage"),
                    subSubNbt.getInt("quality"),
                    subSubNbt.getString("name")));
        }
        this.powerfulItemInfoValue=powerfulItemInfoValue;
        this.energy=subNbt_2.getInt("energy");
        this.proficiency=subNbt_2.getInt("proficiency");
        this.hasRobot=subNbt_2.getBoolean("has_robot");
    }
}
