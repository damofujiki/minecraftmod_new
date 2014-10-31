package com.hinasch.unlsaga.ability.skill.effect;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import com.hinasch.lib.HSLibs;
import com.hinasch.lib.WorldHelper;
import com.hinasch.lib.XYZPos;

public abstract class AbstractInvoker {

	public EntityLivingBase owner;
	public final World world;
	public final WorldHelper worldHelper;
	
	public AbstractInvoker(World world,EntityLivingBase owner){
		this.owner = owner;
		this.worldHelper = new WorldHelper(world);
		this.world = world;
	}
	
	public void addPotion(EntityLivingBase target,int potionID,int time,int amp){
		target.addPotionEffect(new PotionEffect(potionID,time,amp));
	}

	public void addPotionChance(int prob,EntityLivingBase target,int potionID,int time,int amp){
		if(this.world.rand.nextInt(100)<prob){
			target.addPotionEffect(new PotionEffect(potionID,time,amp));
		}

	}
	
	public String getLargeExplode(){
		return "largeexplode";
	}
	public String getFireBallSound(){
		return "mob.ghast.fireball";
	}

	public void playSound(String par1){
		if(this.owner instanceof EntityPlayer){
			this.owner.playSound(par1, 0.5F, 1.8F / (this.world.rand.nextFloat() * 0.4F + 1.2F));
		}else{
			List<EntityPlayer> eps = this.world.getEntitiesWithinAABB(EntityPlayer.class, HSLibs.getBounding(XYZPos.entityPosToXYZ(this.owner), 10.0D, 10.0D));
			for(EntityPlayer ep:eps){
				ep.playSound(par1, 0.5F, 1.8F / (this.world.rand.nextFloat() * 0.4F + 1.2F));
			}
		}


	}

	public void playExplodeSound(XYZPos pos){
		this.world.playSoundEffect(pos.dx, pos.dy, pos.dz, this.getExplodeSonud(), 4.0F, (1.0F + (this.owner.worldObj.rand.nextFloat() - this.owner.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
	}
	public String getWitherSound(){
		return "mob.wither.shoot";

	}

	public String getExplodeSonud(){
		return "random.explode";
	}

	public void playSoundAt(Entity target,String str){
		this.world.playSoundAtEntity(target, getFireBallSound(), 1.0F, 1.0F / (this.world.rand.nextFloat() * 0.4F + 1.2F) + 1.0F * 0.5F);

	}

	public void spawnParticle(String par1,Entity target){
		this.world.spawnParticle(par1, (double)target.posX+0.5D, (double)target.posY+1, (double)target.posZ+0.5D, 1.0D, 0.0D, 0.0D);

	}

	public void playBowSound(float charge){
		this.world.playSoundAtEntity(owner, "random.bow", 1.0F, 1.0F / (this.world.rand.nextFloat() * 0.4F + 1.2F) + charge * 0.5F);

	}
}
