package com.hinasch.unlsaga.ability.skill.effect;

import net.minecraft.util.AxisAlignedBB;

import com.hinasch.lib.RangeDamage;

public class SkillRangedAttack extends SkillMelee{

	protected double rangeVertical;
	protected double rangeHorizontal;
	protected boolean onGroundOnly = false;
	
	public SkillRangedAttack(SkillMelee.Type type,double horizontal,double vertical){
		super(type);
		this.rangeHorizontal = horizontal;
		this.rangeVertical = vertical;
	}
	
	@Override
	public void invokeSkill(InvokeSkill parent) {
		this.hookStart(parent);
		AxisAlignedBB bb = null;
		if(this.getCustomizedBoundingBox(parent)==null){
			bb = parent.owner.boundingBox.expand(rangeHorizontal, rangeVertical, rangeHorizontal);
		}else{
			bb = this.getCustomizedBoundingBox(parent);
		}
		if(this.getCustomizedRangeDamageHelper(parent)!=null){
			
			this.getCustomizedRangeDamageHelper(parent).setHitOnGroundOnly(onGroundOnly).causeDamage(parent.getDamageSource(), bb, parent.getModifiedAttackDamage());
		}else{
			RangeDamage rd = new RangeDamage(parent.owner.worldObj);
			rd.causeDamage(parent.getDamageSource(), bb, parent.getModifiedAttackDamage());
		}
		
		this.hookEnd(parent);
	}
	
	public RangeDamage getCustomizedRangeDamageHelper(InvokeSkill parent){
		return null;
	}
	
	public AxisAlignedBB getCustomizedBoundingBox(InvokeSkill parent){
		return null;
	}
	
	public void hookStart(InvokeSkill parent){
		
	}
	
	public void hookEnd(InvokeSkill parent){
		
	}

}
