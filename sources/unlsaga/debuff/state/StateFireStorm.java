package com.hinasch.unlsaga.debuff.state;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.debuff.Debuff;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingDebuff;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingStateFireStorm;

public class StateFireStorm extends State{

	public StateFireStorm(int num, String nameEn) {
		super(num, nameEn);
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	@Override
	public LivingDebuff init(String[] strs){
		Debuff debuff = Unsaga.debuffManager.getDebuff(Integer.valueOf(strs[0]));
		int remain = Integer.valueOf(strs[1]);
		boolean isOnlyAir = false;
		int x = 0;
		int y = 0;
		int z = 0;
		int amp = 1;
		int entityid = -1;
		if(strs.length>2){
			x = Integer.valueOf(strs[2]);
			y = Integer.valueOf(strs[3]);
			z = Integer.valueOf(strs[4]);
			amp = Integer.valueOf(strs[5]);
			entityid = Integer.valueOf(strs[6]);
		}
		return new LivingStateFireStorm(debuff,remain,x,y,z,amp,entityid);
	}
}
