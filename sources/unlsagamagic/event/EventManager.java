package com.hinasch.unlsagamagic.event;

import com.hinasch.lib.HSLibs;

public class EventManager {

	public void register(){
		//TickRegistry.registerTickHandler(new EventDecipherAtSleep(), Side.SERVER);
		HSLibs.registerEvent(new EventSpellTarget());
		HSLibs.registerEvent(new EventSpellBuff());
		HSLibs.registerEvent(new EventDecipherAtSleep());
	}
}
