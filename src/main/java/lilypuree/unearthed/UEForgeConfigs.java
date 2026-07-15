package lilypuree.unearthed;

import net.neoforged.neoforge.common.ModConfigSpec;

public class UEForgeConfigs implements CommonConfig {

    public static ModConfigSpec COMMON_CONFIG;
    private ModConfigSpec.BooleanValue enableRegolithToDirt;
    private ModConfigSpec.BooleanValue enableDebug;
    private ModConfigSpec.BooleanValue disableReplacement;
    private ModConfigSpec.BooleanValue disableCobbleReplacement;
    private ModConfigSpec.BooleanValue disableDirtReplacement;
    private ModConfigSpec.BooleanValue disableOreReplacement;

    public UEForgeConfigs() {
        ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();
        COMMON_BUILDER.comment("Unearthed Configs\n");

        COMMON_BUILDER.push("general settings");
        enableRegolithToDirt = COMMON_BUILDER.define("enable_regolith_to_dirt_recipe", false);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.push("generation settings");
        enableDebug = COMMON_BUILDER.define("enable_debug", false);
        disableReplacement = COMMON_BUILDER.define("disable_all", false);
        disableCobbleReplacement = COMMON_BUILDER.define("disable_cobble", false);
        disableDirtReplacement = COMMON_BUILDER.define("disable_dirt", false);
        disableOreReplacement = COMMON_BUILDER.define("disable_ore", false);
        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    @Override
    public boolean enableRegolithToDirt() {
        return enableRegolithToDirt.get();
    }

    @Override
    public boolean enableDebug() {
        return enableDebug.get();
    }

    @Override
    public boolean disableReplacement() {
        return disableReplacement.get();
    }

    @Override
    public boolean disableCobbleReplacement() {
        return disableCobbleReplacement.get();
    }

    @Override
    public boolean disableDirtReplacement() {
        return disableDirtReplacement.get();
    }

    @Override
    public boolean disableOreReplacement() {
        return disableOreReplacement.get();
    }
}
