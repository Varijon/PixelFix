package com.varijon.tinies.PixelFix;

import org.apache.logging.log4j.Logger;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.varijon.tinies.PixelFix.ticker.EvolveNextTicker;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("pixelfix")
public class PixelFix 
{
	public static String MODID = "modid";
	public static String VERSION = "version";
	public static Logger logger;
	
	public PixelFix()
	{
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
	}
	
	@SubscribeEvent
    public void setup(FMLCommonSetupEvent event) 
    {
		MinecraftForge.EVENT_BUS.register(new EvolveNextTicker());
	}
	
	@SubscribeEvent
	public void serverStarting(FMLServerStartingEvent event)
	{
	}

	@SubscribeEvent
	public void registerCommands(RegisterCommandsEvent event)
	{
	}
}
