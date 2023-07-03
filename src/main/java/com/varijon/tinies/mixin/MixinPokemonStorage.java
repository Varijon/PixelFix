package com.varijon.tinies.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;

@Mixin(PokemonStorage.class)
public class MixinPokemonStorage 
{
	public void retrieveAll() 
	{
		((PokemonStorage) (Object) this).retrieveAll("retrieve");
	}
}
