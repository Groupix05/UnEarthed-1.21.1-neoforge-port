package lilypuree.unearthed.core;

import com.mojang.serialization.MapCodec;
import lilypuree.unearthed.Constants;
import lilypuree.unearthed.data.condition.RegolithToDirtEnabledCondition;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class UEConditions {
    private static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_SERIALIZERS =
            DeferredRegister.create(net.neoforged.neoforge.registries.NeoForgeRegistries.CONDITION_SERIALIZERS, Constants.MOD_ID);

    static {
        CONDITION_SERIALIZERS.register("regolith_to_dirt_enabled", () -> RegolithToDirtEnabledCondition.CODEC);
    }

    private UEConditions() {
    }

    public static void init(IEventBus modBus) {
        CONDITION_SERIALIZERS.register(modBus);
    }
}
