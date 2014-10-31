package com.hinasch.unlsaga.ability.skill.effect;

import com.hinasch.lib.XYZPos;
import com.hinasch.unlsaga.ability.HelperAbility;
import com.hinasch.unlsaga.ability.skill.Skill;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SkillMelee extends SkillBase{

	public static enum Type  {RIGHTCLICK,USE,ENTITY_LEFTCLICK,STOPPED_USING};
	protected final Type skillType;
	protected Skill skill;
	protected boolean requirePrepare;
	protected boolean requireSneak;
	
	public SkillMelee(Type type){
		this.skillType = type;
		this.requirePrepare = false;
		this.requireSneak = true;
	}
	public boolean canInvoke(World world,EntityPlayer ep,ItemStack is,XYZPos pos){

		if((this.requireSneak && ep.isSneaking()) || !this.requireSneak){
			if(HelperAbility.hasAbilityFromItemStack(skill, is)){
				return true;
			}
		}

		return false;
	}
	

	
	public void prepareSkill(InvokeSkill parent){
		
	}
	@Override
	public void invokeSkill(InvokeSkill parent) {
		
		
	}
	
	public Skill getSkill(){
		return this.skill;
	}

	public Type getType(){
		return this.skillType;
	}
	
	public boolean isRequirePrepare(){
		return this.requirePrepare;
	}
	
	//技のタイミングが別のところである場合使う
	public SkillMelee setRequirePrepare(boolean par1){
		this.requirePrepare = par1;
		return this;
	}
	
	public void associateWithSkill(Skill par1){
		this.skill = par1;
	}
	
	public SkillMelee setRequireSneak(boolean par1){
		this.requireSneak = par1;
		return this;
	}
	
	public boolean hasFinishedPrepare(InvokeSkill parent){
		return parent.hasPrepared();
	}
}
