package com.hinasch.unlsagamagic.spell.effect;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;

import com.hinasch.lib.RangeDamageHelper;


public class SpellRangedAttack extends SpellBase{

	protected double rangeVertical;
	protected double rangeHorizontal;
	protected boolean onGroundOnly = false;
	
	public SpellRangedAttack(double horizontal,double vertical){
		super();
		this.rangeHorizontal = horizontal;
		this.rangeVertical = vertical;
	}
	

	
	public RangeDamageHelper getCustomizedRangeDamageHelper(InvokeSpell parent){
		return null;
	}
	
	public AxisAlignedBB getCustomizedBoundingBox(InvokeSpell parent){
		return null;
	}
	
	public DamageSource getDamageSource(InvokeSpell parent){
		return parent.getDamageSource();
	}
	
	public void hookStart(InvokeSpell parent){
		
	}
	
	public void hookEnd(InvokeSpell parent){
		
	}

	@Override
	public void invokeSpell(InvokeSpell parent) {
		this.hookStart(parent);
		AxisAlignedBB bb = null;
		if(this.getCustomizedBoundingBox(parent)==null){
			bb = parent.getInvoker().boundingBox.expand(rangeHorizontal, rangeVertical, rangeHorizontal);
		}else{
			bb = this.getCustomizedBoundingBox(parent);
		}
		if(this.getCustomizedRangeDamageHelper(parent)!=null){
			this.getCustomizedRangeDamageHelper(parent).causeRangeDamage(bb, this.getDamageSource(parent), parent.getSpell().getStrHurtHP(),onGroundOnly);
		}else{
			RangeDamageHelper.causeDamage(parent.getInvoker().worldObj, null, bb, this.getDamageSource(parent), parent.getSpell().getStrHurtHP(),onGroundOnly);
		}
		
		this.hookEnd(parent);
	}





}
