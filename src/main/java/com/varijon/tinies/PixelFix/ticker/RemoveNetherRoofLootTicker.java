package com.varijon.tinies.PixelFix.ticker;

import java.util.ArrayList;

import com.pixelmonmod.pixelmon.blocks.tileentity.PokeChestTileEntity;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RemoveNetherRoofLootTicker 
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
			MinecraftServer server = event.world.getServer();
			ServerWorld world = (ServerWorld) event.world;
			if(world.dimension() != world.NETHER)
			{
				return;
			}
			if(server.getTickCount() % 20 != 0)
			{
				return;
			}
			ArrayList<TileEntity> lstRemove = new ArrayList<TileEntity>();
			for(TileEntity tileEntity : world.blockEntityList)
			{
				if(tileEntity instanceof PokeChestTileEntity)
				{
					if(tileEntity.getBlockPos().getY() > 127)
					{
						lstRemove.add(tileEntity);					
					}
				}
			}
			for(TileEntity tileEntity : lstRemove)
			{
				world.removeBlock(tileEntity.getBlockPos(), false);
				world.removeBlockEntity(tileEntity.getBlockPos());
			}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
