package com.varijon.tinies.mixin;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.varijon.tinies.PixelFix.ticker.EvolveNextTicker;

@Mixin(BattleController.class)
public class MixinBattleController 
{
	@Shadow
	public Set<Pokemon> checkedPokemon = Sets.newHashSet();
	
	@Overwrite(remap=false)
	private void checkEvolution() {
        for (Pokemon pokemon : this.checkedPokemon) {
        	EvolveNextTicker.setWaitTicks(1);
            EvolveNextTicker.addPokemon(pokemon);
        }
    }
}
