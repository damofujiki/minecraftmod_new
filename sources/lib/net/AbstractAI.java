package com.hinasch.lib.net;

import com.hinasch.unlsaga.entity.ai.AISelector;

import net.minecraft.entity.EntityLivingBase;

public abstract class AbstractAI {

	public String name;
	public AISelector parent;
	
	public String getName(){
		return this.name;
	}
	
	public void setParentAIStore(AISelector ai){
		this.parent = ai;
	}
	
	public abstract int getWeight();
	public abstract void task(EntityLivingBase target);
	public abstract double getMaxDistance();
	public abstract double getMinDistance();
}
