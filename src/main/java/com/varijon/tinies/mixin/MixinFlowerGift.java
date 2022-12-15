package com.varijon.tinies.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.pixelmonmod.pixelmon.api.battles.attack.AttackRegistry;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.Disguise;
import com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.FlowerGift;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.Weather;

@Mixin(FlowerGift.class)
public class MixinFlowerGift extends AbstractAbility
{
	@Overwrite(remap=false)
    public void onWeatherChange(PixelmonWrapper pw, Weather weather) {
        StatusType weatherType;
        if (!pw.getSpecies().is(PixelmonSpecies.CHERRIM) || pw.bc.simulateMode) {
            return;
        }
        StatusType statusType = weatherType = weather == null ? StatusType.None : weather.type;
        if (weatherType == StatusType.Sunny) {
            if (pw.getForm().getName().contains("overcast")) {
                pw.setForm(pw.getForm().getName().replace("overcast", "sunshine"));
                pw.bc.sendToAll("pixelmon.abilities.changeform", pw.getNickname());
            }
        } else if (pw.getForm().getName().contains("sunshine")) {
            pw.setForm(pw.getForm().getName().replace("sunshine", "overcast"));
            pw.bc.sendToAll("pixelmon.abilities.changeform", pw.getNickname());
        }
    }

	@Overwrite(remap=false)
    private void resetForm(PixelmonWrapper pw) {
        if (pw.bc.simulateMode) {
            return;
        }
        if (!pw.getForm().getName().contains("overcast")) {
            pw.setForm(pw.getForm().getName().replace("sunshine", "overcast"));
        }
    }
}
