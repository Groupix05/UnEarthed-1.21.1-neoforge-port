package lilypuree.unearthed.worldgen.misc;

import commoble.databuddy.data.CodecJsonDataManager;
import lilypuree.unearthed.Constants;
import lilypuree.unearthed.worldgen.feature.StoneRegion;
import lilypuree.unearthed.worldgen.feature.gen.StoneType;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class WorldgenDataLoaders {
    public static CodecJsonDataManager<StoneType> STONE_TYPE_LOADER;
    public static final CodecJsonDataManager<StoneRegion> REGION_LOADER = new CodecJsonDataManager<>(
            "hyledata/regions", StoneRegion.CODEC, Constants.LOG);

    public static void init(StoneTypeCodecJsonDataManager stoneTypeManager) {
        STONE_TYPE_LOADER = stoneTypeManager;
    }

    private static StoneType getStoneType(ResourceLocation rl) {
        return STONE_TYPE_LOADER.getData().get(rl);
    }

    public static StoneType getStoneType(String name) {
        if (name.equals("")) return StoneType.NO_REPLACE;
        if (name.contains(":")) return getStoneType(ResourceLocation.parse(name));
        return getStoneType(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
    }

    public static String getNameForStoneType(StoneType state) {
        ResourceLocation key = STONE_TYPE_LOADER.getData().inverse().get(state);
        if (key != null) return key.toString();
        return null;
    }

    public static ResourceLocation getNameForRegion(StoneRegion region) {
        return REGION_LOADER.getData().inverse().get(region);
    }

    public static Optional<StoneRegion> getRegion(ResourceLocation name) {
        return REGION_LOADER.getData().containsKey(name) ? Optional.of(REGION_LOADER.getData().get(name)) : Optional.empty();
    }
}
