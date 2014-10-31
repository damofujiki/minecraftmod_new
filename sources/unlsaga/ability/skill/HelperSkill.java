package com.hinasch.unlsaga.ability.skill;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import com.hinasch.lib.UtilCollection;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.Ability;
import com.hinasch.unlsaga.ability.HelperAbility;
import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;
import com.hinasch.unlsaga.material.UnsagaMaterial;
import com.hinasch.unlsaga.util.HelperUnsagaItem;

public class HelperSkill extends HelperAbility{

	public boolean isHeavy;

	public HelperSkill(ItemStack is, EntityLivingBase living) {
		super(is, living);
		this.isHeavy = HelperUnsagaItem.isHeavy(is);
		// TODO 自動生成されたコンストラクター・スタブ
	}

//	@Override
//	public void gainSomeAbility(Random rand){
//		if(abilities.getSkillList(category,this.isHeavy ).isPresent()){
//			
//			EntityPlayer ep = null;
//			if(this.owner instanceof EntityPlayer){
//				ep = (EntityPlayer) this.owner;
//			}
//
//			List<Ability> abList = this.getAbilityList();
//
//			if(!abList.isEmpty() && abList!=null){
//				int numgain = this.getRandomIndex(rand, abList.size());
//
//				Ability gainab = abList.get(numgain);
//				Unsaga.debug(gainab.getName(1)+"を覚えた");
//
//				String formatted = this.getGainMessage(gainab);
//
//
//
//				this.sendMessage(ep, gainab);
//
//				int repaircost = this.is.getRepairCost();
//				this.is.setRepairCost(repaircost+2);
//				this.addAbility(gainab);
//			}
//		}
//	}


	@Override
	public boolean isAbilityApplicable(Ability ability){
		if(Unsaga.abilityManager.getSkillList(category,this.isHeavy).isPresent()){
			if(Unsaga.abilityManager.getSkillList(category, this.isHeavy).get().contains(ability)){
				return true;
			}
		}
		return false;
	}
	@Override
	public List<Ability> prepareAbilityList(){
		if(HelperAbility.getGainedAbilitiesInteger(this.is).isPresent()){
			List<Integer> gainedAbilityList = HelperAbility.getGainedAbilitiesInteger(this.is).get();
			if(gainedAbilityList.size()>=this.maxAbilitySize){
				return null;
			}
			List<Integer> baseList = abilities.toIntegerList(abilities.getSkillList(category, this.isHeavy).get());
			return abilities.exchangeIntListToAbilityList(UtilCollection.getListExceptList(baseList, gainedAbilityList));


		}else{
			return abilities.getSkillList(category,this.isHeavy).get();

		}
	}

	@Override
	public boolean existAbilities(ToolCategory category,UnsagaMaterial material){
		return abilities.getSkillList(category,this.isHeavy ).isPresent();
	}

}
