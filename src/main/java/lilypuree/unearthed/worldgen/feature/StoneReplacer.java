package lilypuree.unearthed.worldgen.feature;

import lilypuree.unearthed.Constants;
import lilypuree.unearthed.mixin.server.NoiseBasedChunkGeneratorAccessor;
import lilypuree.unearthed.worldgen.core.WorldgenTags;
import lilypuree.unearthed.worldgen.feature.gen.*;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.neoforged.fml.ModList;

import java.util.List;

public class StoneReplacer extends Feature<StoneReplacerConfiguration> {
    private static final int CREATE_ANDESITE_VEINS_PER_CHUNK = 14;
    private static final int CREATE_ANDESITE_VEIN_SIZE = 33;
    private static final OreConfiguration CREATE_ANDESITE_CONFIGURATION = new OreConfiguration(
            List.of(OreConfiguration.target(new TagMatchTest(WorldgenTags.Blocks.CREATE_ANDESITE_REPLACEABLE), Blocks.ANDESITE.defaultBlockState())),
            CREATE_ANDESITE_VEIN_SIZE,
            0.0F
    );

    public StoneReplacer() {
        super(StoneReplacerConfiguration.CODEC);
    }

    private boolean isSeedSet = false;

    private boolean canReplace(ChunkGenerator generator) {
        if (generator instanceof NoiseBasedChunkGenerator nbcg) {
            return ((NoiseBasedChunkGeneratorAccessor) (Object) nbcg).getSettings().value().defaultBlock().is(Blocks.STONE);
        }
        return false;
    }

    @Override
    public boolean place(FeaturePlaceContext<StoneReplacerConfiguration> ctx) {
        if (Constants.CONFIG.disableReplacement() || !canReplace(ctx.chunkGenerator())) return false;
        WorldGenLevel level = ctx.level();
        StoneReplacerConfiguration config = ctx.config();
        if (!isSeedSet) {
            int seed = (int) level.getSeed();
            var frequencies = config.getFrequencies();
            NoiseHolder.setSeed(seed - 193864, frequencies.region());
            PrimaryNoiseSampler.setSeed(seed + 777261, frequencies.primary());
            SecondaryNoiseSampler.setSeed(seed + 390271, frequencies.secondary());
            RoughNoiseSampler.setSeed(seed + 1567241, frequencies.tertiary(), frequencies.unconformity());
            isSeedSet = true;
        }

        BlockPos pos = ctx.origin();
        basePos = pos;
        int minY = Math.max(pos.getY(), level.getMinBuildHeight());
        ChunkAccess chunkAccess = level.getChunk(pos);
        NoiseHolder noiseHolder = new NoiseHolder(config, level, pos.getX(), minY, pos.getZ());
        int maxHeight = noiseHolder.getMaxHeight(chunkAccess);
        if (maxHeight <= minY) return false;
        ChunkFiller filler = new ChunkFiller(level.getSeed(), new BlockPos(pos.getX(), minY, pos.getZ()), maxHeight - minY + 1);
        if (Constants.CONFIG.enableDebug()) {
            filler.fillIn(2, noiseHolder::getRoughNoise);
            filler.fillEmpty(2, noiseHolder::getStrataNoise);
            filler.fillInEdgesAndFaces(1);
            filler.fillInCentersRandom(1);
        } else {
            filler.fillIn(16, noiseHolder::getRoughNoise);
            filler.fillInEdgesAndFaces(8);
            filler.fillInCenters(8, noiseHolder::getRoughNoise);
            filler.fillEmpty(8, noiseHolder::getStrataNoise); //fills in the gaps that roughnoise did not fill
            filler.fillInEdgesAndFaces(4);
            filler.fillInCenters(4, noiseHolder::getCombinedNoise);
            filler.fillBiome(4, noiseHolder::getBiomeStrata);
            filler.fillInEdgesAndFaces(2);
            filler.fillInCentersFast(2);
            filler.fillInEdgesAndFaces(1);
            filler.fillInCentersRandom(1);
        }
        replaceAll(chunkAccess, minY, noiseHolder.getHeights(), filler.results());
        if (shouldGenerateCreateAndesite()) {
            generateCreateAndesite(ctx);
        }
        return true;
    }

    private static boolean shouldGenerateCreateAndesite() {
        return Constants.CONFIG.enableCreateAndesiteReserve() && ModList.get().isLoaded("create");
    }

    private static void generateCreateAndesite(FeaturePlaceContext<StoneReplacerConfiguration> ctx) {
        WorldGenLevel level = ctx.level();
        RandomSource random = ctx.random();
        int minY = level.getMinBuildHeight();
        int height = level.getMaxBuildHeight() - minY;
        BlockPos origin = ctx.origin();

        for (int vein = 0; vein < CREATE_ANDESITE_VEINS_PER_CHUNK; vein++) {
            BlockPos veinOrigin = new BlockPos(
                    origin.getX() + random.nextInt(16),
                    minY + random.nextInt(height),
                    origin.getZ() + random.nextInt(16)
            );
            Feature.ORE.place(CREATE_ANDESITE_CONFIGURATION, level, ctx.chunkGenerator(), random, veinOrigin);
        }
    }

    BlockPos basePos;

    private void replaceAll(ChunkAccess chunkAccess, int minY, int[][] heights, StoneType[][][] results) {
        int currentSectionIndex = chunkAccess.getSectionIndex(minY);
        LevelChunkSection chunkSection = chunkAccess.getSection(currentSectionIndex);
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int height = heights[x][z];
                for (int posY = minY; posY <= height; posY++) {
                    int sectionIndex = chunkAccess.getSectionIndex(posY);
                    if (currentSectionIndex != sectionIndex) {
                        chunkSection = chunkAccess.getSection(sectionIndex);
                        currentSectionIndex = sectionIndex;
                    }
                    if (chunkSection.hasOnlyAir()) continue;

                    BlockState block = chunkSection.getBlockState(x, posY & 15, z);
                    if (block.isAir()) continue;
                    BlockState replaced = results[x][z][posY - minY].replace(block);
                    if (block != replaced) {
                        chunkSection.setBlockState(x, posY & 15, z, replaced, false);
                    }
                }
            }
        }
    }
}
