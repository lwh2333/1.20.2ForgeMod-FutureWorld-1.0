package com.mcfire.autoplay.items.database;

import com.mcfire.autoplay.network.ModMessages;
import com.mcfire.autoplay.network.packet.player.UpdateFacingSTCPacket;
import com.mcfire.autoplay.network.packet.skill.leg.LegFirstSkillJumpSTCPacket;
import com.mcfire.autoplay.registry.ModCapabilityRegistryHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RobotDataBase extends DataBase {
    public RobotDataBase(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public void first_skill(Player player) {
        if((!DataBase.isPlayerStageSufficient(player,6,"powerful_arm"))||(!DataBase.isPlayerStageSufficient(player,6,"powerful_leg"))) {
            player.displayClientMessage(Component.translatable("info.autoplay.need_stage",6).withStyle(ChatFormatting.RED),false);
            return;
        }
        player.getCapability(ModCapabilityRegistryHandler.POWERFUL_ITEM_INFO_VALUE_CAP).ifPresent((I)->{
            if(I.getEnergy()!=100){
                player.displayClientMessage(Component.translatable("info.autoplay.not_enough_energy").withStyle(ChatFormatting.RED),false);
                return;
            } else I.setEnergy(-1);
            int cnt=0;
            Level level=player.level();
            Block targetBlock=null;
            BlockPos pos1=player.getOnPos().offset(-49,-49,-49), pos2=player.getOnPos().offset(49,49,49);
            List<List<BlockPos>> paths=new ArrayList<>();
            List<BlockPos> l_targetPos=new ArrayList<>();
            float xRot=player.getXRot();
            float yRot=player.getYRot();
            for(double i=0;i<=5;i+=0.1){
                int y=(int)Math.floor(player.getEyeY()-i*Math.sin(Math.toRadians(xRot)));
                int x=(int)Math.floor(player.getX()-i*Math.cos(Math.toRadians(xRot))*Math.sin(Math.toRadians(yRot)));
                int z=(int)Math.floor(player.getZ()+i*Math.cos(Math.toRadians(xRot))*Math.cos(Math.toRadians(yRot)));
                if(!(level.getBlockState(new BlockPos(x,y,z)).is(Blocks.AIR))){
                    targetBlock=level.getBlockState(new BlockPos(x,y,z)).getBlock();
                    break;
                }
            } // get the target block
            if(targetBlock==null){
                I.setEnergy(100);
                return;
            }
            I.setProficiency(I.getProficiency()+1);
            Out:
            for(int x=pos1.getX();x<=pos2.getX();x++){
                for(int y=pos1.getY();y<=pos2.getY();y++){
                    for(int z=pos1.getZ();z<=pos2.getZ();z++){
                        BlockState blockState=level.getBlockState(new BlockPos(x,y,z));
                        if(blockState.is(targetBlock)){ // find target blocks
                            Util util=new Util();
                            // find a path and save it in list
                            List<BlockPos> path=util.getPath(new BlockPos(x,y,z), player, false);
                            if(path==null) continue;
                            paths.add(path);
                            l_targetPos.add(new BlockPos(x,y,z));
                            cnt++;
                            if(cnt==11) break Out;
                        }
                    }
                }
            }
            new Thread(()->{
                player.setInvulnerable(true);
                for(int i=0;i<paths.size();i++){
                    Util util=new Util();
                    util.walkTo(paths.get(i), player, l_targetPos.get(i), true);
                }
                player.setInvulnerable(false);
                I.setEnergy(0);
            }).start();
        });
    }

    @Override
    public void second_skill(Player player) {
        if((!DataBase.isPlayerStageSufficient(player,6,"powerful_arm"))||(!DataBase.isPlayerStageSufficient(player,6,"powerful_leg"))) {
            player.displayClientMessage(Component.translatable("info.autoplay.need_stage",6).withStyle(ChatFormatting.RED),false);
            return;
        }
        player.getCapability(ModCapabilityRegistryHandler.POWERFUL_ITEM_INFO_VALUE_CAP).ifPresent((I)->{
            if(I.getEnergy()!=100){
                player.displayClientMessage(Component.translatable("info.autoplay.not_enough_energy").withStyle(ChatFormatting.RED),false);
                return;
            } else I.setEnergy(-1);
            BlockPos targetPos=null;
            Level level=player.level();
            float xRot=player.getXRot();
            float yRot=player.getYRot();
            for(double i=0;i<=49;i+=0.1){
                int y=(int)Math.floor(player.getEyeY()-i*Math.sin(Math.toRadians(xRot)));
                int x=(int)Math.floor(player.getX()-i*Math.cos(Math.toRadians(xRot))*Math.sin(Math.toRadians(yRot)));
                int z=(int)Math.floor(player.getZ()+i*Math.cos(Math.toRadians(xRot))*Math.cos(Math.toRadians(yRot)));
                if(!(level.getBlockState(new BlockPos(x,y,z)).is(Blocks.AIR))){
                    targetPos=new BlockPos(x,y+1,z);
                    break;
                }
            }
            if(targetPos==null) {
                I.setEnergy(100);
                return;
            }
            I.setProficiency(I.getProficiency()+1);
            BlockPos finalTargetPos=targetPos;
            new Thread(()->{
                Util util=new Util();
                util.walkTo(util.getPath(finalTargetPos, player, false), player, finalTargetPos, false);
            }).start();
            I.setEnergy(0);
        });
    }

    @Override
    public void third_skill(Player player) {
        player.displayClientMessage(Component.translatable("info.autoplay.no_this_skill"), false);
    }

    @Override
    public void fourth_skill(Player player) {
        player.displayClientMessage(Component.translatable("info.autoplay.no_this_skill"), false);
    }

    @Override
    public String getCapName() {
        return "robot";
    }

    @Override
    public String getShortName() {
        return "robot";
    }

    public static class Util {
        private final Point[][][] map=new Point[99][99][99];
        private final int[] child_x=new int[]{-1,0,0,1},child_z=new int[]{0,1,-1,0};
        private BlockPos rootPos;
        private List<BlockPos> getPath(BlockPos targetPos, Player serverPlayer, boolean blatant){
            // bfs
            Level level=serverPlayer.level();
            Queue<BlockPos> queue=new LinkedList<>();
            rootPos=getPlayerPos(serverPlayer);
            queue.add(rootPos);
            for(int i=0;i<99;i++){
                for(int j=0;j<99;j++){
                    for(int k=0;k<99;k++){
                        map[i][j][k]=new Point();
                    }
                }
            }
            map[getIndex(rootPos).x][getIndex(rootPos).y][getIndex(rootPos).z].previous=null;
            while (!queue.isEmpty()){
                BlockPos pos=queue.poll();
                map[getIndex(pos).x][getIndex(pos).y][getIndex(pos).z].isPassed=true;
                if(map[getIndex(pos).x][getIndex(pos).y][getIndex(pos).z].previous!=null){
                    if((!isAccessible(level,pos,map[getIndex(pos).x][getIndex(pos).y][getIndex(pos).z].previous.get(map[getIndex(pos).x][getIndex(pos).y][getIndex(pos).z].previous.size()-1),serverPlayer))&&!blatant){ // if it is possible to pass
                        map[getIndex(pos).x][getIndex(pos).y][getIndex(pos).z].isPassed=false;
                        continue;
                    } else if(!blatant) { // jump(<=jump_boost.height) or fall(<=jump_boost.level+3)
                        List<BlockPos> pre=map[getIndex(pos).x][getIndex(pos).y][getIndex(pos).z].previous;
                        pos=pos.offset(0,getTheLowestEmptyFromPos(pos, map[getIndex(pos).x][getIndex(pos).y][getIndex(pos).z].previous.get(map[getIndex(pos).x][getIndex(pos).y][getIndex(pos).z].previous.size()-1), level),0);
                        map[getIndex(pos).x][getIndex(pos).y][getIndex(pos).z].previous=pre;
                        map[getIndex(pos).x][getIndex(pos).y][getIndex(pos).z].isPassed=true;
                    }
                }
                if(isXZEnd(pos,targetPos)){
                    return map[getIndex(pos).x][getIndex(pos).y][getIndex(pos).z].previous;
                }
                for(int i=0;i<4;i++){
                    BlockPos childPos=pos.offset(child_x[i],0,child_z[i]);
                    if(getIndex(childPos).x>98||getIndex(childPos).x<0||getIndex(childPos).y>98||getIndex(childPos).y<0||getIndex(childPos).z>98||getIndex(childPos).z<0){
                        continue;
                    }
                    if(map[getIndex(childPos).x][getIndex(childPos).y][getIndex(childPos).z].isPassed){
                        continue;
                    } else {
                        map[getIndex(childPos).x][getIndex(childPos).y][getIndex(childPos).z].isPassed=true;
                    }
                    queue.offer(childPos);
                    if(map[getIndex(pos).x][getIndex(pos).y][getIndex(pos).z].previous!=null){
                        map[getIndex(childPos).x][getIndex(childPos).y][getIndex(childPos).z].previous.addAll(map[getIndex(pos).x][getIndex(pos).y][getIndex(pos).z].previous);
                        map[getIndex(childPos).x][getIndex(childPos).y][getIndex(childPos).z].previous.add(pos);
                    }else {
                        map[getIndex(childPos).x][getIndex(childPos).y][getIndex(childPos).z].previous.add(pos);
                    }
                }
            }
            if(!blatant) return getPath(targetPos, serverPlayer, true); // if there is no way out, enable the blatant mode
            return null;
        }

        private void walkTo(List<BlockPos> path, Player serverPlayer, BlockPos targetPos, boolean destroy) {
            Level level=serverPlayer.level();
            for(BlockPos pos:path){
                if(!level.getBlockState(pos).is(Blocks.AIR)) level.destroyBlock(pos, true);
                if(!level.getBlockState(pos.offset(0,1,0)).is(Blocks.AIR)) level.destroyBlock(pos.offset(0,1,0), true);
                if(!Block.isShapeFullBlock(level.getBlockState(pos.offset(0,-1,0)).getShape(level, pos.offset(0,-1,0)))) level.setBlockAndUpdate(pos.offset(0,-1,0), Blocks.STONE.defaultBlockState());
                BlockPos playerPos=getPlayerPos(serverPlayer);
                if(pos.getY()>playerPos.getY()) jump(serverPlayer);
                moveTo(serverPlayer, pos);
            }
            if(targetPos.getY()<getPlayerPos(serverPlayer).getY()) {
                ModMessages.sendToPlayer(new UpdateFacingSTCPacket(90,serverPlayer.getYRot()), (ServerPlayer) serverPlayer);
                int CORRECT_VAL=0;
                while (!isEnd(getPlayerPos(serverPlayer),targetPos)){
                    if(level.getBlockState(getPlayerPos(serverPlayer).offset(0,-1,0)).getBlock().defaultDestroyTime()!=-1) level.destroyBlock(getPlayerPos(serverPlayer).offset(0,-1,0), true);
                    CORRECT_VAL++;
                    if(CORRECT_VAL>=200){ // to prevent some bugs
                        serverPlayer.displayClientMessage(Component.translatable("info.autoplay.pathfinding_bug"), false);
                        serverPlayer.teleportTo(targetPos.getCenter().x(),targetPos.getCenter().y(),targetPos.getCenter().z());
                        break;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if(targetPos.getY()!=getPlayerPos(serverPlayer).getY()){
                ModMessages.sendToPlayer(new UpdateFacingSTCPacket(-90,serverPlayer.getYRot()), (ServerPlayer) serverPlayer);
                int CORRECT_VAL=0;
                while (!isEnd(getPlayerPos(serverPlayer),targetPos)){
                    if(level.getBlockState(getPlayerPos(serverPlayer).offset(0,-1,0)).getBlock().defaultDestroyTime()!=-1) level.destroyBlock(getPlayerPos(serverPlayer).offset(0,2,0), true);
                    serverPlayer.teleportTo(serverPlayer.position().x(), serverPlayer.position().y()+1D, serverPlayer.position().z());
                    level.setBlockAndUpdate(getPlayerPos(serverPlayer).offset(0,-1,0), Blocks.STONE.defaultBlockState());
                    CORRECT_VAL++;
                    if(CORRECT_VAL>=200){
                        serverPlayer.displayClientMessage(Component.translatable("info.autoplay.pathfinding_bug"), false);
                        serverPlayer.teleportTo(targetPos.getCenter().x(),targetPos.getCenter().y(),targetPos.getCenter().z());
                        break;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(destroy){
                serverPlayer.displayClientMessage(Component.translatable("info.autoplay.pathfinding_pickup"), false);
                level.destroyBlock(targetPos, true);
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } // player can pick up items in 3s
        }

        private void jump(Player serverPlayer){ // that I don't want to write a new class
            serverPlayer.awardStat(Stats.JUMP);
            ModMessages.sendToPlayer(new LegFirstSkillJumpSTCPacket(), (ServerPlayer) serverPlayer);
            MinecraftForge.EVENT_BUS.post(new LivingEvent.LivingJumpEvent(serverPlayer));
        }

        private boolean isEnd(BlockPos now, BlockPos dest){
            int[] end_x=new int[]{1,0,0,-1,0,0};
            int[] end_y=new int[]{0,1,0,0,-1,0};
            int[] end_z=new int[]{0,0,1,0,0,-1};
            for(int i=0;i<6;i++){
                if(now.offset(end_x[i],end_y[i],end_z[i]).equals(dest)) return true;
            }
            return false;
        }

        private boolean isXZEnd(BlockPos now, BlockPos dest){
            return now.getX()==dest.getX()&&now.getZ()==dest.getZ();
        }

        private int getTheLowestEmptyFromPos(BlockPos now, BlockPos last, Level level){
            int i=now.getY();
            int j=now.getY();
            while(level.getBlockState(new BlockPos(now.getX(),j,now.getZ())).is(Blocks.AIR)&&level.getBlockState(new BlockPos(now.getX(),j+1,now.getZ())).is(Blocks.AIR)){
                j--;
            }
            if(j!=now.getY()) return j+1-last.getY();
            while((!level.getBlockState(new BlockPos(now.getX(),i,now.getZ())).is(Blocks.AIR))||(!level.getBlockState(new BlockPos(now.getX(),i+1,now.getZ())).is(Blocks.AIR))){
                i++;
                if(i==321){
                    return 321-last.getY();
                }
            }
            return i-last.getY();
        }

        private XYZ getIndex(BlockPos pos){
            return new XYZ(pos.getX()-rootPos.getX()+49, pos.getY()-rootPos.getY()+49, pos.getZ()-rootPos.getZ()+49);
        }

        private boolean isAccessible(Level level, BlockPos now, BlockPos last, Player player){
            int offset=getTheLowestEmptyFromPos(now, last, level);
            double jumpHeight=player.hasEffect(MobEffects.JUMP)?(player.getEffect(MobEffects.JUMP).getAmplifier()>0?2.5D:1.8125D):1.0D;
            if(offset>=(player.hasEffect(MobEffects.JUMP)?(player.getEffect(MobEffects.JUMP).getAmplifier()>0?-5:-4):-3)&&offset<0) return true;
            else if(offset<0) return false;
            return jumpHeight>=offset;
        }

        private BlockPos getPlayerPos(Player player){
            return new BlockPos((int)Math.floor(player.getX()),(int)Math.floor(player.getY()),(int)Math.floor(player.getZ()));
        }

        private void moveTo(Player serverPlayer, BlockPos pos){
            Vec3 distance=pos.getCenter().subtract(serverPlayer.position());
            while(!fakeEquals(serverPlayer.position(), pos.getCenter().subtract(0D,0.48D,0D))) {
                Vec3 vec3 = serverPlayer.position().add(distance.subtract(0, 0.48D, 0).scale(0.02D));
                serverPlayer.teleportTo(vec3.x(), vec3.y(), vec3.z());
                serverPlayer.walkAnimation.setSpeed(1F);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private boolean fakeEquals(Vec3 vec3, Vec3 vec31){ // player's bounding box is 0.6*0.6*h (1-0.6)/2=0.2
            return vec3.x()>=vec31.x()-0.2D&&vec3.x()<=vec31.x()+0.2D&&
                    vec3.y()>=vec31.y()-0.2D&&vec3.y()<=vec31.y()+0.2D&&
                    vec3.z()>=vec31.z()-0.2D&&vec3.z()<=vec31.z()+0.2D;
        }
    }
    public static class Point {
        private List<BlockPos> previous;
        private boolean isPassed;
        private Point(){
            previous=new ArrayList<>();
            isPassed=false;
        }
    }
    public static class XYZ {
        private final int x;
        private final int y;
        private final int z;
        public XYZ(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
