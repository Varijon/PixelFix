package com.varijon.tinies.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.pixelmonmod.pixelmon.api.battles.attack.AttackRegistry;
import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.api.util.helpers.ResourceLocationHelper;
import com.pixelmonmod.pixelmon.entities.SpawnLocationType;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills.GroundBirds;
import com.pixelmonmod.pixelmon.tools.LineCalc;

import net.minecraft.entity.Entity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;

@Mixin(GroundBirds.class)
public class MixinGroundBirds 
{
	@Shadow
    public static boolean affectOwnedPokemon = false;
	@Shadow
    public static final float maxRange = 300.0f;
    
	@Overwrite(remap=false)
	public static MoveSkill createMoveSkill() {
        MoveSkill moveSkill = new MoveSkill("ground").setName("pixelmon.moveskill.ground").describe("pixelmon.moveskill.ground.description1", "pixelmon.moveskill.ground.description2").setAnyMoves("Gravity", "Smack Down").setUsePP(true).setIcon(ResourceLocationHelper.of("pixelmon", "textures/gui/overlay/externalmoves/groundbirds.png")).setDefaultCooldownTicks(900);
        moveSkill.setBehaviourNoTarget(usingPixelmon -> {
            usingPixelmon.level.getEntitiesOfClass(PixelmonEntity.class, AxisAlignedBB.ofSize((double)600.0, (double)600.0, (double)600.0).expandTowards(usingPixelmon.position())).stream().filter(pixelmon -> {
                if (!affectOwnedPokemon && pixelmon.hasOwner()) {
                    return false;
                }
                if (pixelmon.getSpawnLocation() != SpawnLocationType.AIR && pixelmon.getSpawnLocation() != SpawnLocationType.AIR_PERSISTENT && pixelmon.getFlyingParameters() == null) {
                    return false;
                }
                if (pixelmon == usingPixelmon) {
                    return false;
                }
                Pokemon pokemon = pixelmon.getPokemon();
                float specialAttack = pokemon.getStat(BattleStatsType.SPECIAL_ATTACK);
                float attack = pokemon.getStat(BattleStatsType.ATTACK);
                float stat = pokemon.getMoveset().hasAttack(AttackRegistry.GRAVITY) ? (pokemon.getMoveset().hasAttack(AttackRegistry.SMACK_DOWN) ? Math.max(attack, specialAttack) : specialAttack) : attack;
                return !(pixelmon.distanceTo((Entity)usingPixelmon) > LineCalc.lerp(stat, 1.0f, 300.0f, 20.0f, 100.0f));
            }).forEach(pixelmon -> {
                pixelmon.setSpawnLocation(SpawnLocationType.LAND);
                pixelmon.grounded = true;
                pixelmon.resetAI();
            });
            usingPixelmon.playSound(SoundEvents.ENDER_DRAGON_FLAP, 1.0f, 2.0f);
            return moveSkill.cooldownTicks;
        });
        return moveSkill;
    }

}
