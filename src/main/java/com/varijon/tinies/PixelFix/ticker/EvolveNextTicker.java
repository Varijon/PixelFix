package com.varijon.tinies.PixelFix.ticker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class EvolveNextTicker 
{
	static HashMap<Pokemon,Integer> lstPokemonToEvolve = new HashMap<Pokemon,Integer>();
	static int waitTicks = 0;
	
	@SubscribeEvent
	public void onWorldTick (TickEvent.ServerTickEvent event)
	{
		try
		{
			if(event.phase != TickEvent.Phase.END)
			{
				return;
			}
			for (Map.Entry<Pokemon,Integer> set : lstPokemonToEvolve.entrySet()) 
			{
				set.setValue(set.getValue() - 1);
				if(set.getValue() == 0)
				{
		            set.getKey().tryEvolution();
				}
	        }
			lstPokemonToEvolve.entrySet().removeIf(e -> e.getValue() == 0);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static void addPokemon(Pokemon pokemon, int ticks)
	{
		lstPokemonToEvolve.put(pokemon, ticks);
	}
}
