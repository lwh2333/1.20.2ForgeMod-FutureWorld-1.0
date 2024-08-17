package com.mcfire.autoplay.items;

import com.mcfire.autoplay.cap.PowerfulItemInfoValue;
import com.mcfire.autoplay.items.database.DataBase;
import com.mcfire.autoplay.registry.ModCapabilityRegistryHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jline.utils.Log;

public class USBDisk extends Item {
    public USBDisk(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level p_41432_, @NotNull Player p_41433_, @NotNull InteractionHand p_41434_) {
        if(p_41432_.isClientSide()) return super.use(p_41432_, p_41433_, p_41434_);
        if(!(p_41433_.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof DataBase database)) return super.use(p_41432_, p_41433_, p_41434_);
        LazyOptional<PowerfulItemInfoValue> cap=p_41433_
                .getCapability(ModCapabilityRegistryHandler.POWERFUL_ITEM_INFO_VALUE_CAP);
        cap.ifPresent((I)->{
            for(PowerfulItemInfo info:I.getPowerfulItemInfoValue()){
                if(info.getName().equals(database.getCapName())){
                    p_41433_.displayClientMessage(Component.translatable("info.autoplay."+database.getShortName()+"_lvl_info", info.getStage(), info.getLevel()),false);
                    p_41433_.displayClientMessage(Component.translatable("info.autoplay.qlty_info").append(getNameFromQualityId(info.getQuality())), false);
                }
            }
        });
        return super.use(p_41432_, p_41433_, p_41434_);
    }

    private static MutableComponent getNameFromQualityId(int quality){
        switch (quality){
            case 0 -> {
                return Component.translatable("name.autoplay.common").withStyle(ChatFormatting.GRAY);
            }
            case 1 -> {
                return Component.translatable("name.autoplay.rare").withStyle(ChatFormatting.AQUA);
            }
            case 2 -> {
                return Component.translatable("name.autoplay.epic").withStyle(ChatFormatting.DARK_PURPLE);
            }
            case 3 -> {
                return Component.translatable("name.autoplay.legendary").withStyle(ChatFormatting.GOLD);
            }
        }
        Log.warn("[AutoPlay] the quality is impossible: "+quality);
        return Component.empty();
    }
}
