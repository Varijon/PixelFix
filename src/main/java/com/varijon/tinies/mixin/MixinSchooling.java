package com.varijon.tinies.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.Schooling;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

@Mixin(Schooling.class)
public class MixinSchooling extends AbstractAbility
{
	@Overwrite(remap=false)
	private void tryFormChange(PixelmonWrapper pw) {
        if (pw.getSpecies().is(PixelmonSpecies.WISHIWASHI) && pw.getPokemonLevelNum() >= 20) {
            if (pw.getForm().getName().contains("school")) {
                if (pw.getHealthPercent() < 25.0f) {
                    pw.setForm(pw.getForm().getName().replace("school", "solo"));
                    pw.bc.sendToAll("pixelmon.abilities.schooling.stop", pw.getNickname());
                }
            } else if (pw.getForm().getName().contains("solo") && pw.getHealthPercent() > 25.0f) {
                pw.setForm(pw.getForm().getName().replace("solo", "school"));
                pw.bc.sendToAll("pixelmon.abilities.schooling.start", pw.getNickname());
            }
        }
    }
	
}
