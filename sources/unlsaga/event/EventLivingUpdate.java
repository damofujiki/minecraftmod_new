package com.hinasch.unlsaga.event;

import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.Ability;
import com.hinasch.unlsaga.ability.AbilityRegistry;
import com.hinasch.unlsaga.ability.skill.effect.SkillBow;
import com.hinasch.unlsaga.debuff.Debuffs;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventLivingUpdate {

	protected AxisAlignedBB bb;
	protected AbilityRegistry ar = Unsaga.abilityManager;
	@SubscribeEvent
	public void onLivingUpdata(LivingUpdateEvent e){
		SkillBow.checkArrowOnStoppedEvent(e);
		SkillBow.checkArrowParticleEvent(e);

		Ability.abilityEventOnLivingUpdate(e.entityLiving,ar);

		Debuffs.debuffEventOnLivingUpdate(e);
		

		if(Unsaga.configs.enableLP){
			Unsaga.lpManager.lpEventOnLivingUpdate(e);
		}


		if(!e.entityLiving.worldObj.isRemote){
			Unsaga.scannerPool.update();
		}
		
	}
	
	

}
