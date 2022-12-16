package com.varijon.tinies.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.pixelmonmod.pixelmon.PixelmonEventHandler;
import com.pixelmonmod.pixelmon.api.events.PokemonSendOutEvent;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.comm.ChatHandler;

import net.minecraft.entity.Entity;

@Mixin(PixelmonEventHandler.class)
public class MixinPixelmonEventHandler
{
	@Overwrite(remap=false)
	public void onSentOut(PokemonSendOutEvent event) {
        if (event.getPokemon().getSpecies().is(PixelmonSpecies.SHAYMIN)) {
            if (event.getPokemon().getForm().getName().contains("sky") && !event.getPlayer().getLevel().isDay()) {
                event.getPokemon().setForm(event.getPokemon().getForm().getName().replace("sky", "land"));
                ChatHandler.sendChat((Entity)event.getPlayer(), "pixelmon.abilities.changeform", event.getPokemon().getDisplayName());
            }
        } else if (event.getPokemon().getSpecies().is(PixelmonSpecies.CHERRIM)) {
            if (event.getPokemon().getForm().getName().contains("sunshine")) {
                event.getPokemon().setForm(event.getPokemon().getForm().getName().replace("sunshine", "overcast"));
            }
        } else if (!event.getPokemon().getSpecies().is(PixelmonSpecies.TOXTRICITY)) {
            if (event.getPokemon().getSpecies().is(PixelmonSpecies.HOOPA)) {
                if (event.getPokemon().getForm().getName().contains("unbound") && System.currentTimeMillis() > event.getPokemon().getPersistentData().getLong("unboundTime") + 3600000L) {
                    event.getPokemon().setForm(event.getPokemon().getForm().getName().replace("unbound", "confined"));
                    event.getPokemon().getPersistentData().remove("unboundTime");
                    ChatHandler.sendChat((Entity)event.getPlayer(), "pixelmon.abilities.changeform", event.getPokemon().getDisplayName());
                }
            }
        }
    }
	
}
