package com.mclegoman.mclm_picknplace.common.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.tick.TickPriority;

public class PickaxeBlock extends PickPlaceBlock {
	public PickaxeBlock(AbstractBlock.Settings settings) {
		super(settings);
	}
	protected TickPriority tickPriority() {
		return TickPriority.EXTREMELY_HIGH;
	}
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		BlockPos blockPos = pos.offset(state.get(direction));
		if (!world.getBlockState(blockPos).isAir() && !(world.getBlockState(blockPos).getBlock().getHardness() < 0.0F)) {
			world.breakBlock(blockPos, true);
		} else {
			world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.BLOCKS);
		}
	}
}
