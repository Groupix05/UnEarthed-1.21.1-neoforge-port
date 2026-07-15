//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package lilypuree.unearthed.platform;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import lilypuree.unearthed.platform.services.IPlatformHelper;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ForgePlatformHelper implements IPlatformHelper {
    public static BlockColors blockColors;
    public static ItemColors itemColors;
    public static final List<Supplier<? extends ItemLike>> MAIN_BLOCKS = new ArrayList();
    public static final List<Supplier<? extends ItemLike>> MAIN_ITEMS = new ArrayList();

    public CreativeModeTab createModTab(String name, Supplier<ItemStack> icon) {
        return CreativeModeTab.builder().title(Component.translatable("itemGroup.unearthed." + name)).icon(icon).displayItems((pParameters, pOutput) -> {
            MAIN_BLOCKS.forEach((itemLike) -> pOutput.accept(itemLike.get()));
            MAIN_ITEMS.forEach((itemLike) -> pOutput.accept(itemLike.get()));
        }).build();
    }

    public SoundType createSoundType(float volumeIn, float pitchIn, Supplier<SoundEvent> breakSoundIn, Supplier<SoundEvent> stepSoundIn, Supplier<SoundEvent> placeSoundIn, Supplier<SoundEvent> hitSoundIn, Supplier<SoundEvent> fallSoundIn) {
        // NeoForge's ForgeSoundType (lazy-resolving SoundType) location could not be confirmed for 1.21.1;
        // all call sites in this mod only ever pass already-registered vanilla SoundEvents constants, so
        // resolving the suppliers immediately here is equivalent and avoids depending on an uncertain API.
        return new SoundType(volumeIn, pitchIn, breakSoundIn.get(), stepSoundIn.get(), placeSoundIn.get(), hitSoundIn.get(), fallSoundIn.get());
    }

    public void setRenderLayer(Block block, RenderType renderType) {
        ItemBlockRenderTypes.setRenderLayer(block, renderType);
    }

    public void setBlockColor(Block block, BlockColor blockColor) {
        blockColors.register(blockColor, block);
    }

    public int getBlockColor(BlockState block, int color) {
        return blockColors.getColor(block, (BlockAndTintGetter)null, (BlockPos)null, color);
    }

    public void setItemColor(ItemLike item, ItemColor itemColor) {
        itemColors.register(itemColor, item);
    }

    public boolean isDiggingHoe(ItemStack item) {
        return item.getItem().canPerformAction(item, ItemAbilities.HOE_DIG);
    }

    public static <T extends Item> DeferredHolder<Item, T> addToMainTab(DeferredHolder<Item, T> itemLike) {
        MAIN_BLOCKS.add(itemLike);
        return itemLike;
    }

    public static <T extends Item> DeferredHolder<Item, T> addToMainTabItems(DeferredHolder<Item, T> itemLike) {
        MAIN_ITEMS.add(itemLike);
        return itemLike;
    }
}
