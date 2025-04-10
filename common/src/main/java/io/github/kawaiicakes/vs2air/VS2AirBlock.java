package io.github.kawaiicakes.vs2air;

import com.mojang.logging.LogUtils;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
public class VS2AirBlock extends Block {
    public static Logger LOGGER = LogUtils.getLogger();

    public static Properties defaultProperties() {
        Material vs2Air = new Material.Builder(MaterialColor.NONE).build();

        return BlockBehaviour.Properties.of(vs2Air)
                .strength(-1.0F, 3600000.8F)
                .noLootTable()
                .noOcclusion()
                .noCollission();
    }

    public VS2AirBlock() {
        super(defaultProperties());
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        super.onPlace(blockState, level, blockPos, blockState2, bl);
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if (!levelAccessor.isClientSide() && blockState2.isAir()) {
            String inOrOut = pointInPolygon(blockPos, direction, 20, levelAccessor) ? "INSIDE" : "OUTSIDE";
            LOGGER.info("VS2 Air at {} is {}", blockPos, inOrOut);
        }
        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    @Override
    public void randomTick(
            BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource
    ) {

    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState) {
        return false;
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {

    }

    /*
        Helper methods here
     */

    /**
     * Point-in-polygon method intended to be called when a block update (specifically a shape update) occurs next to a
     * {@link VS2AirBlock}. These Air blocks must have been or currently are inside a Compartment, and the test
     * occurs to determine whether the Compartment has suffered a breach (and thus is now "outside"). This is necessary
     * since players may make hull walls multiple layers thick and/or are concave. In such cases, a naive algorithm
     * checking only if vanilla air now exists adjacent to the VS2 Air on shape update would indicate a hull breach
     * has occurred when it never did. This solution is also far more performant than what I came up with before LOL
     * @param blockPos the position of the {@link VS2AirBlock} next to whom a shape update has occurred. This is the
     *                 point that is being tested.
     * @param direction the {@link Direction} of the shape update.
     * @param rayLength the <code>int</code> representing how far out the ray cast is made. This should in all cases be
     *                  at least equal to the length of the greatest side of the smallest possible bounding-box
     *                  enclosing all points composing the Compartment, plus 1.
     * @param level     the {@link Level} the Compartment is in.
     * @return <code>boolean</code> indicating if <code>blockPos</code> lies inside or outside the Compartment.
     */
    public static boolean pointInPolygon(BlockPos blockPos, Direction direction, int rayLength, LevelAccessor level) {
        BlockPos relative = blockPos.relative(direction);
        BlockPos.MutableBlockPos rayPos
                = new BlockPos.MutableBlockPos(relative.getX(), relative.getY(), relative.getZ());

        if (!level.getBlockState(rayPos).isAir()) return true;

        int numberOfIntersections = 0;
        boolean wasInEmpty = true;
        for (int i = 0; i < rayLength; i++) {
            rayPos.move(direction, 1);
            boolean posAtIsEmpty = level.getBlockState(rayPos).isAir();

            // only count an intersection when first touching an edge; not when leaving
            boolean intersected = !posAtIsEmpty && wasInEmpty;
            wasInEmpty = posAtIsEmpty;
            if (intersected) numberOfIntersections++;
        }

        if (numberOfIntersections == 0) return false;
        return (numberOfIntersections & 1) == 1;
    }

    /*
        Boilerplate implementations begin here
     */

    @Override
    public boolean canBeReplaced(BlockState blockState, Fluid fluid) {
        return false;
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext blockPlaceContext) {
        return blockPlaceContext.getItemInHand().isEmpty() || !blockPlaceContext.getItemInHand().is(this.asItem());
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
}
