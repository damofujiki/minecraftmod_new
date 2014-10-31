package com.hinasch.lib;



import java.util.List;

import com.hinasch.unlsaga.ability.skill.effect.InvokeSkill;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class RangeDamageHelper {


	public World world;
	public InvokeSkill helper;
	public EntityPlayer ep;
	public RangeDamageHelper(World world){
		this.world = world;
	}
	
	public RangeDamageHelper(World world,InvokeSkill parent){
		this.world = world;
		this.helper = parent;
	}
	
	public void setSkillEffectHelper(InvokeSkill parent){
		this.helper = parent;
	}
	

	
	public static AxisAlignedBB makeBBFromPlayer(EntityPlayer player,double horizontal,double vertical){
		return player.boundingBox.expand(horizontal,vertical,horizontal);
	}
	
	public void causeRangeDamage(AxisAlignedBB bb,DamageSource ds,float damage,boolean onGroundOnly){
		causeDamage(this.world,this,bb,ds,damage,onGroundOnly);
	}
	
	public void causeRangeDamage(AxisAlignedBB bb,DamageSource ds,float damage){
		causeDamage(this.world,this,bb,ds,damage);
	}
	
	public static void causeDamage(World world,RangeDamageHelper rangeHelper,AxisAlignedBB bb,DamageSource ds,float damage){
		causeDamage(world,rangeHelper,bb,ds,damage,false);
	}
	public static void causeDamage(World world,RangeDamageHelper rangeHelper,AxisAlignedBB bb,DamageSource ds,float damage,boolean onGroundOnly){
		Entity damageEntity = ds.getEntity();
		List<EntityLivingBase> mobs = world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
		if(!mobs.isEmpty()){
			for(EntityLivingBase mob:mobs){
				if(mob!=damageEntity){
					boolean flag = true;
					if(damageEntity instanceof EntityLivingBase){
						if(HSLibs.isSameTeam((EntityLivingBase) damageEntity, mob)){
							flag = false;
						}
					}
//					if(mob instanceof EntityPlayer && damageEntity instanceof EntityPlayer){
//						if(!((EntityPlayer) mob).canAttackPlayer((EntityPlayer) damageEntity));{
//							flag = false;
//						}
//					}
					if(flag){
						if((mob.onGround && onGroundOnly) || !onGroundOnly){
							mob.attackEntityFrom(ds, damage);
							if(rangeHelper!=null){
								rangeHelper.takeEntityLiving(mob, ds);
							}
						}
					}


				}
			}
		}

	}

	public void takeEntityLiving(EntityLivingBase living,DamageSource source){
		
	}
}
