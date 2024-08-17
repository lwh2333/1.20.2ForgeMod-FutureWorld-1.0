package com.mcfire.autoplay.items;

import com.mcfire.autoplay.cap.PowerfulItemInfoValue;
import com.mcfire.autoplay.registry.ModCapabilityRegistryHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class iCharge extends Item {
    //美国有苹果，中国有红枣。红枣牌充电宝，开机爆炸，关机爆炸，不买也会爆炸。 qwq
    public iCharge(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack p_41409_, Level p_41410_, @NotNull LivingEntity p_41411_) {
        if(p_41410_.isClientSide()) return super.finishUsingItem(p_41409_, p_41410_, p_41411_);
        if(!(p_41411_ instanceof Player player)) return super.finishUsingItem(p_41409_, p_41410_, p_41411_);
        LazyOptional<PowerfulItemInfoValue> cap=player
                .getCapability(ModCapabilityRegistryHandler.POWERFUL_ITEM_INFO_VALUE_CAP);
        cap.ifPresent((I)->{
            if(I.getEnergy()>=0) {
                I.setEnergy(100);
                player.displayClientMessage(Component.nullToEmpty("美国有苹果，中国有红枣。红枣牌充电宝，开机爆炸，关机爆炸，不买也会爆炸。"),false);
            } else player.displayClientMessage(Component.translatable("info.autoplay.cannot_charge").withStyle(ChatFormatting.RED),false);
        });
        return super.finishUsingItem(p_41409_, p_41410_, p_41411_);
    }
}
