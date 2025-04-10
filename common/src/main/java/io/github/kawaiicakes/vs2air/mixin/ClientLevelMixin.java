package io.github.kawaiicakes.vs2air.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.kawaiicakes.vs2air.VS2AirBlock;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.BlockItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;

/**
 * Permits rendering of this mod's air blocks just like barrier blocks
 */
@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {
    @WrapOperation(
            method = "getMarkerParticleTarget",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z"
            )
    )
    private boolean wrapGetMarkerParticleItems(Set<?> instance, Object o, Operation<Boolean> original) {
        if (!(o instanceof BlockItem item))
            return original.call(instance, o);

        if (item.getBlock() instanceof VS2AirBlock)
            return true;

        return original.call(instance, o);
    }
}
