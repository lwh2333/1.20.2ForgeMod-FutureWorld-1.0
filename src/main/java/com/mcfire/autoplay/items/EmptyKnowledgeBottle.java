package com.mcfire.autoplay.items;

import com.mcfire.autoplay.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class EmptyKnowledgeBottle extends Item {
    public EmptyKnowledgeBottle(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level p_41432_, @NotNull Player p_41433_, @NotNull InteractionHand p_41434_) {
        if(p_41432_.isClientSide()) return super.use(p_41432_, p_41433_, p_41434_);
        if(p_41433_.totalExperience>=100){
            p_41433_.giveExperiencePoints(-100);
            p_41433_.getItemInHand(p_41434_).shrink(1);
            // drop items if the inventory is full
            boolean flag=p_41433_.getInventory().add(ModItems.KNOWLEDGE_BOTTLE.get().getDefaultInstance());
            if(!flag){
                ItemEntity itementity = p_41433_.drop(ModItems.KNOWLEDGE_BOTTLE.get().getDefaultInstance(), false);
                if (itementity != null) {
                    itementity.setNoPickUpDelay();
                    itementity.setTarget(p_41433_.getUUID());
                }
            }
        }else{
            p_41433_.displayClientMessage(Component.translatable("info.autoplay.inexperienced").withStyle(ChatFormatting.RED),false);
        }
        return super.use(p_41432_, p_41433_, p_41434_);
    }
}
