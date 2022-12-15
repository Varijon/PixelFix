package com.varijon.tinies.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.pixelmonmod.pixelmon.api.battles.attack.AttackRegistry;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.Disguise;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

@Mixin(Disguise.class)
public class MixinDisguise extends AbstractAbility
{
	@Overwrite(remap=false)
	public int modifyDamageIncludeFixed(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
        if (!target.getForm().getName().contains("busted") && !a.hasNoEffect(user, target)) {
            if (!a.isAttack(AttackRegistry.MOONGEIST_BEAM, AttackRegistry.SUNSTEEL_STRIKE, AttackRegistry.PHOTON_GEYSER, AttackRegistry.SEARING_SUNRAZE_SMASH, AttackRegistry.MENACING_MOONRAZE_MAELSTROM, AttackRegistry.LIGHT_THAT_BURNS_THE_SKY)) {
                target.bc.sendToAll("pixelmon.abilities.disguise", new Object[0]);
                target.setForm(target.getForm().getName().replace("disguised", "busted"));
                target.bc.sendToAll("pixelmon.abilities.disguisebusted", target.getPokemonName());
                return target.getMaxHealth() / 8;
            }
        }
        return damage;
    }
	
}
