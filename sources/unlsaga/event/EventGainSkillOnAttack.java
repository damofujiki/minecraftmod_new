package com.hinasch.unlsaga.event;

import com.hinasch.unlsaga.ability.IAbility;
import com.hinasch.unlsaga.ability.skill.HelperSkill;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventGainSkillOnAttack {

	@SubscribeEvent 
	public void inspireEvent(LivingAttackEvent e){
		Entity attacker = e.source.getEntity();
		EntityLivingBase entityHurt = e.entityLiving;
		
		if(e.source!=null && attacker instanceof EntityPlayer){
			if(entityHurt instanceof EntityMob){
				EntityPlayer ep = (EntityPlayer)attacker;
				ItemStack weapon = ep.getHeldItem();

				if(weapon!=null && weapon.getItem() instanceof IAbility){
					HelperSkill helper = new HelperSkill(weapon,ep);
					helper.drawChanceToGainAbility(ep.getRNG(),entityHurt);


				}
			}

		}
	}
}
