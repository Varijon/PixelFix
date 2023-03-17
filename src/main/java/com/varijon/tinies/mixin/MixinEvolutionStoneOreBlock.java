package com.varijon.tinies.mixin;

import java.util.Iterator;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.pixelmonmod.pixelmon.api.world.WorldTime;
import com.pixelmonmod.pixelmon.blocks.EvolutionStoneOreBlock;
import com.pixelmonmod.pixelmon.enums.EnumEvolutionStone;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;

@Mixin(EvolutionStoneOreBlock.class)
public class MixinEvolutionStoneOreBlock extends Block
{
	public MixinEvolutionStoneOreBlock(Properties p_i48440_1_) {
		super(p_i48440_1_);
		// TODO Auto-generated constructor stub
	}

	@Shadow
	private EnumEvolutionStone type;

	@Overwrite(remap=false)
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> drops = super.getDrops(state, builder);

		//Do nothing instead

		return drops;
	}
}
