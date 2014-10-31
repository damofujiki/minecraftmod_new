package com.hinasch.unlsaga.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public interface IThrowableAttack {

	public Entity getNewThrowableInstance(AIThrowProjectile parent,EntityLivingBase target,float f);
	
}
