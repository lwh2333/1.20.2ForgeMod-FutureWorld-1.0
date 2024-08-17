package com.mcfire.autoplay.items.powerfulitem;

import com.mcfire.autoplay.cap.PowerfulItemInfoValue;
import com.mcfire.autoplay.items.PowerfulItemInfo;
import com.mcfire.autoplay.items.database.DataBase;
import com.mcfire.autoplay.registry.ModCapabilityRegistryHandler;
import com.mcfire.autoplay.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jline.utils.Log;

import java.util.List;

public class PowerfulArm extends Item {
    public PowerfulArm(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level p_41432_, @NotNull Player p_41433_, @NotNull InteractionHand p_41434_) {
        if(p_41432_.isClientSide()) return super.use(p_41432_, p_41433_, p_41434_);
        if(p_41434_==InteractionHand.OFF_HAND&&!(p_41433_.getItemInHand(InteractionHand.MAIN_HAND)==ItemStack.EMPTY)) return super.use(p_41432_, p_41433_, p_41434_);
        LazyOptional<PowerfulItemInfoValue> cap=p_41433_.
                getCapability(ModCapabilityRegistryHandler.POWERFUL_ITEM_INFO_VALUE_CAP);
        cap.ifPresent((I)->{
            List<PowerfulItemInfo> l_info=I.getPowerfulItemInfoValue();
            for (PowerfulItemInfo info : l_info) {
                if (info == null) break;
                if (info.getName().equals("powerful_arm")) {
                    p_41433_.displayClientMessage(Component.translatable("info.autoplay."+((DataBase)(ModItems.POWERFUL_ARM_DATA_BASE.get())).getShortName()+"_installed").withStyle(ChatFormatting.RED),false);
                    p_41432_.playSound(null,p_41433_.getX(),p_41433_.getY(),p_41433_.getZ(),SoundEvents.ANVIL_PLACE,SoundSource.PLAYERS,1.0F,1.0F);
                    return;
                }
            }
            l_info.add(new PowerfulItemInfo(0,0,getQuality(),"powerful_arm"));
            I.setPowerfulItemInfoValue(l_info);
            p_41433_.displayClientMessage(Component.translatable("info.autoplay.inst_successful").withStyle(ChatFormatting.GREEN),false);
            p_41432_.playSound(null,p_41433_.getX(),p_41433_.getY(),p_41433_.getZ(),SoundEvents.ARMOR_EQUIP_NETHERITE,SoundSource.PLAYERS,1.0F,1.0F);
            if(!p_41433_.isCreative()) p_41433_.getItemInHand(p_41434_).shrink(1);
        });
        return super.use(p_41432_, p_41433_, p_41434_);
    }
    private static int getQuality(){ //70% for common, 20% for rare, 8% for epic, 2% for legendary
        double rand=Math.random();
        if(rand<0.7){
            return 0;
        }else if(rand<0.9){
            return 1;
        }else if(rand<0.98){
            return 2;
        }else {
            return 3;
        }
    }

    public static float getExtraDamageForOneStageByQuality(int quality){
        if(quality>3) Log.warn("[AutoPlay] the quality is impossible: "+quality);
        return switch (quality){
            case 0 -> 0.2F;
            case 1 -> 0.3F;
            case 2 -> 0.4F;
            case 3 -> 0.5F;
            default -> 0F;
        };
    }
}
