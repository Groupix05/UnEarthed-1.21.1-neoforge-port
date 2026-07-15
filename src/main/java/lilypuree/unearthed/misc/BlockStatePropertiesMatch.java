package lilypuree.unearthed.misc;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Set;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class BlockStatePropertiesMatch implements LootItemCondition {
    // MC 1.21+ loot conditions are (de)serialized via MapCodec instead of the old Gson-based Serializer interface.
    public static final MapCodec<BlockStatePropertiesMatch> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(c -> c.block),
            Codec.STRING.fieldOf("propertyA").forGetter(c -> c.propertyNameA),
            Codec.STRING.fieldOf("propertyB").forGetter(c -> c.propertyNameB)
    ).apply(instance, BlockStatePropertiesMatch::new));

    public static DeferredHolder<LootItemConditionType, LootItemConditionType> BLOCK_STATE_PROPERTIES_MATCH;
    private final Block block;
    private final String propertyNameA;
    private final String propertyNameB;

    public static void init() {
    }

    public BlockStatePropertiesMatch(Block block, String propertyNameA, String propertyNameB) {
        this.block = block;
        this.propertyNameA = propertyNameA;
        this.propertyNameB = propertyNameB;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.BLOCK_STATE);
    }

    public boolean test(LootContext lootContext) {
        BlockState blockState = (BlockState) lootContext.getParamOrNull(LootContextParams.BLOCK_STATE);
        if (blockState != null && this.block == blockState.getBlock()) {
            StateDefinition<Block, BlockState> container = blockState.getBlock().getStateDefinition();
            Property<?> propertyA = container.getProperty(this.propertyNameA);
            Property<?> propertyB = container.getProperty(this.propertyNameB);
            if (propertyA != null && propertyB != null) {
                return blockState.getValue(propertyA).equals(blockState.getValue(propertyB));
            }
        }

        return false;
    }

    public static Builder builder(Block blockIn) {
        return new Builder(blockIn);
    }

    static {
        BLOCK_STATE_PROPERTIES_MATCH = HoeDig.LOOT_CONDITIONS.register("block_state_properties_match", () -> new LootItemConditionType(CODEC));
    }

    @Override
    public LootItemConditionType getType() {
        return BLOCK_STATE_PROPERTIES_MATCH.get();
    }

    public static class Builder implements LootItemCondition.Builder {
        private final Block block;
        private String propertyNameA;
        private String propertyNameB;

        public Builder(Block blockIn) {
            this.block = blockIn;
        }

        public Builder propertiesToCompare(Property<?> propertyA, Property<?> propertyB) {
            this.propertyNameA = propertyA.getName();
            this.propertyNameB = propertyB.getName();
            return this;
        }

        @Override
        public LootItemCondition build() {
            return new BlockStatePropertiesMatch(this.block, this.propertyNameA, this.propertyNameB);
        }
    }
}
