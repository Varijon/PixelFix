package com.varijon.tinies.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Freeze;
import com.pixelmonmod.pixelmon.battles.status.Sunny;
import com.pixelmonmod.pixelmon.comm.ChatHandler;

import net.minecraft.util.text.TranslationTextComponent;

@Mixin(Freeze.class)
public class MixinFreeze
{
	@Overwrite(remap=false)
	public static boolean freeze(PixelmonWrapper user, PixelmonWrapper target) {
        if (target.hasType(Element.ICE)) {
            return false;
        }
        if (target.bc.globalStatusController.getWeather() instanceof Sunny) {
            return false;
        }
        TranslationTextComponent message = ChatHandler.getMessage("pixelmon.effect.frozesolid", target.getNickname());
        if (target.getSpecies().is(PixelmonSpecies.SHAYMIN)) {
            if (target.entity.getPokemon().getForm().getName().contains("sky")) {
                target.entity.setForm(target.getForm().getName().replace("sky", "land"));
                user.bc.sendToAll("pixelmon.abilities.changeform", target.getNickname());
            }
        }
        return target.addStatus(new Freeze(), user, message);
    }
	
}
