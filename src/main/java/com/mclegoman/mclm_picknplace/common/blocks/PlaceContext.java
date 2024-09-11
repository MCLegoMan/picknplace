package com.mclegoman.mclm_picknplace.common.blocks;

import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PlaceContext extends ItemPlacementContext {
	public PlaceContext(World world, Hand hand, ItemStack itemStack, BlockHitResult blockHitResult) {
		super(world, null, hand, itemStack, blockHitResult);
		this.canReplaceExisting = world.getBlockState(blockHitResult.getBlockPos()).canReplace(this);
	}

	public static PlaceContext method_50806(World world, BlockPos blockPos, Direction direction, ItemStack itemStack) {
		return new PlaceContext(world, Hand.MAIN_HAND, itemStack, new BlockHitResult(new Vec3d((double)blockPos.getX() + 0.5 + (double)direction.getOffsetX() * 0.5, (double)blockPos.getY() + 0.5 + (double)direction.getOffsetY() * 0.5, (double)blockPos.getZ() + 0.5 + (double)direction.getOffsetZ() * 0.5), direction, blockPos, false));
	}

	public Direction getPlayerLookDirection() {
		return this.getHitResult().getSide();
	}

	public Direction getVerticalPlayerLookDirection() {
		return this.getHitResult().getSide() == Direction.UP ? Direction.UP : Direction.DOWN;
	}

	public Direction[] getPlacementDirections() {
		Direction direction = this.getHitResult().getSide();
		Direction[] directions = new Direction[]{direction, null, null, null, null, direction.getOpposite()};
		int i = 0;
		Direction[] var4 = Direction.values();
		int var5 = var4.length;

		for(int var6 = 0; var6 < var5; ++var6) {
			Direction direction2 = var4[var6];
			if (direction2 != direction && direction2 != direction.getOpposite()) {
				++i;
				directions[i] = direction;
			}
		}

		return directions;
	}
}

