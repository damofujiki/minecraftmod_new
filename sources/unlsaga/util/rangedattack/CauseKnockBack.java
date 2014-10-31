package com.hinasch.unlsaga.util.rangedattack;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.hinasch.lib.RangeDamage;

public class CauseKnockBack extends RangeDamage{

	public double knockbackStr;
	public float lpdamage;
	
	public CauseKnockBack(World world,double knock) {
		super(world);
		this.knockbackStr = knock;
	}
	
	public void setLPDamage(float f){
		this.lpdamage = f;
	}

	@Override
	public void hookOnMobHasAttacked(DamageSource ds, AxisAlignedBB aabb, float damage, EntityLivingBase mob){
		mob.knockBack(mob, 0, 2.0D*knockbackStr, 1.0D*knockbackStr);
	}
	

}
