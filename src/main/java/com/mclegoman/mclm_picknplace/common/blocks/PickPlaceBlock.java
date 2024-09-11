package com.mclegoman.mclm_picknplace.common.blocks;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;

public abstract class PickPlaceBlock extends Block {
	public static final DirectionProperty direction;
	public static final BooleanProperty triggered;
	public PickPlaceBlock(AbstractBlock.Settings settings) {
		super(settings);
		this.setDefaultState(((this.stateManager.getDefaultState()).with(direction, Direction.NORTH)).with(triggered, false));
	}

	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
		boolean bl = world.isReceivingRedstonePower(pos);
		boolean bl2 = state.get(triggered);
		if (bl && !bl2) {
			world.scheduleBlockTick(pos, this, 1, this.tickPriority());
			world.setBlockState(pos, state.with(triggered, true), 2);
		} else if (!bl && bl2) {
			world.setBlockState(pos, state.with(triggered, false), 2);
		}

	}

	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(direction, ctx.getPlayerLookDirection().getOpposite());
	}

	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(direction, rotation.rotate(state.get(direction)));
	}

	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(direction)));
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(direction, triggered);
	}

	protected abstract TickPriority tickPriority();
	static {
		direction = FacingBlock.FACING;
		triggered = Properties.TRIGGERED;
	}
}
