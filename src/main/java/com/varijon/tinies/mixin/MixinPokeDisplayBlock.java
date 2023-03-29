package com.varijon.tinies.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.api.util.helpers.NetworkHelper;
import com.pixelmonmod.pixelmon.blocks.decorative.PokeDisplayBlock;
import com.pixelmonmod.pixelmon.blocks.tileentity.PokeDisplayTileEntity;
import com.pixelmonmod.pixelmon.comm.packetHandlers.statueEditor.PokeDisplayOpenPacket;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.Property;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

@Mixin(PokeDisplayBlock.class)
public class MixinPokeDisplayBlock extends Block
{
	@Shadow
    public static final BooleanProperty RETURN = BooleanProperty.create("return");

	public MixinPokeDisplayBlock(Properties p_i48440_1_) {
		super(p_i48440_1_);
		// TODO Auto-generated constructor stub
	}

	@Overwrite(remap=false)
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult traceResult) {
        if (world.isClientSide || hand != Hand.MAIN_HAND) {
            return ActionResultType.SUCCESS;
        }
        PokeDisplayTileEntity blockEntity = (PokeDisplayTileEntity)world.getBlockEntity(pos);
        if (blockEntity.isLocked()) {
            return ActionResultType.PASS;
        }
        if(blockEntity.getStoredPoke().isPresent())
        {
        	Pokemon pokemon = blockEntity.getStoredPoke().get();
        	if(!player.getUUID().equals(pokemon.getOwnerPlayerUUID()))
        	{
        		return ActionResultType.PASS;
        	}
        }
        
        PlayerPartyStorage party = StorageProxy.getParty((ServerPlayerEntity)player);
        if (party == null || party.countAblePokemon() < 2 && !blockEntity.getStoredPoke().isPresent()) {
            player.sendMessage((ITextComponent)new TranslationTextComponent("poke.display.block.party.size"), Util.NIL_UUID);
            return ActionResultType.PASS;
        }
        blockEntity.setLocked(player);
        NetworkHelper.sendPacket((ServerPlayerEntity)player, new PokeDisplayOpenPacket(pos));
        return ActionResultType.SUCCESS;
    }

	@Overwrite(remap=false)
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid) {
        if (world.isClientSide) {
            return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
        }
        TileEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof PokeDisplayTileEntity) 
        {
            if (!((Boolean)state.hasProperty(RETURN)).booleanValue()) {
                return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
            }
            PokeDisplayTileEntity display = (PokeDisplayTileEntity) blockEntity;
            if(display.getStoredPoke().isPresent())
            {
            	Pokemon pokemon = display.getStoredPoke().get();
            	if(!player.getUUID().equals(pokemon.getOwnerPlayerUUID()) && !player.isCreative())
            	{
            		return false;
            	}
                PlayerPartyStorage party = StorageProxy.getParty(pokemon.getOwnerPlayerUUID());
                display.getStoredPoke().ifPresent(party::add);
            }
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
    }
	
}
