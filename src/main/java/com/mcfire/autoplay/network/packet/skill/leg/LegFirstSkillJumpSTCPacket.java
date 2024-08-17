package com.mcfire.autoplay.network.packet.skill.leg;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class LegFirstSkillJumpSTCPacket {
    // deltaMovement() is only available in the client
    public LegFirstSkillJumpSTCPacket() {

    }

    public LegFirstSkillJumpSTCPacket(FriendlyByteBuf buf){

    }

    public void toBytes(FriendlyByteBuf buf){

    }

    public void handle(CustomPayloadEvent.Context context){
        context.enqueueWork(()->{
            LocalPlayer localPlayer= Minecraft.getInstance().player;
            if(localPlayer==null) return;
            Vec3 vec3 = localPlayer.getDeltaMovement();
            // getJumpBoostPower(): get the height with jump boost
            localPlayer.setDeltaMovement(vec3.x, 0.42F*getBlockJumpFactor(localPlayer)+localPlayer.getJumpBoostPower(), vec3.z);
            if (localPlayer.isSprinting()) {
                float f = localPlayer.getYRot() * ((float)Math.PI / 180F);
                localPlayer.setDeltaMovement(localPlayer.getDeltaMovement().add(-Mth.sin(f) * 0.2F, 0.0D, Mth.cos(f) * 0.2F));
            }
            localPlayer.hasImpulse = true;
        });
    }
    private float getBlockJumpFactor(Player player) { // get the height of the block
        float f = player.level().getBlockState(player.blockPosition()).getBlock().getJumpFactor();
        float f1 = player.level().getBlockState(getOnPos(0.500001F, player)).getBlock().getJumpFactor();
        return (double)f == 1.0D ? f1 : f;
    }
    private BlockPos getOnPos(float p_216987_, Player player) {
        if (player.mainSupportingBlockPos.isPresent()) {
            BlockPos blockpos = player.mainSupportingBlockPos.get();
            if (!(p_216987_ > 1.0E-5F)) {
                return blockpos;
            } else {
                BlockState blockstate = player.level().getBlockState(blockpos);
                return (!((double)p_216987_ <= 0.5D) || !blockstate.collisionExtendsVertically(player.level(), blockpos, player)) ? blockpos.atY(Mth.floor(player.position().y - (double)p_216987_)) : blockpos;
            }
        } else {
            int i = Mth.floor(player.position().x);
            int j = Mth.floor(player.position().y - (double)p_216987_);
            int k = Mth.floor(player.position().z);
            return new BlockPos(i, j, k);
        }
    }
}
