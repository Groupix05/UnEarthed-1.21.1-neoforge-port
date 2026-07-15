package lilypuree.unearthed.mixin.server;

import net.minecraft.core.Holder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NoiseBasedChunkGenerator.class)
public interface NoiseBasedChunkGeneratorAccessor {
    /** In 1.21 the default block belongs to the generator's settings, not the generator itself. */
    @Accessor(value = "settings")
    Holder<NoiseGeneratorSettings> getSettings();
}
