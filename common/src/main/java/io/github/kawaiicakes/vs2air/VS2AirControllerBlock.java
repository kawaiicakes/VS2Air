package io.github.kawaiicakes.vs2air;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
public class VS2AirControllerBlock extends BaseEntityBlock {
    @ExpectPlatform
    public static BlockEntityType<VS2AirController> getControllerType() {
        throw new AssertionError("Architectury failed to replace this VS2 Air method with platform impl!");
    }

    public static Properties defaultProperties() {
        Material vs2Air = new Material.Builder(MaterialColor.NONE).build();

        return BlockBehaviour.Properties.of(vs2Air)
                .strength(-1.0F, 3600000.8F)
                .noLootTable();
    }

    public static BooleanProperty TICKING = BooleanProperty.create("ticking");

    public VS2AirControllerBlock() {
        super(defaultProperties());
        this.registerDefaultState(this.stateDefinition.any().setValue(TICKING, false));
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }

    /*
        Boilerplate block stuff begins here
     */

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(TICKING, true);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TICKING);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    /*
        Boilerplate BE stuff begins here
     */

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new VS2AirController(getControllerType(), blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState blockState, BlockEntityType<T> blockEntityType
    ) {
        return createTickerHelper(blockEntityType, getControllerType(), VS2AirController::tick);
    }
}
