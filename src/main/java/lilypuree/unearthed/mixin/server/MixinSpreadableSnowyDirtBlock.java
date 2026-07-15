package lilypuree.unearthed.mixin.server;

import lilypuree.unearthed.block.RegolithBlock;
import lilypuree.unearthed.block.RegolithGrassBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SpreadingSnowyDirtBlock.class, remap = false)
public class MixinSpreadableSnowyDirtBlock {

    @Inject(method = "randomTick", at = @At("HEAD"))
    private void onRandomTick(BlockState pState, ServerLevel worldIn, BlockPos blockpos, RandomSource pRandom, CallbackInfo ci) {
        BlockState newBlock = worldIn.getBlockState(blockpos);
        if (newBlock.getBlock() instanceof RegolithBlock && RegolithGrassBlock.regolithToGrassMap.containsKey(newBlock.getBlock()) && RegolithGrassBlock.canPropagate(newBlock, worldIn, blockpos)) {
            if (!worldIn.getFluidState(blockpos.above()).is(FluidTags.WATER)) {
                worldIn.setBlockAndUpdate(blockpos, RegolithGrassBlock.regolithToGrassMap.get(newBlock.getBlock()).defaultBlockState());
                worldIn.setBlockAndUpdate(blockpos.above(), Blocks.AIR.defaultBlockState());
            }
        }
    }
}
