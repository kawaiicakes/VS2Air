package io.github.kawaiicakes.vs2air;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static io.github.kawaiicakes.vs2air.VS2AirControllerBlock.TICKING;

public class VS2AirController extends BlockEntity {
    protected UUID controllerId;
    protected BlockPos minPos;
    protected BlockPos maxPos;
    protected boolean[][][] compartmentShape;

    public VS2AirController(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);

        compoundTag.putUUID("controllerId", this.controllerId);
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);

        this.controllerId = compoundTag.getUUID("controllerId");
    }

    public static void tick(Level level, BlockPos pos, BlockState state, VS2AirController pEntity) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (!(state.getBlock() instanceof VS2AirControllerBlock)) return;
        if (!state.getValue(TICKING)) return;

        for (Direction faceDirection : Direction.values()) {
            BlockState blockAt = serverLevel.getBlockState(pos.relative(faceDirection));
            if (!blockAt.isAir()) continue;
        }
    }

    public boolean[][][] validateShapeIntegrity() {
        Set<BlockPos> toReturn = new HashSet<>();

        if (this.maxPos == null || this.minPos == null) return null;

        for (int y = this.minPos.getY(); y <= this.maxPos.getY(); y++) {

        }

        return null;
    }
}
