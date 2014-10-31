package com.hinasch.unlsaga.debuff;

import com.hinasch.lib.Statics;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingBuff;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingDebuff;

public class Buff extends Debuff{

	
	
	protected Buff(int num, String nameEn) {
		super(num, nameEn);
		this.isDisplayDebuff = true;
		// TODO 自動生成されたコンストラクター・スタブ
	}

	
	@Override
	public LivingDebuff init(String[] strs){
		Debuff debuff = Unsaga.debuffManager.getDebuff(Integer.valueOf(strs[0]));
		int remain = Integer.valueOf(strs[1]); 
		int amp = 0;
		if(strs.length>2){
			amp = Integer.valueOf(amp);
		}
		return new LivingBuff(debuff,remain,amp);
	}
	
	@Override
	public int getParticleNumber(){
		if(particle!=-1){
			return particle;
		}
		return Statics.getParticleNumber(Statics.particleMobSpell);
	}
	
	
	@Override
	public Debuff setParticleNumber(int par1){
		this.particle = par1;
		return (Buff)this;
	}
	
	
}
