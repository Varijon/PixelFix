package com.varijon.tinies.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.pixelmonmod.pixelmon.api.battles.attack.AttackRegistry;
import com.pixelmonmod.pixelmon.api.pokemon.stats.Moveset;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.api.util.helpers.NetworkHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.UpdateMovesetPacket;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.HeldItem;
import com.pixelmonmod.pixelmon.items.heldItems.RustedShieldItem;

import net.minecraft.entity.player.ServerPlayerEntity;

@Mixin(RustedShieldItem.class)
public class MixinRustedShieldItem extends HeldItem
{

	public MixinRustedShieldItem(EnumHeldItems heldItemType, Properties properties) {
		super(heldItemType, properties);
		// TODO Auto-generated constructor stub
	}

	@Overwrite(remap=false)
	public void onStartOfBattle(PixelmonWrapper pw) {
        if (pw.getSpecies().is(PixelmonSpecies.ZAMAZENTA)) {
            pw.setForm(pw.getForm().getName().replace("hero", "crowned"));
            Moveset moveset = pw.getMoveset();
            for (Attack attack : moveset) {
                if (!attack.isAttack(AttackRegistry.IRON_HEAD)) continue;
                Attack newAttack = new Attack(AttackRegistry.BEHEMOTH_BASH);
                newAttack.pp = (int)Math.ceil((double)attack.pp / 3.0);
                moveset.replaceMove(AttackRegistry.IRON_HEAD, newAttack);
                if (!(pw.getParticipant() instanceof PlayerParticipant)) continue;
                ServerPlayerEntity player = ((PlayerParticipant)pw.getParticipant()).player;
                NetworkHelper.sendPacket(new UpdateMovesetPacket(pw), player);
            }
            pw.type = pw.getSpecies().getForm("crowned").getTypes();
        }
    }
	
}
