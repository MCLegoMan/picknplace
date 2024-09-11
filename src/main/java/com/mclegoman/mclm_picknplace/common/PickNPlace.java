/*
    Pick N' Mix
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/picknmix
    Licence: GNU LGPLv3
*/

package com.mclegoman.mclm_picknplace.common;

import com.mclegoman.mclm_picknplace.common.blocks.Blocks;
import net.fabricmc.api.ModInitializer;

public class PickNPlace implements ModInitializer {
	@Override
	public void onInitialize() {
		Blocks.init();
	}
}
