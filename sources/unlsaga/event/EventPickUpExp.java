package com.hinasch.unlsaga.event;

import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;

import com.hinasch.unlsaga.Unsaga;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventPickUpExp {

	@SubscribeEvent
	public void onPickUpExp(PlayerPickupXpEvent e){

			if( !Unsaga.configs.isDecipherAtSleep){
				if(e.entityPlayer.getRNG().nextInt(10)==0){
					Unsaga.magic.deciphProcess.progressOnTakingXP(e);
				}

			}
		

	}
}
