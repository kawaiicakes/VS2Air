package io.github.kawaiicakes.vs2air.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.kawaiicakes.vs2air.block.VS2AirBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Prevents ALL flowing fluids from replacing this mod's air blocks.
 */
@Mixin(FlowingFluid.class)
public abstract class FlowingFluidMixin extends Fluid {
    // FIXME
    /**
     * I want to preserve any side effects that people may have added in the original method. Nevertheless, this
     * solution is really not ideal and I should find the preferred way of doing this ASAP.
     */
    @WrapMethod(method = "canSpreadTo")
    private boolean stopFluidReplacement(
            BlockGetter blockGetter,
            BlockPos blockPos, BlockState blockState,
            Direction direction, BlockPos blockPos2, BlockState blockState2,
            FluidState fluidState, Fluid fluid,
            Operation<Boolean> original
    ) {
        final boolean originalResult =
                original.call(blockGetter, blockPos, blockState, direction, blockPos2, blockState2, fluidState, fluid);

        if (blockState2.getBlock() instanceof VS2AirBlock) return false;

        return originalResult;
    }
}
