package lilypuree.unearthed;

import lilypuree.unearthed.compat.projecte.ProjectECompat;
import lilypuree.unearthed.core.Registration;
import lilypuree.unearthed.core.UEConditions;
import lilypuree.unearthed.worldgen.WorldgenSetup;
import lilypuree.unearthed.worldgen.core.WorldgenFeatures;
import lilypuree.unearthed.worldgen.misc.WorldgenDataLoaders;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

// NeoForge 1.21.1: mod constructor no longer takes FMLJavaModLoadingContext.
// Parameters are injected by type - request IEventBus (mod bus) and ModContainer directly.
@Mod(Constants.MOD_ID)
public class UnearthedForge {
    public UnearthedForge(IEventBus modbus, ModContainer container) {
        CommonSetup.init();
        Constants.CONFIG = new UEForgeConfigs();
        container.registerConfig(ModConfig.Type.COMMON, UEForgeConfigs.COMMON_CONFIG);
        UEConditions.init(modbus);
        Registration.init(modbus);
        Registration.registerLootConditions(modbus);
        modbus.addListener(this::commonSetup);
        if (ModList.get().isLoaded("projecte")) {
            modbus.addListener(ProjectECompat::init);
        }

        // Self-contained stone-replacer world-gen (formerly provided by the separate Hyle mod).
        WorldgenSetup.init();
        WorldgenFeatures.FEATURES.register(modbus);
        NeoForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> {
            event.addListener(WorldgenDataLoaders.STONE_TYPE_LOADER);
            event.addListener(WorldgenDataLoaders.REGION_LOADER);
        });
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        CommonSetup.commonSetup();
    }
}
