package com.mcfire.autoplay.blocks;

import com.mcfire.autoplay.entity.blockentity.RefinedLubricatingOilPurifierEntity;
import com.mcfire.autoplay.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class RefinedLubricatingOilPurifierBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING; // only horizontal

    public RefinedLubricatingOilPurifierBlock(Properties p_49224_) {
        super(p_49224_);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_49820_) {
        return this.defaultBlockState().setValue(FACING, p_49820_.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState p_60528_, Mirror p_60529_) {
        return p_60528_.rotate(p_60529_.getRotation(p_60528_.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new RefinedLubricatingOilPurifierEntity(p_153215_, p_153216_);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState p_60515_, Level p_60516_, BlockPos p_60517_, BlockState p_60518_, boolean p_60519_) {
        if(p_60515_.getBlock()!=p_60518_.getBlock()){
            BlockEntity blockEntity=p_60516_.getBlockEntity(p_60517_);
            if(blockEntity instanceof RefinedLubricatingOilPurifierEntity entity){
                entity.drops(); // drops items when remove
            }
        }
        super.onRemove(p_60515_, p_60516_, p_60517_, p_60518_, p_60519_);
    }

    @Override
    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
        if(p_60504_.isClientSide()) return InteractionResult.SUCCESS;
        BlockEntity blockEntity=p_60504_.getBlockEntity(p_60505_);
        if(blockEntity instanceof RefinedLubricatingOilPurifierEntity){
            ((ServerPlayer)(p_60506_)).openMenu((RefinedLubricatingOilPurifierEntity)blockEntity, p_60505_);
        }
        return InteractionResult.CONSUME;
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return createTickerHelper(p_153214_, ModBlockEntities.REFINED_LUBRICATING_OIL_PURIFIER.get(),
                RefinedLubricatingOilPurifierEntity::tick);
    }
}
