package io.github.kawaiicakes.vs2air.block;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
public abstract class VS2AirBlock extends Block {
    public static Properties defaultProperties() {
        return BlockBehaviour.Properties.of(Material.AIR).strength(-1.0F, 3600000.8F).noLootTable().noOcclusion().noCollission();
    }

    public VS2AirBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, Fluid fluid) {
        return false;
    }

    @Override
    public VoxelShape getShape(
            BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext
    ) {
        return collisionContext.isHoldingItem(this.asItem()) ? Shapes.block() : Shapes.empty();
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return 1.0F;
    }

    @Override
    public boolean isPossibleToRespawnInThis() {
        return true;
    }

    @Override
    public abstract boolean isRandomlyTicking(BlockState blockState);

    @Override
    public abstract void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource);

    @Override
    public abstract void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource);
}
