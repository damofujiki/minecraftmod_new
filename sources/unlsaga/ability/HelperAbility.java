package com.hinasch.unlsaga.ability;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

import com.google.common.base.Optional;
import com.hinasch.lib.CSVText;
import com.hinasch.lib.UtilCollection;
import com.hinasch.lib.UtilNBT;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.skill.HelperSkill;
import com.hinasch.unlsaga.event.extendeddata.ExtendedPlayerData;
import com.hinasch.unlsaga.item.IUnsagaMaterialTool;
import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;
import com.hinasch.unlsaga.material.UnsagaMaterial;
import com.hinasch.unlsaga.network.packet.PacketSound;
import com.hinasch.unlsaga.util.ChatMessageHandler;
import com.hinasch.unlsaga.util.HelperUnsagaItem;
import com.hinasch.unlsaga.util.translation.Translation;

public class HelperAbility {


	protected AbilityRegistry abilities = Unsaga.abilityManager;
	protected ItemStack is;
	protected ToolCategory category;
	protected int maxAbilitySize;
	protected UnsagaMaterial material;
	protected static String KEY = "abilities";
	protected EntityLivingBase owner;

	public HelperAbility (ItemStack is,EntityLivingBase living){
		this.owner = living;
		this.is = is;
		IUnsagaMaterialTool iu = (IUnsagaMaterialTool)is.getItem();
		this.category = iu.getCategory();
		this.material = HelperUnsagaItem.getMaterial(is);
		this.maxAbilitySize = ((IAbility)is.getItem()).getMaxAbility();
	}

	public void gainSomeAbility(Random rand){
		Unsaga.debug(owner);
		Unsaga.debug(abilities.getAbilities(category, material));
		if(this.existAbilities(category, material)){
			Unsaga.debug("ikeru",this.getClass());
			
			EntityPlayer player = null;
			if(this.owner instanceof EntityPlayer){
				player = (EntityPlayer)owner;
			}

			List<Ability> abList = this.prepareAbilityList();



			Unsaga.debug("ok");
			if(abList!=null && abList.isEmpty()){
				int numgain =abList.size() == 1?0:rand.nextInt(abList.size()); //this.getRandomIndex(rand, abList.size());

				Ability gainab = abList.get(numgain);
				Unsaga.debug(gainab.getName(1)+"を覚えた:"+this.owner);
				//msg.gained.ability


				this.sendMessage(player,gainab);

				
				int repaircost = this.is.getRepairCost();
				this.is.setRepairCost(repaircost+2);
				this.addAbility(gainab);
				
			}
		}


	}


	public void sendMessage(EntityPlayer player,Ability gainab){
		String mes = this.getGainMessage(gainab);
		if(player!=null && !player.worldObj.isRemote){
			ChatMessageHandler.sendChatToPlayer(player, mes);		
			PacketSound ps = new PacketSound(1022);
			Unsaga.packetPipeline.sendTo(ps, (EntityPlayerMP) player);
		}
		if(this.owner instanceof EntityVillager){
			if(((EntityVillager) this.owner).getCustomer()!=null){
				EntityPlayer customer = ((EntityVillager) this.owner).getCustomer();
				ChatMessageHandler.sendChatToPlayer(customer, mes);
				
				PacketSound ps = new PacketSound(1022);
				Unsaga.packetPipeline.sendTo(ps, (EntityPlayerMP) customer);
			}
		}
	}
	
	public List<Ability> prepareAbilityList(){
		if(getGainedAbilitiesInteger(this.is).isPresent()){
			List<Integer> gainedAbilityList = getGainedAbilitiesInteger(this.is).get();
			if(gainedAbilityList.size()>=this.maxAbilitySize){
				return null;
			}
			List<Integer> baseList = abilities.toIntegerList(abilities.getAbilities(category, material).get());
			//すでに覚えた技・アビリティは省く
			return abilities.exchangeIntListToAbilityList(UtilCollection.getListExceptList(baseList, gainedAbilityList));


		}else{
			return abilities.getAbilities(category, material).get();

		}
	}
	public boolean existAbilities(ToolCategory category,UnsagaMaterial material){
		return abilities.getAbilities(category, material).isPresent();
	}
	
	
	protected String getGainMessage(Ability abilityGained){
		String mesbase = "";
		if(this instanceof HelperSkill){
			if(this.owner instanceof EntityVillager){
				mesbase = Translation.localize("msg.gained.ability.smith");
			}else{
				mesbase = Translation.localize("msg.gained.skill");
			}
			
		}else{
			mesbase = Translation.localize("msg.gained.ability");
		}

		String mes = String.format(mesbase, abilityGained.getName(Translation.getLang()));
		return mes;
	}
	public void drawChanceToGainAbility(Random rand,int par1){
		int prob = MathHelper.clamp_int(par1, 0, 100);
		if(rand.nextInt(100)<=prob){
			this.gainSomeAbility(rand);
		}
	}

	public void drawChanceToGainAbility(Random rand,Entity enemy){
		int prob = GainProbFromEnemy.getProbGainFromEntity(enemy);
		this.drawChanceToGainAbility(rand, prob);
	}

	public void addAbility(Ability ab){
		if(UtilNBT.hasKey(is, KEY)){
			List<Integer> gainedList = getGainedAbilitiesInteger(this.is).get();
			gainedList.add(ab.number);
			this.setAbilitiesToNBT(gainedList);
		}else{
			List<Integer> newList = new ArrayList();
			newList.add(ab.number);
			this.setAbilitiesToNBT(newList);
		}
	}

