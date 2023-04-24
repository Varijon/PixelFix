package com.varijon.tinies.mixin;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommandUtils;
import com.pixelmonmod.pixelmon.api.pokedex.PlayerPokedex;
import com.pixelmonmod.pixelmon.api.pokemon.species.Pokedex;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.api.util.helpers.NumberHelper;
import com.pixelmonmod.pixelmon.command.PixelCommand;
import com.pixelmonmod.pixelmon.command.impl.DexCheckCommand;
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning;

import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

@Mixin(DexCheckCommand.class)
public class MixinDexCheckCommand extends PixelCommand
{
	public MixinDexCheckCommand(CommandDispatcher<CommandSource> dispatcher) {
		super(dispatcher);
		// TODO Auto-generated constructor stub
	}

	@Overwrite(remap=false)
	@Override
    public void execute(CommandSource sender, String[] args) throws CommandException 
	{
        ServerPlayerEntity player = PixelmonCommandUtils.requireEntityPlayer(sender);
        PlayerPokedex dex = PixelmonCommandUtils.require(PixelmonCommandUtils.getPlayerStorage((ServerPlayerEntity)player), (String)"pixelmon.command.general.invalidplayer", (Object[])new Object[0]).playerPokedex;
		if(args.length > 0)
		{
			if(args[0].equals("r") || args[0].equals("remaining"))
			{
				ArrayList<Species> lstReturnEntries = Lists.newArrayList();
				for(Species species : PixelmonSpecies.getAll())
				{
					if(!dex.hasCaught(species) && !species.is(PixelmonSpecies.MISSINGNO))
					{
						lstReturnEntries.add(species);
					}
				}
				int page = 1;
				if(args.length > 1)
				{
					page = PixelmonCommandUtils.requireInt(args[1], TextFormatting.RED + "Invalid page number!");
				}
				StringBuilder sb = new StringBuilder();
				lstReturnEntries.sort(Comparator.comparing(Species::getDex));
				for(int x = 0; x < lstReturnEntries.size(); x++)
				{
					if(x >= (page-1) * 20 && x < page * 20)
					{
						sb.append(lstReturnEntries.get(x).getLocalizedName());
						if(x + 1 < page * 20 && x + 1 < lstReturnEntries.size())
						{
							sb.append(", ");
						}
					}
				}
				sender.sendSuccess(new StringTextComponent(TextFormatting.DARK_AQUA + "Remaining Pokemon: " + TextFormatting.GRAY + "Page: " + TextFormatting.GOLD + page + TextFormatting.GRAY + " of " + TextFormatting.GOLD +((int) Math.ceil((double)lstReturnEntries.size() / 20.0))), false);
				sender.sendSuccess(new StringTextComponent(sb.toString()), false);
			}
		}
		else
		{
	        int caught = dex.countCaught();
	        BigDecimal percent = BigDecimal.valueOf(caught).divide(BigDecimal.valueOf(Pokedex.pokedexSize), new MathContext(5, RoundingMode.HALF_EVEN)).multiply(BigDecimal.valueOf(100L));
	        String percentString = NumberHelper.formatPercentage(percent.doubleValue());
	        sender.sendSuccess((ITextComponent)PixelmonCommandUtils.format(TextFormatting.GREEN, "pixelmon.command.dexcheck", "" + (Object)TextFormatting.DARK_AQUA + caught + (Object)TextFormatting.GOLD, "" + (Object)TextFormatting.DARK_AQUA + Pokedex.pokedexSize + (Object)TextFormatting.GOLD, "" + (Object)TextFormatting.DARK_AQUA + percentString + (Object)TextFormatting.GOLD), false);
		}
		
    }
	
    @Override
    public List<String> getTabCompletions(MinecraftServer server, CommandSource sender, String[] args, BlockPos pos) 
    {
        ArrayList<String> list = Lists.newArrayList();
        list.add("r");
        list.add("remaining");
        return args.length == 1 ? list : Collections.emptyList();
    }
}
