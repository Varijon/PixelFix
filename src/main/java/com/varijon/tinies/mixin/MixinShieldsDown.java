package com.varijon.tinies.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.pixelmonmod.pixelmon.api.battles.attack.AttackRegistry;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.Disguise;
import com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.ShieldsDown;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;

@Mixin(ShieldsDown.class)
public class MixinShieldsDown extends AbstractAbility
{
	@Overwrite(remap=false)
    public boolean allowsStatus(StatusType status, PixelmonWrapper pw, PixelmonWrapper user) {
        if (status == StatusType.StealthRock) return true;
        if (status == StatusType.Spikes) return true;
        if (status == StatusType.ToxicSpikes) return true;
        if (status == StatusType.StickyWeb) return true;
        if (status == StatusType.Steelsurge) return true;
        if (status == StatusType.Substitute) {
            return true;
        }
        if (!pw.getSpecies().is(PixelmonSpecies.MINIOR)) return true;
        if (pw.getForm().getName().contains("meteor")) return false;
        return true;
    }
	
	@Overwrite(remap=false)
	private void tryFormChange(PixelmonWrapper pw) {
        if (pw.getSpecies().is(PixelmonSpecies.MINIOR)) {
            if (!pw.getForm().getName().contains("meteor")) {
                if (pw.getHealthPercent() > 50.0f) {
                    pw.setForm(pw.getForm().getName().replace("core", "meteor"));
                    pw.bc.sendToAll("pixelmon.abilities.changeform", pw.getNickname());
                }
            } else if (pw.getForm().getName().contains("meteor") && pw.getHealthPercent() < 50.0f) {
                pw.setForm(pw.getForm().getName().replace("meteor", "core"));
                pw.bc.sendToAll("pixelmon.abilities.changeform", pw.getNickname());
            }
        }
    }
}
