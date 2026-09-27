package lilypuree.unearthed.worldgen.compat;

import lilypuree.unearthed.worldgen.feature.gen.StoneType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.ModList;

import java.util.Set;

public final class TFMGCompat {
    private static final ResourceLocation LEAD_ORE = ResourceLocation.fromNamespaceAndPath("tfmg", "lead_ore");
    private static final ResourceLocation DEEPSLATE_LEAD_ORE = ResourceLocation.fromNamespaceAndPath("tfmg", "deepslate_lead_ore");
    private static final ResourceLocation LITHIUM_ORE = ResourceLocation.fromNamespaceAndPath("tfmg", "lithium_ore");
    private static final ResourceLocation DEEPSLATE_LITHIUM_ORE = ResourceLocation.fromNamespaceAndPath("tfmg", "deepslate_lithium_ore");
    private static final ResourceLocation NICKEL_ORE = ResourceLocation.fromNamespaceAndPath("tfmg", "nickel_ore");
    private static final ResourceLocation DEEPSLATE_NICKEL_ORE = ResourceLocation.fromNamespaceAndPath("tfmg", "deepslate_nickel_ore");
    private static final Set<String> SURFACE_STONES = Set.of(
            "phyllite", "slate", "gabbro", "granodiorite", "rhyolite", "white_granite",
            "limestone", "beige_limestone", "grey_limestone", "siltstone", "mudstone", "conglomerate"
    );
    private static final Set<String> DEEP_STONES = Set.of(
            "dark_slate", "deep_beige_limestone", "deep_grey_limestone", "deep_limestone", "deep_phyllite",
            "deep_conglomerate", "deep_gabbro", "deep_granodiorite", "deep_mudstone", "deep_siltstone", "deep_white_granite"
    );

    private TFMGCompat() {
    }

    public static void addOreReplacements(StoneType stoneType) {
        if (!ModList.get().isLoaded("tfmg")) {
            return;
        }

        ResourceLocation baseBlockId = BuiltInRegistries.BLOCK.getKey(stoneType.getBaseBlock().getBlock());
        if (!baseBlockId.getNamespace().equals("unearthed")) {
            return;
        }

        String stoneName = baseBlockId.getPath();
        if (SURFACE_STONES.contains(stoneName)) {
            stoneType.addOreReplacement(getBlock(LEAD_ORE), getBlock(stoneName + "_lead_ore").defaultBlockState());
            stoneType.addOreReplacement(getBlock(LITHIUM_ORE), getBlock(stoneName + "_lithium_ore").defaultBlockState());
            stoneType.addOreReplacement(getBlock(NICKEL_ORE), getBlock(stoneName + "_nickel_ore").defaultBlockState());
        } else if (DEEP_STONES.contains(stoneName)) {
            stoneType.addOreReplacement(getBlock(DEEPSLATE_LEAD_ORE), getBlock(stoneName + "_lead_ore").defaultBlockState());
            stoneType.addOreReplacement(getBlock(DEEPSLATE_LITHIUM_ORE), getBlock(stoneName + "_lithium_ore").defaultBlockState());
            stoneType.addOreReplacement(getBlock(DEEPSLATE_NICKEL_ORE), getBlock(stoneName + "_nickel_ore").defaultBlockState());
        }
    }

    private static Block getBlock(ResourceLocation id) {
        return BuiltInRegistries.BLOCK.get(id);
    }

    private static Block getBlock(String path) {
        return getBlock(ResourceLocation.fromNamespaceAndPath("unearthed", path));
    }
}
