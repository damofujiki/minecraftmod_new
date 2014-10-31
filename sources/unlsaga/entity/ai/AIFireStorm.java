package com.hinasch.unlsaga.entity.ai;

import net.minecraft.entity.EntityLivingBase;

import com.hinasch.lib.XYZPos;
import com.hinasch.lib.net.AbstractAI;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingDebuff;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingStateFireStorm;

public class AIFireStorm extends AbstractAI{

	protected EntityLivingBase owner;
	
	public AIFireStorm(String name,EntityLivingBase owner){
		this.name = name;
		this.owner = owner;
	}
	@Override
	public int getWeight() {
		// TODO 自動生成されたメソッド・スタブ
		return 2;
	}

	@Override
	public void task(EntityLivingBase target) {
		// TODO 自動生成されたメソッド・スタブ
		if(target!=null){
			XYZPos xyz = XYZPos.entityPosToXYZ(target);
			LivingDebuff.addLivingDebuff(owner, new LivingStateFireStorm(Unsaga.debuffManager.crimsonFlare,100,xyz.x,xyz.y,xyz.z,1,-1));
		}

	}

	@Override
	public double getMaxDistance() {
		// TODO 自動生成されたメソッド・スタブ
		return 8.0D;
	}

	@Override
	public double getMinDistance() {
		// TODO 自動生成されたメソッド・スタブ
		return 2.0D;
	}

}
