package com.hinasch.unlsaga.entity.ai;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.skill.Skill;
import com.hinasch.unlsaga.ability.skill.effect.InvokeSkill;

public class EntityAISkill extends EntityAIBase{

	protected final List<SkillAIData> spellList;
	protected List<SkillAIData> rangedList;
	protected EntityLivingBase attackTarget;
	protected final EntityLiving entityHost;
	protected final ISkillAI entityInvoker;
	protected double entityMoveSpeed;
	protected int rangedAttackTime;
	protected int maxRangedAttackTime;
	protected InvokeSkill invoker;
	//索敵範囲
	protected float search;
	protected float attackPowerFromRange;
	protected int cooling = 0;

	//謎
	private int field_96561_g = 60;

	public EntityAISkill(ISkillAI host,List spellList,double moveSpeed,int interval,float search){
		this.rangedAttackTime = -1;
		this.spellList = spellList;
		this.rangedList = new ArrayList();
		this.entityHost = (EntityLiving) host;
		this.entityInvoker = (ISkillAI)host;
		this.entityMoveSpeed = moveSpeed;
		this.maxRangedAttackTime = interval;
		this.attackPowerFromRange = search;
		this.search = search * search;
	}
	@Override
	public boolean shouldExecute() {
		//Unsaga.debug("spell",this.entityHost.getAttackTarget());
		EntityLivingBase entitylivingbase = this.entityHost.getAttackTarget();
		if(this.entityInvoker.canInvokeSkill()){
			if (entitylivingbase == null)
			{
				return false;
			}
			else
			{
				this.attackTarget = entitylivingbase;
				return true;
			}
		}

		return false;
	}

	@Override
	public void resetTask()
	{
		this.attackTarget = null;
		this.cooling = 0;
		this.rangedAttackTime = -1;
	}

	@Override
	public boolean continueExecuting()
	{
		return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
	}

	@Override
	public void updateTask()
	{
		double d0 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.boundingBox.minY, this.attackTarget.posZ);
		boolean flag = this.entityHost.getEntitySenses().canSee(this.attackTarget);

		if (flag)
		{
			++this.cooling;
		}
		else
		{
			this.cooling = 0;
		}

		if (d0 <= (double)this.search && this.cooling >= 20)
		{
			this.entityHost.getNavigator().clearPathEntity();
		}
		else
		{
			this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
		}

		this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
		float f;

		if (--this.rangedAttackTime == 0)
		{
			if (d0 > (double)this.search || !flag)
			{
				return;
			}

			f = MathHelper.sqrt_double(d0) / this.attackPowerFromRange;
			float f1 = f;

			if (f < 0.1F)
			{
				f1 = 0.1F;
			}

			if (f1 > 1.0F)
			{
				f1 = 1.0F;
			}

			this.executeSpell(this.attackTarget, f1);
			this.rangedAttackTime = MathHelper.floor_float(f * (float)(this.maxRangedAttackTime - this.field_96561_g) + (float)this.field_96561_g);
		}
		else if (this.rangedAttackTime < 0)
		{
			f = MathHelper.sqrt_double(d0) / this.attackPowerFromRange;
			this.rangedAttackTime = MathHelper.floor_float(f * (float)(this.maxRangedAttackTime - this.field_96561_g) + (float)this.field_96561_g);
		}
	}


	public void executeSpell(EntityLivingBase target,float f1){
		double rangeToTarget = this.entityHost.getDistanceSqToEntity(target);
		for(SkillAIData p:this.spellList){
			if(p.maxDistance>=rangeToTarget && p.minDistance<=rangeToTarget){
				this.rangedList.add(p);
			}
		}

		Unsaga.debug("spellList",this.spellList,"distance:",rangeToTarget,"Spell Ranged:",this.rangedList);
		if(!this.rangedList.isEmpty()){

			int rn = this.rangedList.size()<=1? 0: this.entityHost.getRNG().nextInt(this.rangedList.size());
			Skill skill = this.rangedList.get(rn).skill;
			this.invoker = new InvokeSkill(this.entityHost.worldObj, this.entityHost, skill, this.entityHost.getHeldItem());
			this.invoker.setTarget(target);
			if(this.entityInvoker.preInvoke(invoker)){
				this.invoker.doSkill();
			}


		}
	}

	public static class SkillAIData{
		public final Skill skill;
		public final double maxDistance;
		public final double minDistance;
		public SkillAIData(Skill skill,double max,double min){
			this.skill = skill;
			this.maxDistance =max;
			this.minDistance = min;
		}
	}

	public static interface ISkillAI{
		public boolean canInvokeSkill();
		public boolean preInvoke(InvokeSkill invoker);
	}
}



