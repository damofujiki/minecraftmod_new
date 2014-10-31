package com.hinasch.unlsaga.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

import com.hinasch.lib.net.AbstractAI;
import com.hinasch.unlsaga.Unsaga;

public class AIThrowProjectile extends AbstractAI{

	//protected String name = "rangedAttackAI";
	protected World world;
	protected IThrowableAttack owner;
	protected int weight;
	protected double MaxDistance;
	protected double MinDistance;
	
	public AIThrowProjectile(String name,World world,IThrowableAttack owner,int weight){
		this.name = name;
		this.world = world;
		this.owner = owner;
		this.weight = weight;
		this.MaxDistance = 18.0D;
		this.MaxDistance = 0.2D;
	}
	
	public AIThrowProjectile setMinMaxDistance(double min,double max){
		this.MaxDistance = max;
		this.MinDistance = min;
		return this;
	}
	@Override
	public int getWeight() {
		// TODO 自動生成されたメソッド・スタブ
		return this.weight;
	}

	@Override
	public void task(EntityLivingBase target) {
		if(target!=null){
			Entity throwable = owner.getNewThrowableInstance(this,target,0.2F);
	        if(!this.world.isRemote && throwable!=null){
	        	this.world.spawnEntityInWorld(throwable);
	        }
		}
		Unsaga.debug("doTask:"+this.name);
		


		
	}

	@Override
	public double getMaxDistance() {
		// TODO 自動生成されたメソッド・スタブ
		return 18.0D;
	}

	@Override
	public double getMinDistance() {
		// TODO 自動生成されたメソッド・スタブ
		return 0.2D;
	}


}
