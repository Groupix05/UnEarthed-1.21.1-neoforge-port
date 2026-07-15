package lilypuree.unearthed.mixin.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import lilypuree.unearthed.client.LichenColors;
import net.minecraft.Util;
import net.minecraft.client.color.block.BlockTintCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(value = ClientLevel.class, remap = false)
public abstract class MixinClientWorld {

    @Mutable
    @Final
    @Shadow
    private Object2ObjectArrayMap<ColorResolver, BlockTintCache> tintCaches;

    @Shadow public abstract int calculateBlockTint(BlockPos $$0, ColorResolver $$1);

    @Inject(at = @At("RETURN"), method = "<init>")
    private void addLichenColorCaches(ClientPacketListener clientPlayNetHandler, ClientLevel.ClientLevelData clientWorldInfo, ResourceKey<Level> key, Holder<DimensionType> type, int viewDistance, int serverSimulationDistance, Supplier<ProfilerFiller> profiler, LevelRenderer renderer, boolean isDebug, long seed, CallbackInfo ci) {
        tintCaches = Util.make(new Object2ObjectArrayMap<>(tintCaches.size() + 1), map -> {
            map.putAll(tintCaches);
            map.put(LichenColors.LICHEN_COLOR, new BlockTintCache(i->this.calculateBlockTint(i, LichenColors.LICHEN_COLOR)));
        });
    }

    // TODO: setLichenBlendRadius is disabled for the 1.21.1 port.
    // It used to inject into calculateBlockTint at the point where the vanilla algorithm allocated
    // a `Cursor3D` to widen the tint blend radius for lichen. In 1.21.1 the block-tint caching
    // algorithm was rewritten (see BlockTintCache) and Cursor3D no longer exists there, so this
    // injection point is gone. Effect if left disabled: lichen tint just uses the default blend
    // radius instead of a custom one - a minor visual-only difference, not a functional bug.
    // To restore it, inspect the decompiled ClientLevel#calculateBlockTint / BlockTintCache source
    // for 1.21.1 and find the new equivalent injection point.
}