	public void forgetRandomAbility(Random rand){
		if(getGainedAbilitiesInteger(this.is).isPresent()){
			List<Ability> ablist = abilities.exchangeIntListToAbilityList(getGainedAbilitiesInteger(this.is).get());
			int indexforgot =ablist.size() == 1?0 : rand.nextInt(ablist.size());
			ablist.remove(indexforgot);
			if(ablist.isEmpty()){
				this.removeAbilityTag();
			}else{
				this.setAbilityListToNBT(ablist);
			}

		}
	}
	
	public boolean forgetRandomAbilityByChance(Random rand,int prob){
		if(rand.nextInt(100)<prob){
			forgetRandomAbility(rand);
			return true;
		}
		return false;
	}

	public void removeAbilityTag(){
		if(UtilNBT.hasKey(is, KEY)){
			UtilNBT.removeTag(is, KEY);
		}
	}

	protected void setAbilitiesToNBT(List<Integer> input){
		if(input!=null && !input.isEmpty()){
			UtilNBT.setFreeTag(is, KEY, CSVText.intListToCSV(input));
		}
	}

	public void setAbilityListToNBT(List<Ability> input){
		if(input!=null && !input.isEmpty()){
			List<Integer> newlist = abilities.toIntegerList(input);
			this.setAbilitiesToNBT(newlist);
		}
	}

	public boolean hasAbility(Ability ab){
		if(getGainedAbilitiesInteger(this.is).isPresent()){
			return getGainedAbilitiesInteger(this.is).get().contains(ab.number);
		}
		if(Unsaga.abilityManager.getInheritAbilities(this.category, this.material).isPresent()){
			return Unsaga.abilityManager.getInheritAbilities(this.category, this.material).get().contains(ab);
		}
		return false;
	}

	public static boolean hasAbilityFromItemStack(Ability ab,ItemStack is){

		if(getGainedAbilitiesInteger(is).isPresent()){
			return getGainedAbilitiesInteger(is).get().contains(ab.number);
		}
		if(is.getItem() instanceof IUnsagaMaterialTool){
			UnsagaMaterial us = HelperUnsagaItem.getMaterial(is);
			ToolCategory category = ((IUnsagaMaterialTool)is.getItem()).getCategory();
			if(Unsaga.abilityManager.getInheritAbilities(category, us).isPresent()){
				return Unsaga.abilityManager.getInheritAbilities(category, us).get().contains(ab);
			}
		}

		return false;
	}

	public static Optional<List<Integer>> getGainedAbilitiesInteger(ItemStack is){
		if(UtilNBT.hasKey(is, KEY)){
			List<Integer> intlist = CSVText.csvToIntList(UtilNBT.readFreeStrTag(is, KEY));
			if(intlist!=null){
				return Optional.of(intlist);
			}

		}
		return Optional.absent();
	}



	public static boolean canItemStackGainAbility(ItemStack is){
		if(is!=null){
			if(is.getItem() instanceof IAbility && is.getItem() instanceof IUnsagaMaterialTool){
				return true;
			}
		}
		return false;
	}

	public int getGainedAbilitiesAmount(){
		if(this.getGainedAbilities().isPresent()){
			return this.getGainedAbilities().get().size();
		}
		return 0;
	}
	public Optional<List<Ability>> getGainedAbilities(){
		if(getGainedAbilitiesInteger(this.is).isPresent()){
			List<Integer> list = getGainedAbilitiesInteger(this.is).get();
			return Optional.of(abilities.exchangeIntListToAbilityList(list));
		}
		return Optional.absent();
	}



	public boolean isAbilityApplicable(Ability ability){
		if(Unsaga.abilityManager.getAbilities(category, material).isPresent()){
			if(Unsaga.abilityManager.getAbilities(category, material).get().contains(ability)){
				return true;
			}
		}

		return false;
	}
	public int getHealAbilitiesAmount(){
		List<Ability> list = new ArrayList();
		int healAmount = 0;
		if(this.getGainedAbilities().isPresent()){
			list = this.getGainedAbilities().get();
			for(Ability ab:Unsaga.abilityManager.healUps){
				if(list.contains(ab)){
					healAmount += ab.healPoint;
				}
			}
			for(Ability ab:Unsaga.abilityManager.healDowns){
				if(list.contains(ab)){
					healAmount += ab.healPoint;
				}
			}
			//Unsaga.debug("計算:"+healAmount);
		}
		return healAmount;
	}

	public static int getAmountAbility(EntityLivingBase el,Ability ability){
		int amount = 0;
		if(el instanceof EntityPlayer){
			EntityPlayer ep = (EntityPlayer)el;
			if(ep.getExtendedProperties(ExtendedPlayerData.KEY)!=null){
				ExtendedPlayerData data = (ExtendedPlayerData)ep.getExtendedProperties(ExtendedPlayerData.KEY);
				for(ItemStack is:data.getAccessories()){
					if(is!=null && is.getItem() instanceof IAbility){
						HelperAbility helper = new HelperAbility(is,ep);
						if(helper.hasAbility(ability)){
							amount += 1;
						}
					}
				}
			}
			for(ItemStack armor:ep.inventory.armorInventory){
				if(armor!=null && armor.getItem() instanceof IAbility){
					HelperAbility helper = new HelperAbility(armor,ep);
					if(helper.hasAbility(ability)){
						amount += 1;
					}
				}
			}


			ItemStack held = ep.getHeldItem();
			if(held!=null && HelperAbility.hasAbilityFromItemStack(ability, held)){
				amount += 1;
			}

		}else{
			//ItemStack currentitem = el.getCurrentItemOrArmor(0);
			//if(currentitem!=null && HelperAbility.hasAbilityFromItemStack(ability, currentitem))amount +=1;
			for(int i=0;i<5;i++){
				ItemStack is = el.getEquipmentInSlot(i);
				if(is!=null && HelperAbility.hasAbilityFromItemStack(ability, is))amount +=1;
			}
		}

		return amount;
	}
}
