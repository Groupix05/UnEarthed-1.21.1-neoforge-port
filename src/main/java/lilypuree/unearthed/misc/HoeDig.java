package lilypuree.unearthed.misc;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import java.util.Set;

import lilypuree.unearthed.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class HoeDig implements LootItemCondition {
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITIONS;
    static final HoeDig INSTANCE = new HoeDig();
    static final DeferredHolder<LootItemConditionType, LootItemConditionType> HOE_DIG;
    // MC 1.21+ loot conditions are (de)serialized via MapCodec instead of the old Gson-based Serializer interface.
    // This condition has no fields, so it's a singleton codec that always yields INSTANCE.
    public static final MapCodec<HoeDig> CODEC = MapCodec.unit(() -> INSTANCE);

    public static void init(IEventBus bus) {
        LOOT_CONDITIONS.register(bus);
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.TOOL);
    }

    public boolean test(LootContext lootContext) {
        ItemStack testTool = lootContext.getParamOrNull(LootContextParams.TOOL);
        return testTool != null && Services.PLATFORM.isDiggingHoe(testTool);
    }

    public static LootItemCondition.Builder builder() {
        return () -> INSTANCE;
    }

    static {
        LOOT_CONDITIONS = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, "unearthed");
        HOE_DIG = LOOT_CONDITIONS.register("hoe_dig", () -> new LootItemConditionType(CODEC));
    }

    @Override
    public LootItemConditionType getType() {
        return HOE_DIG.get();
    }
}
