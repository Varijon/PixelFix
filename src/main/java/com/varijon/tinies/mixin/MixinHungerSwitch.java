package com.varijon.tinies.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.HungerSwitch;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

@Mixin(HungerSwitch.class)
public class MixinHungerSwitch extends AbstractAbility
{
	@Overwrite(remap=false)
    public void applyRepeatedEffect(PixelmonWrapper pokemon) {
        if (pokemon.getForm().getName().contains("fullbelly")) {
            pokemon.setForm(pokemon.getForm().getName().replace("fullbelly", "hangry"));
            pokemon.bc.sendToAll("pixelmon.abilities.hungerswitch.hangry", pokemon.getNickname());
        } else if (pokemon.getForm().getName().contains("hangry")) {
            pokemon.setForm(pokemon.getForm().getName().replace("hangry", "fullbelly"));
            pokemon.bc.sendToAll("pixelmon.abilities.hungerswitch.full", pokemon.getNickname());
        }
    }
	
}
