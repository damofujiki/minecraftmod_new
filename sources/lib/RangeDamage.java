package com.hinasch.lib;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class RangeDamage {

	public World world;
	public boolean hitOnGroundOnly = true;
	
	public static AxisAlignedBB makeBBFromPlayer(EntityPlayer player,double horizontal,double vertical){
		return player.boundingBox.expand(horizontal,vertical,horizontal);
	}
	
	public RangeDamage(World world){
		this.world = world;
		this.hitOnGroundOnly = false;
	}
	
	public RangeDamage setHitOnGroundOnly(boolean par1){
		this.hitOnGroundOnly = par1;
		return this;
	}
	public void causeDamage(DamageSource ds,AxisAlignedBB aabb,float damage){
		Entity attacker = ds.getEntity();
		List<EntityLivingBase> mobList = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
		if(!mobList.isEmpty()){
			for(EntityLivingBase mob:mobList){
				if(mob!=attacker){
					boolean isLockedOn = true;
					if(isSameTeam(attacker,mob)){
						isLockedOn = false;
					}
					
					if(isLockedOn){
						this.mobHasLockedOn(ds,aabb,damage,mob);
					}
				}
			}
		}
	}
	
	protected void mobHasLockedOn(DamageSource ds, AxisAlignedBB aabb, float damage,EntityLivingBase mob){
		if(mob.onGround && this.hitOnGroundOnly || !this.hitOnGroundOnly){
			mob.attackEntityFrom(ds, damage);
			this.hookOnMobHasAttacked(ds,aabb,damage,mob);
		}
	}
	

	public void hookOnMobHasAttacked(DamageSource ds, AxisAlignedBB aabb, float damage, EntityLivingBase mob){
		
	}
	
	protected boolean isSameTeam(Entity atacker,EntityLivingBase mob){
		if(atacker instanceof EntityLivingBase){
			if(HSLibs.isSameTeam((EntityLivingBase) atacker, mob)){
				return true;
			}
		}
		return false;
	}
}
