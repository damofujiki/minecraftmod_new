package com.hinasch.unlsagamagic.spell;

import java.util.Map;
import java.util.Set;

import com.hinasch.lib.HSLibs;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.util.FiveElements;
import com.hinasch.unlsagamagic.spell.effect.SpellBase;

public class Spell {




	
	protected final int damegeItem;
	protected final int difficultyDecipher;
	protected final int baseProbability;
	protected float hurtLP;
	protected float hurtHP;
	public final FiveElements.Enums element;

	public boolean usePointer;
	public final int number;
	protected SpellBase spellEffect;
	
	public final String nameEn;
	public final String nameJp;
	
	public SpellMixTable elementsTable;
	public SpellMixTable elementsAmp;
	public SpellMixTable elementsCost;
	
	protected Class<? extends SpellBase> spellClass;

	protected Spell(FiveElements.Enums element,int num,String nameEn,String nameJp,int difficulty,int damageItem,int baseProb){
		this.number = num;
		this.nameEn = nameEn;
		this.nameJp = nameJp;
		this.damegeItem = damageItem;
		this.baseProbability = baseProb;
		this.difficultyDecipher = difficulty;
		this.spellEffect = null;
		this.element = element;
		this.usePointer = false;
		

		
	}
	
	public int getDifficultyDecipher(){
		return this.difficultyDecipher;
	}
	
	public int getBaseProb(){
		return this.baseProbability;
	}
	public int getDamageForItem(){
		return this.damegeItem;
	}
	public float getStrHurtLP(){
		return this.hurtLP;
	}
	
	public float getStrHurtHP(){
		return this.hurtHP;
	}
	
	public Spell addToBlenderSet(Set<SpellBlend> set){
		
		set.add((SpellBlend) this);
		return this;
	}
	
	public Spell addToMap(Map<Integer,Spell> map){
		Unsaga.debug("register Spell "+this.nameJp);
		map.put(this.number, this);
		return this;
	}
	
	public void setSpellMixElements(SpellMixTable table){
		this.elementsTable = table;
	}
	
	public void setSpellAmplifier(SpellMixTable amp,SpellMixTable cost){
		this.elementsAmp = amp;
		this.elementsCost = cost;
	}

	public SpellMixTable getSpellMixElements(){
		return this.elementsTable;
	}
	
	public SpellMixTable getAmp(){
		return this.elementsTable;
	}
	
	public SpellMixTable getCost(){
		return this.elementsTable;
	}
	
	public String getName(String currentLang) {
		if(currentLang.equals(HSLibs.JPKEY)){
			return this.nameJp;
		}
		return this.nameEn;
	}
	
	public Spell setStrength(float hp,float lp){
		this.hurtHP = hp;
		this.hurtLP = lp;
		return this;
	}
	
	public boolean isUsePointer(){
		return this.usePointer;
	}
	
	public Spell setUsePointer(boolean par1){
		this.usePointer = par1;
		return this;
	}
	
	public Spell setSpellEffect(SpellBase par1){
		this.spellEffect = par1;
		return this;
	}
	
	public Spell setSpellClass(Class<? extends SpellBase> spellClass){
		this.spellClass = spellClass;
		return this;
	}
	
	public Class<? extends SpellBase> getSpellClass(){
		return this.spellClass;
	}
	public SpellBase getSpellEffect(){
		return this.spellEffect;
	}
}
