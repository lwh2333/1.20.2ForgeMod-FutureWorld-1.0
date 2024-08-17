package com.mcfire.autoplay.items;

import com.mcfire.autoplay.cap.PowerfulItemInfoValue;
import com.mcfire.autoplay.items.database.DataBase;
import com.mcfire.autoplay.registry.ModCapabilityRegistryHandler;
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

import java.util.List;

public class KnowledgeBottle extends Item {
    public KnowledgeBottle(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        if(p_41432_.isClientSide()) return super.use(p_41432_, p_41433_, p_41434_);
        if(p_41434_==InteractionHand.OFF_HAND) return super.use(p_41432_, p_41433_, p_41434_);
        LazyOptional<PowerfulItemInfoValue> cap=p_41433_.
                getCapability(ModCapabilityRegistryHandler.POWERFUL_ITEM_INFO_VALUE_CAP);
        cap.ifPresent((I)->{
            List<PowerfulItemInfo> l_info=I.getPowerfulItemInfoValue();
            // database must be in the off hand
            if(p_41433_.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof DataBase){
                for(PowerfulItemInfo info:l_info){
                    if(info.getName().equals(((DataBase) p_41433_.getItemInHand(InteractionHand.OFF_HAND).getItem()).getCapName())){
                        if(!info.levelUp()) {
                            p_41433_.displayClientMessage(Component.translatable("info.autoplay.cannot_lvl_up").withStyle(ChatFormatting.RED),false);
                            return;
                        }
                        I.setPowerfulItemInfoValue(l_info);
                        p_41433_.getItemInHand(p_41434_).shrink(1);
                        p_41432_.playSound(null,p_41433_.getX(),p_41433_.getY(),p_41433_.getZ(),SoundEvents.PLAYER_LEVELUP,SoundSource.PLAYERS,1.0F,1.0F);
                        return;
                    }
                }
                p_41432_.playSound(null,p_41433_.getX(),p_41433_.getY(),p_41433_.getZ(),SoundEvents.ANVIL_PLACE,SoundSource.PLAYERS,1.0F,1.0F);
                p_41433_.displayClientMessage(Component.translatable("info.autoplay.no_powerful_"+((DataBase) p_41433_.getItemInHand(InteractionHand.OFF_HAND).getItem()).getShortName()).withStyle(ChatFormatting.RED),false);
                return;
            }
            p_41433_.displayClientMessage(Component.translatable("info.autoplay.database_to_off").withStyle(ChatFormatting.GOLD),false);
            p_41432_.playSound(null,p_41433_.getX(),p_41433_.getY(),p_41433_.getZ(),SoundEvents.ANVIL_PLACE,SoundSource.PLAYERS,1.0F,1.0F);
        });
        return super.use(p_41432_, p_41433_, p_41434_);
    }
}
