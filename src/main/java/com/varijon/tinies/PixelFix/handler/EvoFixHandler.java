package com.varijon.tinies.PixelFix.handler;

import java.util.ArrayList;
import java.util.UUID;

import com.pixelmonmod.pixelmon.api.registries.PixelmonItems;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.EvolutionQuery;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.EvolutionQueryList;
import com.pixelmonmod.pixelmon.items.HammerItem;
import com.pixelmonmod.pixelmon.items.PixelmonItem;
import com.pixelmonmod.pixelmon.items.group.PixelmonItemGroups;
import com.pixelmonmod.pixelmon.items.tools.GenericHammer;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class EvoFixHandler 
{
	@SubscribeEvent
	public void onHammerClick(PlayerInteractEvent.EntityInteract event)
	{	
		if(event.getHand() != Hand.MAIN_HAND)
		{
			return;
		}
		if(!(event.getTarget() instanceof PixelmonEntity))
		{
			return;
		}
		PixelmonEntity pixelmon = (PixelmonEntity) event.getTarget();
		if(!pixelmon.hasOwner())
		{
			return;
		}
		if(!pixelmon.getOwner().getUUID().equals(event.getPlayer().getUUID()))
		{
			return;
		}
		if(event.getItemStack() == null)
		{
			return;
		}
		if(event.getItemStack().getItem() == Items.AIR)
		{
			return;
		}

		ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
		if(event.getItemStack().getItem() instanceof HammerItem)
		{
			boolean removed = false;
			ArrayList<EvolutionQuery> lstRemove = new ArrayList<EvolutionQuery>();
			for(EvolutionQuery query : EvolutionQueryList.queryList)
			{
				if(query.pokemonUUID.equals(pixelmon.getUUID()))
				{
					lstRemove.add(query);
					removed = true;
				}
			}
			for(EvolutionQuery query : lstRemove)
			{
				EvolutionQueryList.queryList.remove(query);
			}
			if(removed)
			{
				ServerWorld world = (ServerWorld) event.getWorld();
				world.playSound(null, event.getPos(), SoundEvents.ANVIL_PLACE, SoundCategory.PLAYERS, 0.5f, 0.2f);
				world.sendParticles(ParticleTypes.CRIT, pixelmon.position().x, pixelmon.position().y, pixelmon.position().z, 10, pixelmon.getBoundingBox().getXsize()/2, pixelmon.getBoundingBox().getYsize()/2, pixelmon.getBoundingBox().getZsize()/2, 0);
				event.setCanceled(true);
				pixelmon.getPokemon().tryEvolution();				
			}
		}
	}
}
