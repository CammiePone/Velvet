package ladysnake.satin.api.util;

import ladysnake.satin.impl.RenderLayerDuplicator;
import ladysnake.satin.mixin.client.render.MultiPhaseAccessor;
import net.minecraft.client.render.RenderLayer;
import org.apiguardian.api.API;

import java.util.function.Consumer;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

@API(status = EXPERIMENTAL, since = "2.4.0")
public final class RenderLayerHelper {
    /**
     * Copies a {@link RenderLayer} with all its parameters, but applies the given {@code phaseTransform} to the
     * copied parameters.
     *
     * <p>A typical use of this method is to create a new render layer with a different target (eg. a target
     * that supports shaders). For example:
     * <pre>{@code RenderLayerHelper.copy(
     *      RenderLayer.getTranslucent(),
     *      "mymod:special_layer",
     *      builder -> builder.target(someShaderEnablingTarget)
     * );}</pre>
     *
     * @param existing the render layer to duplicate
     * @param newName the name of the new render layer
     * @param phaseTransform a transformation operation to apply to the new layer's {@linkplain RenderLayer.MultiPhaseParameters.Builder parameter builder}
     * @return a {@link RenderLayer} with the same base parameters as {@code existing} but modified according to {@code phaseTransform}
     * @throws IllegalArgumentException if {@code existing} is not a {@code MultiPhase} render layer
     */
    @API(status = EXPERIMENTAL, since = "2.4.0")
    public static RenderLayer copy(RenderLayer existing, String newName, Consumer<RenderLayer.MultiPhaseParameters.Builder> phaseTransform) {
        return RenderLayerDuplicator.copy(existing, newName, phaseTransform);
    }

    @API(status = EXPERIMENTAL, since = "2.4.0")
    public static RenderLayer.MultiPhaseParameters copyPhaseParameters(RenderLayer existing, Consumer<RenderLayer.MultiPhaseParameters.Builder> phaseTransform) {
        return RenderLayerDuplicator.copyPhaseParameters((MultiPhaseAccessor) existing, phaseTransform);
    }
}
