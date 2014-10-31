package com.hinasch.unlsaga.entity.ai;

import net.minecraft.entity.EntityLivingBase;

import com.hinasch.lib.XYZPos;
import com.hinasch.lib.net.AbstractAI;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.skill.effect.InvokeSkill;

public class AIInvokeSkill extends AbstractAI{

	public EntityLivingBase attacker;
	
	public AIInvokeSkill(EntityLivingBase living){
		this.attacker = living;
		this.name = "Invoke Skill";
	}
	@Override
	public int getWeight() {
		// TODO 自動生成されたメソッド・スタブ
		return 6;
	}

	@Override
	public void task(EntityLivingBase target) {
		
		InvokeSkill invoker = new InvokeSkill(this.attacker.worldObj,this.attacker,Unsaga.abilityManager.grassHopper,this.attacker.getHeldItem());
		invoker.setUsePoint(XYZPos.entityPosToXYZ(target));
		invoker.doSkill();
	}

	@Override
	public double getMaxDistance() {
		// TODO 自動生成されたメソッド・スタブ
		return 12.0D;
	}

	@Override
	public double getMinDistance() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

}
