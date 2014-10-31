package com.hinasch.unlsaga.debuff;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;

import com.hinasch.lib.PairObject;
import com.hinasch.lib.Statics;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.Ability;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingDebuff;

public class Debuff {

	public final String name;
	protected boolean isDisplayDebuff;
	protected int particle;
	protected List<Ability> abilitiesAgainst;
	protected PairObject<IAttribute,AttributeModifier> attributeModifier;
	
	public final int number;
	
	protected Debuff(int num,String nameEn){
		this.name = nameEn;
		this.number = num;
		this.particle = -1;
		this.abilitiesAgainst = new ArrayList();

		this.isDisplayDebuff = true;
		this.attributeModifier = new PairObject(null,null);
		Unsaga.debuffManager.debuffMap.put(num, this);
	}
	public String toString(){
		return this.name;
	}
	
	public boolean isDisplayDebuff(){
		return this.isDisplayDebuff;
	}
	public LivingDebuff init(String[] strs){
		Debuff debuff = Unsaga.debuffManager.getDebuff(Integer.valueOf(strs[0]));
		int remain = Integer.valueOf(strs[1]); 
		return new LivingDebuff(debuff,remain);
	}
	
	public int getParticleNumber(){
		if(particle!=-1){
			return particle;
		}
		return Statics.getParticleNumber(Statics.particleSpell);
	}
	
	public Debuff setParticleNumber(int par1){
		this.particle = par1;
		return this;
	}
	
	public Debuff setAttributeModifier(IAttribute ia,AttributeModifier par1){
		this.attributeModifier = new PairObject(ia,par1);
		return this;
	}
	
	public AttributeModifier getAttributeModifier(){
		return this.attributeModifier.right;
	}
	
	public IAttribute getAttributeType(){
		return this.attributeModifier.left;
	}
	
	public List<Ability> getAbilityAgainst(){
		return this.abilitiesAgainst;
	}
	
	public Debuff addAbilityAgainst(Ability par1){
		this.abilitiesAgainst.add(par1);
		return this;
	}
	public Debuff setAbilitiesAgainst(List<Ability> par1){
		this.abilitiesAgainst = par1;
		return this;
	}
	

}
