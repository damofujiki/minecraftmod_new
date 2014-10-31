package com.hinasch.unlsaga.debuff.state;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.debuff.Debuff;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingDebuff;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingState;


public class StateHurricane extends State {

	public StateHurricane(int num, String nameEn) {
		super(num, nameEn);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public LivingDebuff init(String[] strs){
		Debuff debuff = Unsaga.debuffManager.getDebuff(Integer.valueOf(strs[0]));
		int remain = Integer.valueOf(strs[1]);
		boolean isOnlyAir = false;
		if(strs.length>2){
			isOnlyAir = (Integer.valueOf(strs[2])==0 ? true :false);
		}
		return new LivingState(debuff,remain,isOnlyAir);
	}

}
