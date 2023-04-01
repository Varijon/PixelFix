package com.varijon.tinies.PixelFix.ticker;

import java.lang.reflect.Field;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.entities.pixelmon.StatueEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LegendPersistenceTicker 
{
	@SubscribeEvent
	public void onWorldTick (TickEvent.WorldTickEvent event)
	{
		try
		{
			if(event.phase != TickEvent.Phase.END)
			{
				return;
			}
			ServerWorld world = (ServerWorld) event.world;
			for(Entity entity : world.getAllEntities())
			{
				if(!(entity instanceof PixelmonEntity))
				{
					continue;
				}
				PixelmonEntity pixelmon = (PixelmonEntity) entity;
				if(pixelmon.hasOwner())
				{
					continue;
				}
				if(!PixelmonSpecies.getLegendaries().contains(pixelmon.getSpecies().getDex()) && !PixelmonSpecies.getUltraBeasts().contains(pixelmon.getSpecies().getDex()))
				{
					continue;
				}
				
				long despawnTime = getDespawnTime(pixelmon);
				if(despawnTime == -1)
				{
					continue;
				}
				if(System.currentTimeMillis() >= despawnTime)
				{
					if(pixelmon.battleController == null)
					{
						pixelmon.remove();
					}
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	

	long getDespawnTime(PixelmonEntity pixelmon)
	{
		CompoundNBT persistentData = pixelmon.getPokemon().getPersistentData();
		persistentData = pixelmon.getPokemon().getPersistentData();
		if(persistentData.isEmpty())
		{
			return -1;
		}
		if(!persistentData.contains("despawnTime"))
		{
			return -1;
		}
		return persistentData.getLong("despawnTime");
	}
}
