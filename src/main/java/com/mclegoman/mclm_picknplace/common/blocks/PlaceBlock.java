package com.mclegoman.mclm_picknplace.common.blocks;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class PlaceBlock extends PickPlaceBlock {
	public PlaceBlock(AbstractBlock.Settings settings) {
		super(settings);
	}
	protected TickPriority tickPriority() {
		return TickPriority.EXTREMELY_LOW;
	}
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		Direction oppositeDirection = state.get(direction).getOpposite();
		BlockPos blockPos = pos.offset(oppositeDirection);
		BlockPos blockPos2 = pos.offset(state.get(direction));
		if (world.getBlockState(blockPos2).isAir()) {
			method_50878(world, blockPos, oppositeDirection, (itemStack) -> {
				if (itemStack.isEmpty()) {
					return false;
				} else {
					boolean var10000;
					label19: {
						Item var6 = itemStack.getItem();
						if (var6 instanceof BlockItem blockItem) {
							if (blockItem.place(new PlaceContext(world, Hand.MAIN_HAND, itemStack, new BlockHitResult(blockPos2.toCenterPos(), oppositeDirection, blockPos2, false))).isAccepted()) {
								var10000 = true;
								break label19;
							}
						}

						var10000 = false;
					}

					boolean bl = var10000;
					if (!bl) {
						double d = (double) EntityType.ITEM.getHeight() / 2.0;
						double e = (double)blockPos2.getX() + 0.5;
						double f = (double)blockPos2.getY() + 0.5 - d;
						double g = (double)blockPos2.getZ() + 0.5;
						ItemEntity itemEntity = new ItemEntity(world, e, f, g, itemStack);
						itemEntity.setToDefaultPickupDelay();
						world.spawnEntity(itemEntity);
					}

					return true;
				}
			});
		} else {
			world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.BLOCKS);
		}
	}

	public static void method_50878(World world, BlockPos blockPos, Direction direction, Function<ItemStack, Boolean> function) {
		List<Inventory> list = method_50877(world, blockPos);
		Iterator<Inventory> var5 = list.iterator();

		boolean bl;
		do {
			if (!var5.hasNext()) {
				ItemEntity itemEntity = method_50880(world, blockPos);
				if (itemEntity != null) {
					ItemStack itemStack = itemEntity.getStack();
					if (!itemStack.isEmpty()) {
						bl = function.apply(itemStack.copyWithCount(1));
						if (bl) {
							itemStack.decrement(1);
							if (itemStack.getCount() <= 0) {
								itemEntity.discard();
							}
						}

						return;
					}
				}

				return;
			}

			Inventory inventory = var5.next();
			bl = Arrays.stream(HopperBlockEntity.getAvailableSlots(inventory, direction)).anyMatch((i) -> {
				ItemStack itemStack = inventory.removeStack(i, 1);
				if (!itemStack.isEmpty()) {
					if (function.apply(itemStack.copy())) {
						inventory.markDirty();
					} else {
						inventory.setStack(i, itemStack);
					}
					return true;
				} else {
					return false;
				}
			});
		} while(!bl);

	}

	public static List<Inventory> method_50877(World world, BlockPos blockPos) {
		BlockState blockState = world.getBlockState(blockPos);
		Block block = blockState.getBlock();
		if (block instanceof InventoryProvider) {
			SidedInventory sidedInventory = ((InventoryProvider)block).getInventory(blockState, world, blockPos);
			if (sidedInventory != null) {
				return List.of(sidedInventory);
			}
		} else if (blockState.hasBlockEntity()) {
			BlockEntity blockEntity = world.getBlockEntity(blockPos);
			if (blockEntity instanceof Inventory) {
				if (!(blockEntity instanceof ChestBlockEntity) || !(block instanceof ChestBlock)) {
					return List.of((Inventory)blockEntity);
				}

				Inventory inventory = ChestBlock.getInventory((ChestBlock)block, blockState, world, blockPos, true);
				if (inventory != null) {
					return List.of(inventory);
				}
			}
		}

		List<Inventory> list = new ArrayList<>();
		List<Entity> list2 = world.getOtherEntities(null, method_50879(blockPos), EntityPredicates.VALID_INVENTORIES);

		for (Entity entity : list2) {
			if (entity instanceof Inventory inventory2) {
				list.add(inventory2);
			}
		}

		return list;
	}

	@Nullable
	public static ItemEntity method_50880(World world, BlockPos blockPos) {
		List<ItemEntity> list = world.getEntitiesByClass(ItemEntity.class, method_50879(blockPos), EntityPredicates.VALID_ENTITY);
		return list.isEmpty() ? null : list.getFirst();
	}

	private static Box method_50879(BlockPos blockPos) {
		return Box.of(blockPos.toCenterPos(), 0.9999999, 0.9999999, 0.9999999);
	}
}