package com.varijon.tinies.PixelFix.handler;

import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.spawning.LegendarySpawner;
import com.pixelmonmod.pixelmon.spawning.PlayerTrackingSpawner;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LegendPersistenceHandler 
{

	@SubscribeEvent
	public void onPixelmonSpawn(SpawnEvent event)
	{	
		if(event.action.getOrCreateEntity() instanceof PixelmonEntity)
		{
			PixelmonEntity pixelmon = (PixelmonEntity) event.action.getOrCreateEntity();
			if(!pixelmon.hasOwner() && (PixelmonSpecies.getLegendaries().contains(pixelmon.getSpecies().getDex()) || (PixelmonSpecies.getUltraBeasts().contains(pixelmon.getSpecies().getDex()))))
			{
				if(pixelmon.isBossPokemon())
				{
					return;
				}
				applyDespawnTag(pixelmon);
			}
		}
	}
	
	@SubscribeEvent
	public void onCatch(CaptureEvent.SuccessfulCapture event)
	{	
		event.getPokemon().getPokemon().getPersistentData().remove("despawnTime");
	}	
	

	void applyDespawnTag(PixelmonEntity pixelmon)
	{
		long despawnTime = System.currentTimeMillis() + 3600000l;
		pixelmon.setPersistenceRequired();
		pixelmon.getPokemon().getPersistentData().putLong("despawnTime", despawnTime);
	}
}
