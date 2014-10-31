package com.hinasch.unlsagamagic.spell.effect;

import com.hinasch.unlsaga.ability.skill.effect.AbstractSkillEffect;

public abstract class SpellBase extends AbstractSkillEffect{


	public SpellBase(){
		
	}

	
	@Override
	public void invoke(Object parent){
		this.invokeSpell((InvokeSpell)parent);
	}
	
	abstract public void invokeSpell(InvokeSpell parent);

}
