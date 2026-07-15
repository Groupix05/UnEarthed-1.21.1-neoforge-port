package lilypuree.unearthed.worldgen.core;

import lilypuree.unearthed.Constants;
import lilypuree.unearthed.worldgen.feature.StoneReplacer;
import lilypuree.unearthed.worldgen.feature.StoneReplacerConfiguration;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class WorldgenFeatures {
    private WorldgenFeatures() {}
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, Constants.MOD_ID);
    public static final DeferredHolder<Feature<?>, Feature<StoneReplacerConfiguration>> STONE_REPLACER =
            FEATURES.register("stone_replacer", StoneReplacer::new);
    /** Separate feature id used by consumers that apply replacement below the deepslate transition. */
    public static final DeferredHolder<Feature<?>, Feature<StoneReplacerConfiguration>> DEEPSLATE_REPLACER =
            FEATURES.register("deepslate_replacer", StoneReplacer::new);
}
