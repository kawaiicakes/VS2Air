package io.github.kawaiicakes.vs2air.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

@SuppressWarnings("deprecation")
public class ShipAirBlock extends StandardAirBlock {
    public ShipAirBlock() {
        super(defaultProperties().strength(-1.0F, 9));
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext blockPlaceContext) {
        return false;
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        FluidState fluidDownwards = serverLevel.getFluidState(blockPos.relative(Direction.DOWN));
        if (fluidDownwards.isEmpty()) {
            serverLevel.setBlock(blockPos, blockState.setValue(RANDOMLY_TICKS, false), 18);
            return;
        }

        for (Direction direction : Direction.values()) {
            BlockState neighbourAt = serverLevel.getBlockState(blockPos.relative(direction));
            FluidState fluidAt = serverLevel.getFluidState(blockPos.relative(direction));
            if (!neighbourAt.isAir() && !fluidAt.isEmpty()) continue;
            if (randomSource.nextInt(1800) != 0) continue;
            serverLevel.setBlock(blockPos, getSourceFluidState(fluidDownwards).createLegacyBlock(), 2);
        }
    }

    @Override
    public void wasExploded(Level level, BlockPos blockPos, Explosion explosion) {
        super.wasExploded(level, blockPos, explosion);
        if (!(level instanceof ServerLevel serverLevel)) return;
        serverLevel.setBlock(blockPos, Fluids.WATER.defaultFluidState().createLegacyBlock(), 2);
    }
}
