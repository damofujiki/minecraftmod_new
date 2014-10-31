package com.hinasch.unlsaga.debuff.livingdebuff;

import com.hinasch.unlsaga.debuff.Debuff;


public class LivingStateFlyingAxe extends LivingState{

	public int attackDamage;
	
	public LivingStateFlyingAxe(Debuff par1, int par2,int attackdamage) {
		super(par1, par2,true);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	
	@Override
	public String toString(){
		return this.debuff.number+":"+this.remain+":"+this.attackDamage;
	}
	

}
