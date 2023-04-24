package com.varijon.tinies.PixelFix.handler;

import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SuffocationPreventionHandler 
{

	@SubscribeEvent
	public void onSuffocate(LivingAttackEvent event)
	{
		if(event.getSource() != DamageSource.IN_WALL)
		{
			return;
		}
		if(event.getEntityLiving() instanceof PixelmonEntity)
		{
			event.setCanceled(true);
		}
	}
}
