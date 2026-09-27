package lilypuree.unearthed.data.condition;

import com.mojang.serialization.MapCodec;
import lilypuree.unearthed.Constants;
import net.neoforged.neoforge.common.conditions.ICondition;

public final class RegolithToDirtEnabledCondition implements ICondition {
    public static final RegolithToDirtEnabledCondition INSTANCE = new RegolithToDirtEnabledCondition();
    public static final MapCodec<RegolithToDirtEnabledCondition> CODEC = MapCodec.unit(INSTANCE);

    private RegolithToDirtEnabledCondition() {
    }

    @Override
    public boolean test(IContext context) {
        return Constants.CONFIG.enableRegolithToDirt();
    }

    @Override
    public MapCodec<RegolithToDirtEnabledCondition> codec() {
        return CODEC;
    }
}
