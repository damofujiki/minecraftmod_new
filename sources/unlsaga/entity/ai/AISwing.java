package com.hinasch.unlsaga.entity.ai;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.hinasch.lib.HSLibs;
import com.hinasch.lib.XYZPos;
import com.hinasch.lib.net.AbstractAI;
import com.hinasch.unlsaga.Unsaga;

public class AISwing extends AbstractAI{

	public EntityLivingBase attacker;
	public AISwing(EntityLivingBase entity){
		this.name = "Swing";

		this.attacker = entity;
		Unsaga.debug("つくられました",this.getClass());
	}
	@Override
	public int getWeight() {
		// TODO 自動生成されたメソッド・スタブ
		return 10;
	}

	public void preTask(EntityLivingBase target,AISelector parent){
		this.parent = parent;
		this.task(target);
	}
	@Override
	public void task(EntityLivingBase target) {
		if(this.attacker.swingProgress!=0.0F)return;
		this.attacker.swingItem();
		Unsaga.debug("実行",this.getClass());
		World world = target.worldObj;
		List<EntityPlayer> list = world.getEntitiesWithinAABB(EntityPlayer.class, HSLibs.getBounding(XYZPos.entityPosToXYZ(attacker),10.0D, 4.0D));
		for(EntityPlayer ep:list){
			ep.attackEntityFrom(DamageSource.causeMobDamage(target), (float)attacker.getEntityAttribute(SharedMonsterAttributes.attackDamage).getBaseValue());
			
		}
		
	}

	@Override
	public double getMaxDistance() {
		// TODO 自動生成されたメソッド・スタブ
		return 10.0D;
	}

	@Override
	public double getMinDistance() {
		// TODO 自動生成されたメソッド・スタブ
		return 0.0D;
	}

}
