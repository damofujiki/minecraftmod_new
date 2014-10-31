package com.hinasch.unlsaga.ability.skill.effect;

import com.hinasch.lib.WorldHelper;

import net.minecraft.world.World;

public abstract class AbstractSkillEffect {

	public WorldHelper worldHelper;
	public AbstractSkillEffect(){
		
	}
	

	public void setWorldHelper(World world){
		this.worldHelper = new WorldHelper(world);
	}
	
	public abstract void invoke(Object parent);

}
