package lilypuree.unearthed.mixin.server;

import lilypuree.unearthed.block.schema.BlockSchemas;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LiquidBlock.class, remap = false)
public abstract class MixinFlowingFluidBlock {


    @Shadow
    protected abstract void fizz(LevelAccessor p_180688_1_, BlockPos p_180688_2_);

    @Inject(method = "shouldSpreadLiquid", at = @At("HEAD"), cancellable = true)
    private void onReactWithNeighbors(Level level, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        // Originally this checked only the single neighbor direction the vanilla method's internal
        // flood-fill loop happened to be looking at (captured via a local variable). Capturing that
        // local requires obfuscation-mapping data that NeoForge 1.21.1 no longer provides at compile
        // time, so instead we just check all 4 horizontal neighbors ourselves up front.
        boolean isClay = level.getBlockState(pos.below()).is(Blocks.CLAY);
        boolean isSnow = level.getBlockState(pos.below()).is(Blocks.SNOW_BLOCK);
        if (!isClay && !isSnow) {
            return;
        }

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos blockPos = pos.relative(direction);

            if (isClay && level.getBlockState(blockPos).is(Blocks.BLUE_ICE)) {
                Block block = BlockSchemas.DACITE.getBaseBlock();
                level.setBlockAndUpdate(pos, block.defaultBlockState());
                this.fizz(level, pos);
                cir.setReturnValue(false);
                return;
            }

            if (isSnow && level.getFluidState(blockPos).is(FluidTags.WATER)) {
                Block block = level.getFluidState(pos).isSource() ? Blocks.OBSIDIAN : BlockSchemas.WHITE_GRANITE.getBaseBlock();
                level.setBlockAndUpdate(pos, block.defaultBlockState());
                this.fizz(level, pos);
                cir.setReturnValue(false);
                return;
            }
        }
    }
}
