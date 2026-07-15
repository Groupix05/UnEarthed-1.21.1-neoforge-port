package lilypuree.unearthed.core;

import java.util.Objects;

import lilypuree.unearthed.Constants;
import lilypuree.unearthed.block.LichenBlock;
import lilypuree.unearthed.block.schema.BlockSchemas;
import lilypuree.unearthed.misc.BlockStatePropertiesMatch;
import lilypuree.unearthed.misc.HoeDig;
import lilypuree.unearthed.misc.RegolithItem;
import lilypuree.unearthed.platform.ForgePlatformHelper;
import lilypuree.unearthed.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ColoredFallingBlock;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.BuiltInRegistries;

public class Registration {
    public static final DeferredRegister<Block> BLOCKS;
    public static final DeferredRegister<Item> ITEMS;
    public static DeferredRegister<CreativeModeTab> CREATIVE_TABS;
    public static final DeferredHolder<Block, Block> LICHEN;
    public static final DeferredHolder<Block, ColoredFallingBlock> PYROXENE;
    public static final DeferredHolder<Block, Block> LIGNITE_BRIQUETTES;
    public static final CreativeModeTab tab;
    public static DeferredHolder<CreativeModeTab, CreativeModeTab> modeTab;
    public static final DeferredHolder<Item, BlockItem> PYROXENE_ITEM;
    public static final DeferredHolder<Item, RegolithItem> REGOLITH;
    public static final DeferredHolder<Item, BlockItem> LICHEN_ITEM;
    public static final DeferredHolder<Item, BlockItem> LIGNITE_BRIQUETTES_ITEM;

    public static void init(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        registerBlocks();
        registerItems();
        CREATIVE_TABS.register(bus);
    }

    public static void registerBlocks() {
        Constants.LOG.debug("UE: Registering blocks...");
        BlockSchemas.ROCK_TYPES.forEach((schema) -> schema.entries().forEach((entry) -> {
            DeferredHolder<Block, Block> block = BLOCKS.register(entry.getId(), () -> entry.createBlock(schema));
        }));
        Constants.LOG.info("UE: Blocks registered!");
    }

    public static void registerItems() {
        Constants.LOG.debug("UE: Registering items...");
        Item.Properties properties = new Item.Properties();
        BlockSchemas.ROCK_TYPES.forEach((schema) -> schema.entries().forEach((entry) -> {
            DeferredHolder<Item, BlockItem> item = ITEMS.register(entry.getId(), () -> new BlockItem(entry.getBlock(), properties));
            ForgePlatformHelper.addToMainTabItems(item);
        }));
        ForgePlatformHelper.addToMainTabItems(PYROXENE_ITEM);
        ForgePlatformHelper.addToMainTabItems(REGOLITH);
        ForgePlatformHelper.addToMainTabItems(LICHEN_ITEM);
        ForgePlatformHelper.addToMainTabItems(LIGNITE_BRIQUETTES_ITEM);
        Constants.LOG.info("UE: Items registered!");
    }

    public static void registerLootConditions(IEventBus bus) {
        HoeDig.init(bus);
        BlockStatePropertiesMatch.init();
    }

    static {
        BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, "unearthed");
        ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, "unearthed");
        CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "unearthed");
        LICHEN = BLOCKS.register("lichen", () -> new LichenBlock(Properties.of().noOcclusion().randomTicks().strength(0.2F).sound(SoundType.GRASS)));
        PYROXENE = BLOCKS.register("pyroxene", () -> new ColoredFallingBlock(new ColorRGBA(MapColor.STONE.col), Properties.of().strength(0.6F).sound(SoundType.GRAVEL)));
        LIGNITE_BRIQUETTES = BLOCKS.register("lignite_briquettes", () -> new Block(Properties.ofFullCopy(Blocks.COAL_BLOCK)));
        tab = Services.PLATFORM.createModTab("general", () -> new ItemStack(Objects.requireNonNull(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("unearthed", "chiseled_limestone")))));
        modeTab = CREATIVE_TABS.register("general", () -> tab);
        PYROXENE_ITEM = ITEMS.register("pyroxene", () -> new BlockItem(PYROXENE.get(), (new Item.Properties()).fireResistant()));
        REGOLITH = ITEMS.register("regolith", () -> new RegolithItem(new Item.Properties()));
        LICHEN_ITEM = ITEMS.register("lichen", () -> new BlockItem(LICHEN.get(), new Item.Properties()));
        LIGNITE_BRIQUETTES_ITEM = ITEMS.register("lignite_briquettes", () -> new BlockItem(LIGNITE_BRIQUETTES.get(), new Item.Properties()));
    }
}
