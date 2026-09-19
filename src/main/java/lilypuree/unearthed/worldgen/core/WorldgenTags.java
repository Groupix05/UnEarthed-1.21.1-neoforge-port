package lilypuree.unearthed.worldgen.core;

import lilypuree.unearthed.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class WorldgenTags {

    public static void init(){
        Blocks.init();
    }

    public static class Blocks {
        public static TagKey<Block> REPLACABLE;
        public static TagKey<Block> REPLACE_GRASS;
        public static TagKey<Block> REPLACE_DIRT;
        public static TagKey<Block> CREATE_ANDESITE_REPLACEABLE;
        
        public static void init(){
            WorldgenTags.Blocks.REPLACABLE = create("replaceable");
            WorldgenTags.Blocks.REPLACE_DIRT = create("replace_dirt");
            WorldgenTags.Blocks.REPLACE_GRASS = create("replace_grass");
            WorldgenTags.Blocks.CREATE_ANDESITE_REPLACEABLE = create("create_andesite_replaceable");
        }
        private static TagKey<Block> create(String name){
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
        }
    }
}
