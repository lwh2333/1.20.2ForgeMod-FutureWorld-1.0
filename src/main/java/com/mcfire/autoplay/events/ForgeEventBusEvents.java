package com.mcfire.autoplay.events;

import com.mcfire.autoplay.AutoPlay;
import com.mcfire.autoplay.cap.PowerfulItemInfoValue;
import com.mcfire.autoplay.cap.PowerfulItemInfoValueProvider;
import com.mcfire.autoplay.items.PowerfulItemInfo;
import com.mcfire.autoplay.items.powerfulitem.PowerfulArm;
import com.mcfire.autoplay.items.powerfulitem.PowerfulLeg;
import com.mcfire.autoplay.registry.ModBlocks;
import com.mcfire.autoplay.registry.ModCapabilityRegistryHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AutoPlay.MODID)
public class ForgeEventBusEvents {
    @SubscribeEvent
    public static void onAttachCapabilityEvent(AttachCapabilitiesEvent<Entity> event){
        Entity entity=event.getObject();
        if(entity instanceof Player){
            event.addCapability(new ResourceLocation(AutoPlay.MODID,"powerful_item_info_value"),
                    new PowerfulItemInfoValueProvider());
        }
    } // register capability
    @SubscribeEvent
    public static void resetValueOnRespawn(PlayerEvent.Clone event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayerNew && event.getOriginal() instanceof ServerPlayer serverPlayerOld) {
            serverPlayerOld.reviveCaps();
            LazyOptional<PowerfulItemInfoValue> new_cap=serverPlayerNew.
                    getCapability(ModCapabilityRegistryHandler.POWERFUL_ITEM_INFO_VALUE_CAP);
            LazyOptional<PowerfulItemInfoValue> old_cap=serverPlayerOld.
                    getCapability(ModCapabilityRegistryHandler.POWERFUL_ITEM_INFO_VALUE_CAP);
            new_cap.ifPresent(I -> {
                old_cap.ifPresent(J ->{
                    I.deserializeNBT(J.serializeNBT());
                });
            });
            serverPlayerOld.invalidateCaps();
        }
    } // keep capability when dying

    @SubscribeEvent
    public static void addAttackDamage(LivingHurtEvent event){
        if(!(event.getSource().getEntity() instanceof Player player)) return;
        LazyOptional<PowerfulItemInfoValue> cap=player
                .getCapability(ModCapabilityRegistryHandler.POWERFUL_ITEM_INFO_VALUE_CAP);
        cap.ifPresent((I)->{
            for(PowerfulItemInfo info:I.getPowerfulItemInfoValue()){
                if(info.getName().equals("powerful_arm")){
                    event.setAmount(event.getAmount()+info.getStage()*PowerfulArm.getExtraDamageForOneStageByQuality(info.getQuality()));
                }
            }
        });
    } // add damage with the stage of the powerful arm

    @SubscribeEvent
    public static void velocity(LivingKnockBackEvent event){
        if(!(event.getEntity() instanceof Player player)||(event.getEntity().getLastHurtByMobTimestamp()!=event.getEntity().tickCount)||(event.getEntity().getLastHurtByMob()==null)) return;
        LazyOptional<PowerfulItemInfoValue> cap=player
                .getCapability(ModCapabilityRegistryHandler.POWERFUL_ITEM_INFO_VALUE_CAP);
        cap.ifPresent((I)->{
            for(PowerfulItemInfo info:I.getPowerfulItemInfoValue()){
                if(info.getName().equals("powerful_leg")){
                    event.setStrength((float)(event.getStrength()-event.getStrength()*info.getStage()*PowerfulLeg.getVelocityScaleForOneStageByQuality(info.getQuality())));
                }
            }
        });
    } // powerful leg

    @SubscribeEvent
    public static void initialize(EntityJoinLevelEvent event){
        if(!event.getLevel().isClientSide()&&event.getEntity() instanceof Player player){
            player.getCapability(ModCapabilityRegistryHandler.POWERFUL_ITEM_INFO_VALUE_CAP).ifPresent((I)->{
                if(I.getEnergy()==-1) I.setEnergy(0);
            });
        }
    }

    @SubscribeEvent
    public static void initializeLevelEnd(EntityEvent event){ // I can't find any api which can monitor loading ender dragon
        if(event.getEntity()==null) return;
        if(event.getEntity().level().isClientSide()||event.getEntity().level().dimension()!=Level.END||!(event.getEntity() instanceof EnderDragon)) return;
        event.getEntity().getServer().getLevel(Level.END).setBlockAndUpdate(new BlockPos(0,50,0), ModBlocks.PROTECTOR.get().defaultBlockState());
        if(event.getEntity().getTags().contains("initialized")){
            return;
        }
        event.getEntity().setInvulnerable(true);
        event.getEntity().addTag("initialized");
    }

    @SubscribeEvent
    public static void onPlayerCloneInEnd(PlayerEvent.Clone event){
        if(event.isWasDeath()) return;
        if(event.getEntity()==null||event.getOriginal()==null) return;
        event.getEntity().displayClientMessage(Component.translatable("info.autoplay.congrats").withStyle(ChatFormatting.LIGHT_PURPLE), false);
    }
}
