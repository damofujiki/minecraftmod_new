package com.hinasch.unlsaga.ability.skill.effect;

import net.minecraft.entity.EntityLivingBase;

abstract public class SkillBase extends AbstractSkillEffect{

	@Override
	public void invoke(Object parent){
		this.invokeSkill((InvokeSkill)parent);
	}
	
	abstract public void invokeSkill(InvokeSkill parent);
	
	protected void playShootSound(EntityLivingBase ep){
		ep.playSound("mob.wither.shoot", 0.5F, 1.8F / (ep.getRNG().nextFloat() * 0.4F + 1.2F));
	}
}
