package lilypuree.unearthed;

import lilypuree.unearthed.client.ClientCommon;
import lilypuree.unearthed.platform.ForgePlatformHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        ClientCommon.initRenderLayers();
    }

    @SubscribeEvent
    public static void onBlockColourHandleEvent(final RegisterColorHandlersEvent.Block event) {
        ForgePlatformHelper.blockColors = event.getBlockColors();
        ClientCommon.blockColors();
    }

    @SubscribeEvent
    public static void onItemColourHandlerEvent(final RegisterColorHandlersEvent.Item event) {
        ForgePlatformHelper.blockColors = event.getBlockColors();
        ForgePlatformHelper.itemColors = event.getItemColors();
        ClientCommon.itemColors();
    }
}