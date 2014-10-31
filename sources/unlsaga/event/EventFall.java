package com.hinasch.unlsaga.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingFallEvent;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.debuff.Debuffs;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingDebuff;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventFall {

	
	@SubscribeEvent
	public void onLivingFall(LivingFallEvent e){
		EntityLivingBase hurtEntity = e.entityLiving;
		Debuffs debuffs = Unsaga.debuffManager;
		if(LivingDebuff.hasDebuff(hurtEntity, debuffs.antiFallDamage)){
			//hurtEntity.fallDistance = 0;
			e.setCanceled(true);
			LivingDebuff.removeDebuff(hurtEntity, debuffs.antiFallDamage);
			return;
		}
	}
}
