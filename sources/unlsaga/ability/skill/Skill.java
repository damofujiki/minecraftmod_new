package com.hinasch.unlsaga.ability.skill;

import java.util.Map;

import com.hinasch.unlsaga.ability.Ability;
import com.hinasch.unlsaga.ability.skill.effect.SkillBase;
import com.hinasch.unlsaga.ability.skill.effect.SkillMelee;
import com.hinasch.unlsaga.util.damage.DamageHelper;

public class Skill extends Ability{

	//public static enum EnumDamageUnsaga {SWORD,PUNCH,SPEAR,SWORDPUNCH};
	protected float hurtHp;
	protected float hurtLp;
	protected int damageWeapon;
	public DamageHelper.Type damageType;
	public SkillBase effect;
	
	
	public Skill(int num, String par1, String par2,int hp,int lp,DamageHelper.Type type,int damage) {
		super(num, par1, par2);
		this.hurtHp = hp;
		this.hurtLp = lp;
		this.damageType = type;
		this.damageWeapon = damage;
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	public Skill addToMap(Map<Integer,Ability> map){
		map.put(this.number, this);
		return this;
	}
	
	public float getStrHurtHP(){
		return this.hurtHp;
		
	}
	
	public float getStrHurtLP(){
		return this.hurtLp;
	}
	
	public int getDamageForWeapon(){
		return this.damageWeapon;
	}
	
	public DamageHelper.Type getDamageType(){
		return this.damageType;
	}
	
	public Skill setSkillEffect(SkillBase par1){
		this.effect = par1;
		if(par1 instanceof SkillMelee){
			((SkillMelee) par1).associateWithSkill(this);
		}
		return this;
	}
	
	public SkillBase getSkillEffect(){
		return this.effect;
	}

}
