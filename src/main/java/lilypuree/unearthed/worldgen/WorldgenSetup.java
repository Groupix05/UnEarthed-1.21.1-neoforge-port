package lilypuree.unearthed.worldgen;

import lilypuree.unearthed.worldgen.core.WorldgenTags;
import lilypuree.unearthed.worldgen.feature.gen.StoneType;
import lilypuree.unearthed.worldgen.misc.StoneTypeCodecJsonDataManager;
import lilypuree.unearthed.worldgen.misc.WorldgenDataLoaders;

/**
 * Self-contained replacement for what used to be provided by the separate "Hyle" library mod.
 * Initializes tags and the stone-type/region data loaders used by the stone replacer world-gen feature.
 */
public final class WorldgenSetup {
    private WorldgenSetup() {}

    public static void init() {
        WorldgenDataLoaders.init(new StoneTypeCodecJsonDataManager() {
            @Override
            public boolean fireEvent(StoneType stoneType) {
                // No external mod-compat event bus needed now that this is self-contained.
                return true;
            }
        });
        WorldgenTags.init();
    }
}
