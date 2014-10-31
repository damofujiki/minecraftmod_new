package com.hinasch.unlsaga.event;

import com.hinasch.unlsaga.Unsaga;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventConfigChanged {

	
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent e){
		if(e.modID.equals(Unsaga.MODID)){
			Unsaga.debug(this.getClass().getName(),"sync!");
			Unsaga.configs.syncConfig();
		}
	}
	
	
}
