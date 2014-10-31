package com.hinasch.unlsaga.debuff.livingdebuff;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.debuff.Debuff;

import net.minecraft.entity.EntityLivingBase;


public class LivingStateTarget extends LivingState{

	public int targetid;
	
	public LivingStateTarget(Debuff par1, int par2,int targetid) {
		super(par1, par2,false);
		this.targetid = targetid;
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void updateRemain(EntityLivingBase living){
		this.remain -= 1;
		if(this.remain<=0){
			this.remain = 0;
		}
		Unsaga.debug(this.toString());
	}
	
	@Override
	public String toString(){
		return this.debuff.number+":"+this.remain + ":" + this.targetid;
	}
}
