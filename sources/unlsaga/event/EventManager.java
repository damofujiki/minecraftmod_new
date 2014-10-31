package com.hinasch.unlsaga.event;

import com.hinasch.lib.HSLibs;
import com.hinasch.unlsaga.event.extendeddata.ExtendedDataLP;
import com.hinasch.unlsaga.event.extendeddata.ExtendedEntityTag;
import com.hinasch.unlsaga.event.extendeddata.ExtendedLivingData;
import com.hinasch.unlsaga.event.extendeddata.ExtendedMerchantData;
import com.hinasch.unlsaga.event.extendeddata.ExtendedPlayerData;
import com.hinasch.unlsaga.item.panel.SkillPanels;

import cpw.mods.fml.common.FMLCommonHandler;

public class EventManager {

	
	public void register(){
		//HSLibs.registerEvent(new EventInitEnemyWeapon()); //敵に武器を持たせる関連
		HSLibs.registerEvent(new EventInteractEntity()); //エンティティにライトクリックした時
		HSLibs.registerEvent(new ExtendedPlayerData()); //プレイヤーの拡張インベントリ
		HSLibs.registerEvent(new ExtendedMerchantData()); //物々交換関連
		HSLibs.registerEvent(new EventUnsagaToolTip()); //ツールチップ
		//TickRegistry.registerTickHandler(new TickHandlerUnsaga(), Side.SERVER);
		HSLibs.registerEvent(new EventLivingDeath()); //敵が死んだ時
		HSLibs.registerEvent(new EventGainSkillOnAttack()); //敵を攻撃した時
		HSLibs.registerEvent(new ExtendedLivingData()); //デバフ関連
		HSLibs.registerEvent(new EventLivingUpdate()); //TickHandlerのかわり
		HSLibs.registerEvent(new ExtendedEntityTag()); //矢に属性つけたり
		HSLibs.registerEvent(new EventLivingHurt());
		HSLibs.registerEvent(new EventReplaceFoodStats());
		HSLibs.registerEvent(new ExtendedDataLP());
		HSLibs.registerEvent(new EventLivingDrops());
		HSLibs.registerEvent(new EventAnvil());
		HSLibs.registerEvent(SkillPanels.EventBreakSpeed.getEvent());
		//HSLibs.registerEvent(new SkillBow());
		HSLibs.registerEvent(new EventFall());
		HSLibs.registerEvent(new EventPickUpExp());
		FMLCommonHandler.instance().bus().register(new EventConfigChanged());

		
	}
}
