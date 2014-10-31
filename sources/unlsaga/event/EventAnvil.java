package com.hinasch.unlsaga.event;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.HelperAbility;
import com.hinasch.unlsaga.ability.IAbility;
import com.hinasch.unlsaga.ability.skill.HelperSkill;
import com.hinasch.unlsaga.ability.skill.Skill;
import com.hinasch.unlsaga.item.IUnsagaMaterialTool;
import com.hinasch.unlsaga.item.misc.ItemSkillBook;
import com.hinasch.unlsaga.util.HelperUnsagaItem;
import com.hinasch.unlsaga.villager.smith.MaterialInfo;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventAnvil {

	MaterialInfo info;

	@SubscribeEvent
	public void onAnvilUpdate(AnvilUpdateEvent e){
		Unsaga.debug(e.output);
		//技書の処理
		if(e.right!=null && e.left!=null){
			if(e.right.getItem() instanceof ItemSkillBook && (HelperAbility.canItemStackGainAbility(e.left))){
				if(ItemSkillBook.readSkill(e.right).isPresent()){
					e.cost = 5;
					Skill skill = (Skill) ItemSkillBook.readSkill(e.right).get();
					
					
					
					if(Unsaga.abilityManager.isSkillSuited(skill,((IUnsagaMaterialTool)e.left.getItem()).getCategory() , HelperUnsagaItem.isHeavy(e.left))){
						HelperSkill hsLeft = new HelperSkill(e.left, null);
						if(hsLeft.getGainedAbilitiesAmount()==0){
							e.output = e.left.copy();
							HelperSkill hs = new HelperSkill(e.output,null);

							hs.addAbility(skill);
						}

					}

				}
			}
		}
		//技がついてるとコストがあがる
		if(e.output!=null){
			Unsaga.debug(e.output);
			if(e.output.getItem() instanceof IAbility){
				HelperAbility ha = new HelperAbility(e.output, null);
				Unsaga.debug(ha.getGainedAbilitiesAmount());
				if(ha.getGainedAbilitiesAmount()>0){
					
					e.cost += ha.getGainedAbilitiesAmount();
				}
			}
		}
//		int cost = e.cost;
//		if(e.left!=null & e.right!=null){
//			if(!(e.left.getItem() instanceof IUnsagaMaterial))return;
//			if(e.left.getItem().isRepairable()){
//				info = new MaterialInfo(e.right);
//				UnsagaMaterial m1 = null;
//				UnsagaMaterial m2 =  null;
//				if(info.getMaterial().isPresent()){
//					m1 = HelperUnsagaItem.getMaterial(e.left);
//					m2 = info.getMaterial().get();					
//
//					Unsaga.debug(m1,m2);
//
//				}
//				if(e.left.getItem()==e.right.getItem()){
//					m1 = HelperUnsagaItem.getMaterial(e.left);
//					m2 = HelperUnsagaItem.getMaterial(e.right);
//					
//					int damage = (int) Math.min(Math.floor(getItemDurability(e.left)+getItemDurability(e.right)+(e.left.getMaxDamage()/20)),e.left.getMaxDamage());
//					
//				}
//				if((m1!=null && m2!=null) && m1==m2){
//					e.output = e.left;
//					e.cost = cost;
//				}
//			}
//		}

	}
	
	public int getItemDurability(ItemStack is){
		return is.getMaxDamage() - is.getItemDamage();
	}
}
