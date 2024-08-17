package com.mcfire.autoplay.entity.monster;

import com.mcfire.autoplay.cap.PowerfulItemInfoValue;
import com.mcfire.autoplay.network.ModMessages;
import com.mcfire.autoplay.network.packet.entity.RobotDataSyncSTCPacket;
import com.mcfire.autoplay.registry.ModCapabilityRegistryHandler;
import com.mcfire.autoplay.registry.ModItems;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RobotEntity extends Mob implements GeoEntity {
    public int skillTick=0;
    private final AnimatableInstanceCache cache=GeckoLibUtil.createInstanceCache(this);
    public RobotEntity(EntityType<? extends Mob> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    public static AttributeSupplier setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH,1000.0D)
                .add(Attributes.ATTACK_DAMAGE, 10F)
                .add(Attributes.ATTACK_SPEED,10F)
                .add(Attributes.MOVEMENT_SPEED,0F)
                .add(Attributes.KNOCKBACK_RESISTANCE,1.0D).build(); // no kb
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(6,new RandomLookAroundGoal(this));
        super.registerGoals();
    }

    private <E extends GeoAnimatable> PlayState predicate(AnimationState<E> state){
        state.getController().setAnimation(RawAnimation.begin().then("animation.robot.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    private <E extends GeoAnimatable> PlayState attackPredicate(AnimationState<E> state){
        switch (skillTick){
            case 100 -> { // first skill
                state.getController().setAnimation(RawAnimation.begin().then("animation.robot.attack1", Animation.LoopType.PLAY_ONCE));
                skillTick++;
            }
            case 200 -> { // second
                state.getController().setAnimation(RawAnimation.begin().then("animation.robot.attack2", Animation.LoopType.PLAY_ONCE));
                skillTick++;
            }
            case 300 -> { // third
                state.getController().setAnimation(RawAnimation.begin().then("animation.robot.attack3", Animation.LoopType.PLAY_ONCE));
                skillTick++;
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController(this, "controller",0,this::predicate));
        controllers.add(new AnimationController(this, "attackController",0,this::attackPredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.IRON_GOLEM_STEP;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource){
        return SoundEvents.IRON_GOLEM_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound(){
        return SoundEvents.IRON_GOLEM_DEATH;
    }

    @Override
    protected float getSoundVolume(){
        return 1F;
    }

    @Override
    public void push(double p_20286_, double p_20287_, double p_20288_) {
        // no kb
    }

    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        if( // damage type ignored
                (p_21016_.getEntity()!=null&&(p_21016_.getEntity() instanceof RobotEntity||p_21016_.getEntity() instanceof IronGolem))
                ||(p_21016_.equals(level().damageSources().onFire()))
                ||(p_21016_.equals(level().damageSources().lava()))
                ||(p_21016_.equals(level().damageSources().fall()))
                ||(p_21016_.equals(level().damageSources().hotFloor()))
                ||(p_21016_.is(DamageTypes.MOB_PROJECTILE))
                ||(p_21016_.is(DamageTypes.EXPLOSION))
        ) return false;
        return super.hurt(p_21016_, p_21017_);
    }

    @Override
    protected InteractionResult mobInteract(Player p_21472_, InteractionHand p_21473_) {
        if(this.level().isClientSide()) return InteractionResult.FAIL;
        ItemStack itemStack=p_21472_.getItemInHand(p_21473_);
        if(this.hasEffect(MobEffects.WEAKNESS)&&this.getHealth()<=50F&&itemStack.is(ModItems.LUBRICATING_OIL.get())){
            itemStack.shrink(1);
            this.discard();
            LazyOptional<PowerfulItemInfoValue> cap=p_21472_
                    .getCapability(ModCapabilityRegistryHandler.POWERFUL_ITEM_INFO_VALUE_CAP);
            cap.ifPresent((I)->{
                I.setHasRobot(true);
            });
            p_21472_.displayClientMessage(Component.translatable("info.autoplay.merge_robot"), false);
        }
        return InteractionResult.PASS;
    }

    @Override
    public void checkDespawn() {
        // is persistence
    }

    @Override
    public void tick() {
        super.tick();
        if(this.hasEffect(MobEffects.WEAKNESS)&&this.getHealth()<=50F){
            return;
        }
        if(isFighting()){ // at least 1 player in 100*100*100
            if(!this.level().isClientSide()) skillTick++;
            fight();
        }
        if(!this.level().isClientSide()) {
            if(skillTick>300) skillTick=0;
            tickSync();
        }
    }

    private void tickSync(){ // send a packet to client to sync 'skillTick'
        for(Entity entity:((ServerLevel)(this.level())).getEntities().getAll()){
            if(entity instanceof ServerPlayer serverPlayer){
                ModMessages.sendToPlayer(new RobotDataSyncSTCPacket(skillTick,this.getId()), serverPlayer);
            }
        }
    }

    private boolean isFighting(){
        Level level=this.level();
        for(Entity entity:level.getEntities(null, new AABB(this.getX()-50,this.getY()-50,this.getZ()-50,this.getX()+50,this.getY()+50,this.getZ()+50))){
            if(entity instanceof Player){
                return true;
            }
        }
        return false;
    }

    private void fight(){
        if(!this.level().isClientSide()){
            Level level=this.level();
            for(Entity entity:level.getEntities(null, new AABB(this.getX()-50,this.getY()-50,this.getZ()-50,this.getX()+50,this.getY()+50,this.getZ()+50))){
                if(entity instanceof LivingEntity && !(entity instanceof IronGolem) && !(entity instanceof RobotEntity)){
                    entity.hurt(level.damageSources().mobAttack(this), 1F);
                }
            } // all entity will hurt in 100*100*100 except iron golem and robot
        }
        Player nearstPlayer=this.level().getNearestPlayer(this, 50F);
        if(nearstPlayer==null) return;
        this.lookAt(nearstPlayer.createCommandSourceStack().getAnchor(), nearstPlayer.position());
        if(!this.level().isClientSide()){
            switch (skillTick){
                case 130 -> { // the animation duration is 30 ticks
                    PrimedTnt tnt=new PrimedTnt(EntityType.TNT, this.level());
                    tnt.setFuse(20);
                    tnt.setPos(nearstPlayer.getX(), nearstPlayer.getY(), nearstPlayer.getZ());
                    this.level().addFreshEntity(tnt);
                }
                case 215 -> { // the animation duration is 15 ticks
                    double yRot=nearstPlayer.getYRot();
                    double x=this.getX()+(3*Math.sin(Math.toRadians(yRot)));
                    double z=this.getZ()-(3*Math.cos(Math.toRadians(yRot)));
                    for(int i=0;i<5;i++){
                        IronGolem ironGolem=new IronGolem(EntityType.IRON_GOLEM, this.level()){
                            @Override
                            protected void dropAllDeathLoot(DamageSource p_21192_) {
                                // no loot
                            }
                        };
                        ironGolem.setPos(x,this.getY(),z);
                        this.level().addFreshEntity(ironGolem);
                        ironGolem.setPersistentAngerTarget(nearstPlayer.getUUID());
                    }
                }
                case 10 -> nearstPlayer.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 255)); // the animation duration is 10 ticks
            }
        }
    }

    @Override
    public void lookAt(EntityAnchorArgument.Anchor p_20033_, Vec3 p_20034_){ // only x z
        Vec3 vec3 = p_20033_.apply(this);
        double d0 = p_20034_.x - vec3.x;
        double d1 = p_20034_.z - vec3.z;
        this.setYRot(Mth.wrapDegrees((float)(Mth.atan2(d1, d0) * (double)(180F / (float)Math.PI)) - 90.0F));
        this.setYHeadRot(this.getYRot());
        this.yRotO = this.getYRot();
        this.yBodyRot = this.yHeadRot;
        this.yBodyRotO = this.yBodyRot;
    }
}
