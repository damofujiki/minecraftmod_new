package com.hinasch.unlsaga.debuff.livingdebuff;

import java.util.Random;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.debuff.Debuff;
import com.hinasch.unlsaga.debuff.state.StateRandomThrow;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class LivingStateRandomThrow extends LivingState{

	public int shoottick;
	public int amp;
	public StateRandomThrow parentDebuff;
	public int interval;

	public LivingStateRandomThrow(Debuff par1, int par2, int shoottick,int amp) {
		super(par1, par2, false);
		this.shoottick = shoottick;
		this.amp = amp;
		this.parentDebuff = (StateRandomThrow)par1;

		this.interval = 0;

	}

	@Override
	public String toString(){
		return this.buildSaveString(this.debuff.number,this.remain,this.shoottick,this.amp);
	}

	@Override
	public void updateTick(EntityLivingBase living) {
		Unsaga.debug("呼ばれてますtick");

		if(this.shoottick<0){
			this.remain = 0;
		}

		if(this.interval > this.parentDebuff.getInterval()){
			this.interval = 0;
			//EntityPlayer ep = (EntityPlayer)living;
			if(!this.parentDebuff.getSoundString().equals("")){
				living.worldObj.playSoundAtEntity(living, this.parentDebuff.getSoundString(),1.0F, 1.0F / (living.worldObj.rand.nextFloat() * 0.4F + 1.2F) + 1.0F * 0.5F);
			}

			Random rand = living.worldObj.rand;
			double dx = rand.nextInt(3) + rand.nextDouble();
			double dy = rand.nextInt(3) + rand.nextDouble();
			double dz = rand.nextInt(3) + rand.nextDouble();
			Entity var8 = this.parentDebuff.getThrowingEntity(this,living, dx, dy, dz);
			if (var8!=null && !living.worldObj.isRemote)
			{
				living.worldObj.spawnEntityInWorld(var8);
			}

			shoottick -=1;
		}
		this.interval += 1;
	}



}
