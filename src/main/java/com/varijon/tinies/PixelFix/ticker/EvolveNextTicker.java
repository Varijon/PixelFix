package com.varijon.tinies.PixelFix.ticker;

import java.util.ArrayList;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class EvolveNextTicker 
{
	static ArrayList<Pokemon> lstPokemonToEvolve = new ArrayList<Pokemon>();
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
			MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
			if(server.getTickCount() % 20 != 0)
			{
				return;
			}
			if(waitTicks > 0)
			{
				waitTicks -= 1;
				return;
			}
			for (Pokemon pokemon : lstPokemonToEvolve) 
			{
	            pokemon.tryEvolution();
	        }
			lstPokemonToEvolve.clear();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static void addPokemon(Pokemon pokemon)
	{
		lstPokemonToEvolve.add(pokemon);
	}

	public static int getWaitTicks() {
		return waitTicks;
	}

	public static void setWaitTicks(int waitTicks) {
		EvolveNextTicker.waitTicks = waitTicks;
	}
	
	
}
