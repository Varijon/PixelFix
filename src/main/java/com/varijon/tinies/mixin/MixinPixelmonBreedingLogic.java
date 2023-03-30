package com.varijon.tinies.mixin;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.pixelmonmod.api.registry.RegistryValue;
import com.pixelmonmod.pixelmon.api.config.PixelmonConfigProxy;
import com.pixelmonmod.pixelmon.api.pokemon.Nature;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.ability.Ability;
import com.pixelmonmod.pixelmon.api.pokemon.egg.BreedingLogicFactory;
import com.pixelmonmod.pixelmon.api.pokemon.egg.impl.PixelmonBreedingLogic;
import com.pixelmonmod.pixelmon.api.pokemon.item.pokeball.PokeBall;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.pokemon.species.Stats;
import com.pixelmonmod.pixelmon.api.pokemon.stats.IVStore;
import com.pixelmonmod.pixelmon.api.pokemon.stats.Moveset;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.ImmutableAttack;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;

import net.minecraft.item.Item;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;

@Mixin(PixelmonBreedingLogic.class)
public class MixinPixelmonBreedingLogic implements BreedingLogicFactory 
{
	@Overwrite(remap=false)
    @Override
    public boolean shouldEggBeShiny(Pokemon parentOne, Pokemon parentTwo) {
        float differentTrainerFactor = 1.0f;
        if (!parentOne.getOriginalTrainerUUID().equals(parentTwo.getOriginalTrainerUUID())) {
            differentTrainerFactor = 2.0f;
        }
        if (StorageProxy.getParty(parentOne.getOwnerPlayerUUID()).getShinyCharm().isActive()) {
            differentTrainerFactor *= 2.0f;
        }
        return PixelmonConfigProxy.getSpawning().getShinyRate(World.OVERWORLD) != 0.0f && RandomHelper.getRandom().nextFloat() < differentTrainerFactor / PixelmonConfigProxy.getSpawning().getShinyRate(World.OVERWORLD);
    }
	
	@Shadow
	@Override
	public Stats calculateForm(Pokemon var1, Pokemon var2, Species var3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Shadow
	@Override
	public String calculatePalette(Pokemon var1, Pokemon var2, Species var3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Shadow
	@Override
	public Species calculateSpecies(Pokemon var1, Pokemon var2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Shadow
	@Override
	public boolean canBreed(Pokemon var1, Pokemon var2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Shadow
	@Override
	public boolean canLearnVoltTackle(Species var1, Pokemon var2, Pokemon var3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Shadow
	@Override
	public boolean checkIncense(Item var1, Item var2, Pokemon var3, Item var4, RegistryValue<Species> var5,
			RegistryValue<Species>... var6) {
		// TODO Auto-generated method stub
		return false;
	}

	@Shadow
	@Override
	public Pokemon findFather(Pokemon var1, Pokemon var2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Shadow
	@Override
	public Pokemon findMother(Pokemon var1, Pokemon var2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Shadow
	@Override
	public Ability getEggAbility(Stats var1, Pokemon var2, Pokemon var3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Shadow
	@Override
	public EnumGrowth getEggGrowth(Pokemon var1, Pokemon var2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Shadow
	@Override
	public List<ImmutableAttack> getEggMoves(Stats var1, Pokemon var2, Set<ImmutableAttack> var3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Shadow
	@Override
	public Moveset getEggMoveset(Stats var1, Pokemon var2, Pokemon var3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Shadow
	@Override
	public List<ImmutableAttack> getFathersTMHMTutorMoves(Stats var1, Pokemon var2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Shadow
	@Override
	public Moveset getFirstFourMoves(List<ImmutableAttack> var1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Shadow
	@Override
	public IVStore getIVsForEgg(Pokemon var1, Pokemon var2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Shadow
	@Override
	public Nature getNatureForEgg(Pokemon var1, Pokemon var2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Shadow
	@Override
	public PokeBall getPokeBall(Pokemon var1, Pokemon var2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Shadow
	@Override
	public List<ImmutableAttack> getPokemonLevelupMoves(Stats var1, Pokemon var2, Pokemon var3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Shadow
	@Override
	public Optional<Pokemon> makeEgg(Pokemon var1, Pokemon var2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Shadow
	@Override
	public Pokemon makeRandomEgg(Pokemon var1, Pokemon var2) {
		// TODO Auto-generated method stub
		return null;
	}
}
