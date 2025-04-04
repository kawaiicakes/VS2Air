package io.github.kawaiicakes.vs2air.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class StandardAirBlock extends VS2AirBlock {
    public static final BooleanProperty RANDOMLY_TICKS = BooleanProperty.create("randomly_ticks");

    public StandardAirBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(RANDOMLY_TICKS, false));
    }

    // FIXME: Fix flooding mechanic
    @Override
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext blockPlaceContext) {
        return super.canBeReplaced(blockState, blockPlaceContext);
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, Fluid fluid) {
        return super.canBeReplaced(blockState, fluid);
    }

    @Override
    public @NotNull BlockState updateShape(
            BlockState blockState,
            Direction direction, BlockState blockState2,
            LevelAccessor levelAccessor,
            BlockPos blockPos, BlockPos blockPos2
    ) {
        if (blockState2.getBlock() instanceof VS2AirBlock)
            return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);

        levelAccessor.scheduleTick(blockPos, this, 2);
        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState) {
        return blockState.getValue(RANDOMLY_TICKS);
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        int adjacent = 0;

        for (Direction direction : Direction.values()) {
            BlockState neighbourAt = serverLevel.getBlockState(blockPos.relative(direction));

            if (neighbourAt.isAir()) adjacent++;
        }

        if (adjacent == 0) {
            serverLevel.setBlock(blockPos, blockState.setValue(RANDOMLY_TICKS, false), 18);
            return;
        }

        if (randomSource.nextInt(adjacent) > 0) {
            serverLevel.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
            return;
        }

        serverLevel.setBlock(blockPos, blockState.setValue(RANDOMLY_TICKS, false), 18);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        for (Direction direction : Direction.values()) {
            BlockState neighbourAt = serverLevel.getBlockState(blockPos.relative(direction));

            if (!direction.equals(Direction.DOWN)) {
                FluidState fluidState = serverLevel.getFluidState(blockPos.relative(direction));

                if (!fluidState.is(Fluids.EMPTY)) {
                    serverLevel.setBlock(blockPos, fluidState.createLegacyBlock(), 3);
                    break;
                }
            }

            if (!neighbourAt.isAir()) continue;

            serverLevel.setBlock(blockPos, blockState.setValue(RANDOMLY_TICKS, true), 18);
            break;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(RANDOMLY_TICKS, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(RANDOMLY_TICKS);
    }
}
