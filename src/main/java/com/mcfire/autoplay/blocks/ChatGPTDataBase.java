package com.mcfire.autoplay.blocks;

import com.mcfire.autoplay.entity.monster.RobotEntity;
import com.mcfire.autoplay.registry.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

public class ChatGPTDataBase extends HorizontalDirectionalBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING; // only horizontal
    public ChatGPTDataBase(Properties properties) {
        super(properties);
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

    @Override
    public void randomTick(BlockState p_222954_, ServerLevel p_222955_, BlockPos p_222956_, RandomSource p_222957_) {
        if(p_222957_.nextInt(31)==0){ // 1/31
            p_222955_.setBlockAndUpdate(p_222956_, Blocks.AIR.defaultBlockState());
            RobotEntity robot=new RobotEntity(ModEntityTypes.ROBOT.get(), p_222955_);
            robot.setPos(p_222956_.getCenter());
            p_222955_.addFreshEntity(robot);
        }
    }
}
