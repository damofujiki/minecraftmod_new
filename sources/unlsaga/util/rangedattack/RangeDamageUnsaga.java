package com.hinasch.unlsaga.util.rangedattack;

import net.minecraft.world.World;

import com.hinasch.lib.RangeDamage;
import com.hinasch.unlsaga.ability.skill.effect.InvokeSkill;

public class RangeDamageUnsaga extends RangeDamage{

	
	public InvokeSkill helper;
	
	public RangeDamageUnsaga(World world) {
		super(world);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public RangeDamageUnsaga(World world,InvokeSkill parent) {
		super(world);
		this.helper = parent;
		// TODO 自動生成されたコンストラクター・スタブ
	}
	

}
