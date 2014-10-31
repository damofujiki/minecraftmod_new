package com.hinasch.unlsaga.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.hinasch.lib.XYZPos;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.skill.effect.InvokeSkill;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingDebuff;
import com.hinasch.unlsaga.entity.ai.AIInvokeSkill;
import com.hinasch.unlsaga.entity.ai.AISelector;
import com.hinasch.unlsaga.entity.ai.AISwing;
import com.hinasch.unlsaga.entity.ai.EntityAISkill;
import com.hinasch.unlsaga.entity.ai.EntityAISkill.ISkillAI;
import com.hinasch.unlsaga.entity.ai.EntityAISkill.SkillAIData;
import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;

public class EntityGolemUnsaga extends EntityMob implements ISkillAI{

	protected AISelector aiStore;
	protected final List<SkillAIData> skillList;
	
	public EntityGolemUnsaga(World par1World) {
		super(par1World);
		this.skillList = new ArrayList();
		this.skillList.add(new SkillAIData(Unsaga.abilityManager.grassHopper,40.0D,0.0D));
		this.setCurrentItemOrArmor(0, Unsaga.items.getItemStack(ToolCategory.SPEAR,Unsaga.materialManager.steel1, 1, 0));
		this.setSize(1.5F, 5.0F);
        this.tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityPlayer.class, 2.0D, false));
        this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
        this.tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(4, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(5, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        this.targetTasks.addTask(3, new EntityAISkill(this,this.skillList,1.0D,200,15.0F));
        this.aiStore = new AISelector(this,par1World.rand);
        this.aiStore.addAI(new AISwing(this));
        this.aiStore.addAI(new AIInvokeSkill(this));

	}

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0D);
        

    }
    
    protected void entityInit()
    {
    	super.entityInit();

    }
    
    protected String getHurtSound()
    {
        return "mob.irongolem.hit";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.irongolem.death";
    }

    protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_)
    {
        this.playSound("mob.irongolem.walk", 1.0F, 1.0F);
    }

	@Override
	public void onUpdate(){
		super.onUpdate();
		//this.aiStore.updateAITick();
	}
	
    @Override
    protected boolean isAIEnabled()
    {
        return true;
    }

	@Override
	public boolean canInvokeSkill() {
		if(LivingDebuff.hasDebuff(this, Unsaga.debuffManager.sleep)){
			return false;
		}
		return true;
	}

	@Override
	public boolean  preInvoke(InvokeSkill invoker) {
		if(invoker.target!=null && invoker.getSkill()==Unsaga.abilityManager.grassHopper){
			invoker.setUsePoint(XYZPos.entityPosToXYZ(invoker.target));
			return true;
		}
		return false;
		
	}
	
}
