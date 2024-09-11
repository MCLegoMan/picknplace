package com.mclegoman.mclm_picknplace.common.blocks;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Blocks {
	public static final Block pickaxeBlock;
	public static final Block placeBlock;
	public static void init() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(itemGroup -> {
			itemGroup.addAfter(Items.CRAFTER, pickaxeBlock);
			itemGroup.addAfter(pickaxeBlock, placeBlock);
		});
	}
	public static Block register(Block block, String key, boolean shouldRegisterItem) {
		Identifier id = Identifier.of("mclm_picknplace", key);
		if (shouldRegisterItem) {
			BlockItem blockItem = new BlockItem(block, new Item.Settings());
			Registry.register(Registries.ITEM, id, blockItem);
		}
		return Registry.register(Registries.BLOCK, id, block);
	}
	static {
		pickaxeBlock = register(new PickaxeBlock(AbstractBlock.Settings.create().strength(3.0F).requiresTool().solidBlock((blockState, blockView, blockPos) -> true)), "pickaxe_block", true);
		placeBlock = register(new PlaceBlock(AbstractBlock.Settings.create().strength(3.0F).requiresTool().solidBlock((blockState, blockView, blockPos) -> true)), "place_block", true);
	}
}
