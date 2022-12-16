package com.varijon.tinies.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.pokemon.ItemFormChangeEvent;
import com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.ShieldsDown;
import com.pixelmonmod.pixelmon.api.util.helpers.NetworkHelper;
import com.pixelmonmod.pixelmon.client.particle.ParticleSystems;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.PlayParticleSystemPacket;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.formChanges.HoopaFormChange;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

@Mixin(HoopaFormChange.class)
public class MixinHoopaFormChange
{
	@Overwrite(remap=false)
	public boolean execute(PixelmonEntity pixelmon, ItemStack stack, ServerPlayerEntity player) {
        if (!pixelmon.getPokemon().getForm().getName().contains("unbound")) {
            ItemFormChangeEvent event = new ItemFormChangeEvent(player, stack, pixelmon, null);
            if (Pixelmon.EVENT_BUS.post((Event)event)) {
                return false;
            }
            NetworkHelper.sendToDimension(new PlayParticleSystemPacket(ParticleSystems.SLINGRING, pixelmon.getX(), pixelmon.getY(), pixelmon.getZ(), pixelmon.level, pixelmon.getPixelmonScale(), pixelmon.getPokemon().isPalette("shiny"), new double[0]), pixelmon.level);
            pixelmon.setForm(pixelmon.getForm().getName().replace("confined", "unbound"));
            pixelmon.getPokemon().getPersistentData().putLong("unboundTime", System.currentTimeMillis());
            ChatHandler.sendChat((Entity)player, "pixelmon.abilities.changeform", pixelmon.getNickname());
            return true;
        }
        return false;
    }
}
