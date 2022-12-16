package com.varijon.tinies.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.pixelmonmod.pixelmon.api.battles.AttackCategory;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.IceFace;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Hail;
import com.pixelmonmod.pixelmon.battles.status.Weather;

@Mixin(IceFace.class)
public class MixinIceFace extends AbstractAbility
{
	@Shadow 
	boolean busted;
	
	@Overwrite(remap=false)
    public int modifyDamageIncludeFixed(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
        if (!target.getSpecies().is(PixelmonSpecies.EISCUE)) {
            return damage;
        }
        if (target.getForm().getName().contains("ice_face") && a.getAttackCategory() == AttackCategory.PHYSICAL && !this.busted) {
            target.bc.sendToAll("pixelmon.abilities.iceface.protected", target.getNickname());
            if (!target.bc.simulateMode) {
                this.busted = true;
            }
            return 0;
        }
        return damage;
    }

	@Overwrite(remap=false)
    public void applySwitchInEffect(PixelmonWrapper newPokemon) {
        if (!newPokemon.getSpecies().is(PixelmonSpecies.EISCUE)) {
            return;
        }
        if (newPokemon.getForm().getName().contains("noice_face") && newPokemon.bc.globalStatusController.getWeather() instanceof Hail && !newPokemon.bc.simulateMode) {
            newPokemon.bc.sendToAll("pixelmon.abilities.iceface.restore", newPokemon.getNickname());
            newPokemon.setForm(newPokemon.getForm().getName().replace("noice_face", "ice_face"));
            this.busted = false;
        }
    }
	
	@Overwrite(remap=false)
    public void applyRepeatedEffect(PixelmonWrapper pokemon) {
        if (!pokemon.getSpecies().is(PixelmonSpecies.EISCUE)) {
            return;
        }
        if (this.busted) {
            if (pokemon.getForm().getName().contains("ice_face")) {
                pokemon.setForm(pokemon.getForm().getName().replace("ice_face", "noice_face"));
            }
        }
    }
	
	@Overwrite(remap=false)
    public void onWeatherChange(PixelmonWrapper pw, Weather weather) {
        if (!pw.getSpecies().is(PixelmonSpecies.EISCUE)) {
            return;
        }
        if (pw.getForm().getName().contains("noice_face") && weather instanceof Hail && !pw.bc.simulateMode) {
            pw.bc.sendToAll("pixelmon.abilities.iceface.restore", pw.getNickname());
            pw.setForm(pw.getForm().getName().replace("noice_face", "ice_face"));
            this.busted = false;
        }
    }
}
