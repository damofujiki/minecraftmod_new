package com.hinasch.unlsaga.debuff.state;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.debuff.Debuff;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingDebuff;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingState;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingStateRandomThrow;
import com.hinasch.unlsaga.entity.projectile.EntityBoulderNew;
import com.hinasch.unlsaga.entity.projectile.EntitySolutionLiquid;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class StateRandomThrow extends State{

	protected String soundStr;
	protected int interval;
	
	public StateRandomThrow(int num, String nameEn) {
		super(num, nameEn);
		this.soundStr = "";
		this.interval = 1;
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public LivingDebuff init(String[] strs){
		Debuff debuff = Unsaga.debuffManager.getDebuff(Integer.valueOf(strs[0]));
		int remain = Integer.valueOf(strs[1]);
		int shoottick = 0;
		int amp = 0;
		if(strs.length>2){
			shoottick = Integer.valueOf(strs[2]);
			amp = Integer.valueOf(strs[3]);
		}
		return new LivingStateRandomThrow(debuff,remain,shoottick,amp);
	}
	
	public Entity getThrowingEntity(LivingState parent,EntityLivingBase thrower,double dx,double dy,double dz){
		if(this==Unsaga.debuffManager.stoneShower){
			EntityBoulderNew var8 = new EntityBoulderNew(thrower.worldObj, thrower, 1.0F * 2.0F);
			var8.setLocationAndAngles(var8.posX+dx, var8.posY+dy, var8.posZ+dz, var8.rotationYaw, var8.rotationPitch);
			var8.setDamage(Unsaga.magic.spellManager.stoneShower.getStrHurtHP()+parent.getModifierAttackBuff(thrower));
			return var8;
		}
		if(this==Unsaga.debuffManager.thunderCrap){
			EntitySolutionLiquid liquid = new EntitySolutionLiquid(thrower.worldObj,thrower);
			liquid.setThunderCrap();
			return liquid;
		}
		return null;

	}
	
	public String getSoundString(){
		return this.soundStr;
	}
	
	public StateRandomThrow setSoundString(String par1){
		this.soundStr = par1;
		return this;
	}
	
	public int getInterval(){
		return this.interval;
	}
	
	public StateRandomThrow setInterval(int par1){
		this.interval = par1;
		return this;
	}
}
