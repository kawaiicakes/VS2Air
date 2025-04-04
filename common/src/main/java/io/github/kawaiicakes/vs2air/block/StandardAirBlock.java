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
import net.minecraft.world.level.material.FlowingFluid;
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
        FluidState fluidDownwards = Fluids.EMPTY.defaultFluidState();

        for (Direction direction : Direction.values()) {
            BlockState neighbourAt = serverLevel.getBlockState(blockPos.relative(direction));
            FluidState fluidAt = serverLevel.getFluidState(blockPos.relative(direction));

            if (direction.equals(Direction.DOWN)) fluidDownwards = fluidAt;

            if (!neighbourAt.isAir() && fluidAt.isEmpty()) continue;

            adjacent++;
        }

        if (adjacent == 0) {
            serverLevel.setBlock(blockPos, blockState.setValue(RANDOMLY_TICKS, false), 18);
            return;
        }

        final int random = randomSource.nextInt(adjacent);

        if (random > 0) {
            BlockState stateForPlacement = fluidDownwards.isEmpty()
                    ? Blocks.AIR.defaultBlockState()
                    : randomSource.nextInt((int)(400.0F / adjacent) + 1) == 0
                            ? getSourceFluidState(fluidDownwards).createLegacyBlock()
                            : Blocks.AIR.defaultBlockState();

            serverLevel.setBlock(blockPos, stateForPlacement, 2);
            return;
        }

        serverLevel.setBlock(blockPos, blockState.setValue(RANDOMLY_TICKS, false), 18);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        for (Direction direction : Direction.values()) {
            BlockState neighbourAt = serverLevel.getBlockState(blockPos.relative(direction));
            FluidState fluidState = serverLevel.getFluidState(blockPos.relative(direction));

            if (neighbourAt.isAir() && !blockState.getValue(RANDOMLY_TICKS)) {
                serverLevel.setBlock(blockPos, blockState.setValue(RANDOMLY_TICKS, true), 18);
            }

            if (direction.equals(Direction.DOWN)) continue;

            if (fluidState.isEmpty()) continue;

            serverLevel.setBlock(blockPos, getSourceFluidState(fluidState).createLegacyBlock(), 3);
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

    public static FluidState getSourceFluidState(FluidState fluidAt) {
        return fluidAt.getType() instanceof FlowingFluid flowingFluid
                ? flowingFluid.getSource().defaultFluidState()
                : fluidAt;
    }
}
