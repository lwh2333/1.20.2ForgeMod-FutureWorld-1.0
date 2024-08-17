package com.mcfire.autoplay.blocks;

import com.mcfire.autoplay.items.PowerfulItemInfo;
import com.mcfire.autoplay.registry.ModCapabilityRegistryHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class Protector extends Block {
    // it can protect ender dragon before player right click
    public Protector(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
        if(p_60504_.isClientSide()) return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_);
        p_60506_.getCapability(ModCapabilityRegistryHandler.POWERFUL_ITEM_INFO_VALUE_CAP).ifPresent((I)->{
            for(PowerfulItemInfo info:I.getPowerfulItemInfoValue()){
                if(info.getStage()!=10) return;
            } // arm and leg >= stage 10
            if(p_60504_.dimension()==Level.END){
                // get the ender dragon (i know its so ... but i can't find any api can do this. i have searched EntitySelector before, but the package com.mojang.brigadier isw missing)
                List<Entity> enderDragons=p_60504_.getEntities((Entity) null, new AABB(-30000000, -30000000, -30000000, 30000000, 30000000, 30000000), (entity)->{return entity instanceof EnderDragon;});
                for(Entity entity:enderDragons){
                    entity.setInvulnerable(false);
                }
                p_60506_.displayClientMessage(Component.translatable("info.autoplay.protector_removed"), false);
            }
        });
        return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_);
    }
}
