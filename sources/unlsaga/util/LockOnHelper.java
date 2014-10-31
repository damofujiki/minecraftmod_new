package com.hinasch.unlsaga.util;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingDebuff;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingStateTarget;
import com.hinasch.unlsaga.debuff.state.State;

public class LockOnHelper {

	
	public static void setAttackTarget(EntityLivingBase attacker,EntityLivingBase target){
		setTarget(attacker,target,Unsaga.debuffManager.weaponTarget);
	}
	
	public static void setSpellTarget(EntityLivingBase attacker,EntityLivingBase target){
		setTarget(attacker,target,Unsaga.debuffManager.spellTarget);
	}
	
	public static void setTarget(EntityLivingBase attacker,EntityLivingBase target,State debuff){
		LivingStateTarget state = new LivingStateTarget(debuff,30,target.getEntityId());
		LivingDebuff.addLivingDebuff(attacker, state);
		if(attacker instanceof EntityPlayer){
			ChatMessageHandler.sendChatToPlayer((EntityPlayer) attacker, "Set Target To "+target.getCommandSenderName());
		}
		if(target instanceof EntityPlayer){
			ChatMessageHandler.sendChatToPlayer((EntityPlayer) target, attacker.getCommandSenderName()+" Sets Target To You");
		}
		
	}
	
	public static EntityLivingBase searchEnemyNear(EntityLivingBase attacker,State state){
		AxisAlignedBB bb = attacker.boundingBox.expand(10.0D, 10.0D, 10.0D);
		if(attacker instanceof EntityPlayer){
			List<EntityLivingBase> entitynearlist = attacker.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, bb);
			if(!entitynearlist.isEmpty()){
				for(EntityLivingBase entity:entitynearlist){
					if(entity!=attacker && entity instanceof IMob){
						setTarget(attacker,entity,state);
						return entity;
					}
				}
			}
		}
		if(attacker instanceof IMob){
			EntityPlayer ep = attacker.worldObj.getClosestPlayerToEntity(attacker, 10.0D);
			if(ep!=null){
				return ep;
			}
		}


		return null;
	}



}
