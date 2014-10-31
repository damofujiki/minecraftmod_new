package com.hinasch.unlsagamagic.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.event.extendeddata.ExtendedPlayerData;
import com.hinasch.unlsagamagic.item.ItemTablet;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventDecipherAtSleep {



	@SubscribeEvent
	public void onPlayerTick(LivingUpdateEvent e) {
		if(e.entityLiving instanceof EntityPlayer){
			EntityPlayer entityPlayer = (EntityPlayer)e.entityLiving;
			if(entityPlayer.getSleepTimer() == 99){
				onPlayerSleep(entityPlayer.worldObj,entityPlayer);
			}
		}

	}

	public void onPlayerSleep(World world,EntityPlayer ep){
		if(!Unsaga.configs.isDecipherAtSleep)return;


		ExtendedPlayerData data = ExtendedPlayerData.getData(ep);
		if(data.getTablet()!=null && data.getTablet().getItem() instanceof ItemTablet){
			ItemStack tablet = data.getTablet();
			if(!ItemTablet.isDeciphered(tablet)){
				Unsaga.magic.deciphProcess.progress(world, ep, tablet);
			}
		}


	}

}
