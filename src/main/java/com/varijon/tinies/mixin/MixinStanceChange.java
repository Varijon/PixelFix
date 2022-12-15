package com.varijon.tinies.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.pixelmonmod.pixelmon.api.battles.attack.AttackRegistry;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.Disguise;
import com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.StanceChange;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

@Mixin(StanceChange.class)
public class MixinStanceChange extends AbstractAbility
{
	@Overwrite(remap=false)
    public void preProcessAttackUser(PixelmonWrapper pokemon, PixelmonWrapper target, Attack a) {
        if (pokemon.getSpecies().is(PixelmonSpecies.AEGISLASH) && Attack.dealsDamage(a)) {
            if (pokemon.getForm().getName().contains("shield")) {
                pokemon.setForm(pokemon.getForm().getName().replace("shield", "blade"));
                pokemon.bc.modifyStats(pokemon);
                pokemon.bc.sendToAll("pixelmon.abilities.stancechange.blade", pokemon.getNickname());
            }
        }
    }

	@Overwrite(remap=false)
    public void applySwitchOutEffect(PixelmonWrapper oldPokemon) {
        if (oldPokemon.getSpecies().is(PixelmonSpecies.AEGISLASH)) {
            if (oldPokemon.getForm().getName().contains("blade")) {
                oldPokemon.setForm(oldPokemon.getForm().getName().replace("blade", "shield"));
            }
        }
    }
	
}
