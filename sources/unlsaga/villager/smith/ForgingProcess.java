package com.hinasch.unlsaga.villager.smith;

import java.util.Map;
import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

import com.google.common.base.Optional;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.HelperAbility;
import com.hinasch.unlsaga.ability.skill.HelperSkill;
import com.hinasch.unlsaga.inventory.container.ContainerSmithUnsaga;
import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;
import com.hinasch.unlsaga.item.armor.ItemAccessory;
import com.hinasch.unlsaga.material.UnsagaMaterial;
import com.hinasch.unlsaga.util.HelperUnsagaItem;
import com.hinasch.unlsaga.villager.smith.ValidPayment.EnumPayValues;

public class ForgingProcess {

	public static final String wellDone = "well";
	public static final String failed = "failed";
	public static final String normal = "good";
	public static enum EnumWorkResult {WELL,FAILED,GOOD};
	
	protected EntityPlayer owner;
	protected UnsagaMaterial materialForged;
	protected MaterialInfo baseInfo;
	protected MaterialInfo subInfo;
	protected int damageForged;
	protected UnsagaMaterial baseMaterial;
	protected UnsagaMaterial subMaterial;
	protected int damageBase;
	protected int damageSub;
	protected ToolCategory categoryForge;
	protected Random rand;
	protected Optional<Map> newenchantMap;
	protected int weightForged;
	protected String smithType;
	
	public ForgingProcess(EntityPlayer ep,ToolCategory category,MaterialInfo base,MaterialInfo sub,Random rand,String smithType){
		this.owner = ep;
		this.categoryForge = category;
		this.baseInfo = base;
		this.subInfo = sub;
		this.baseMaterial = base.getMaterial().get();
		this.subMaterial = sub.getMaterial().get();
		this.rand = rand;
		this.newenchantMap = Optional.absent();
		this.smithType = smithType;
	}
	public ItemStack getForgedItemStack(){
		ItemStack newstack = Unsaga.items.getItemStack(this.categoryForge, this.materialForged, 1, this.damageForged);
		HelperUnsagaItem.initWeapon(newstack, this.materialForged.name, this.weightForged);
		if(this.newenchantMap.isPresent()){
			EnchantmentHelper.setEnchantments(newenchantMap.get(), newstack);
		}
		int maxdamage = newstack.getMaxDamage();
		int newdamage = maxdamage -1;
		newdamage = newdamage - this.damageForged;
		if(newdamage<0)newdamage = 0;
		newstack.setItemDamage(newdamage);
		return newstack;
	}
	
	public boolean isChangeNewCategory(){
		if(this.categoryForge!=this.baseInfo.getCategory()){
			return true;
		}
		return false;
	}
	public void doForge(EnumPayValues pay){
		this.decideForgedMaterial();
		this.calcForgedDamage();
		this.prepareTransplantEnchant(pay);
		this.calcForgedWeight();
	}
	protected void calcForgedWeight(){
		int forged = 0;
		int baseweight = this.baseInfo.getWeight();
		int subweight = this.subInfo.getWeight();
		forged = subweight - baseweight;
		Unsaga.debug(forged+":sub:"+subweight+"baseweight:"+baseweight);
		this.weightForged = baseweight += MathHelper.clamp_int(forged, -2, +2);
		this.weightForged = MathHelper.clamp_int(this.weightForged, 0, 20);
	}

	protected void decideForgedMaterial(){
		Optional<UnsagaMaterial> transformed = MaterialTransform.drawTransformed(owner,baseMaterial, subMaterial, rand);
		if(transformed.isPresent()){
			this.materialForged = transformed.get();
			if(!Unsaga.items.isForgeMaterial(this.categoryForge, this.materialForged)){
				this.materialForged = this.baseMaterial;
			}
		}else{
			this.materialForged = this.baseMaterial;
		}
		
	}
	
	protected void calcForgedDamage(){
		this.damageForged = baseInfo.getPositiveDamage().get() + subInfo.getPositiveDamage().get();
		if(this.smithType==ContainerSmithUnsaga.SMITH_TYPE_REPAIR_PRO){
			int additional = (int)((float)this.damageForged * 0.2F);
			this.damageForged += additional;
		}
		Unsaga.debug(this.damageForged);
	}
	

	//エンチャントの移植
	protected void prepareTransplantEnchant(EnumPayValues pay){
		
		Map newMap = EnchantmentHelper.getEnchantments(this.baseInfo.is);
		if(newMap.isEmpty()){
			this.newenchantMap = Optional.absent();
			return;
		}
		this.newenchantMap = Optional.of(newMap);
		switch(pay){
		case HIGH:break;
		case MID:
			this.forgotSomeEnchant(25);
			break;
		case LOW:
			this.forgotSomeEnchant(60);
			break;
		}
		
	}
	
	//アビリティの移植
	public EnumWorkResult transplantAbilities(ItemStack forged,EntityVillager smith,EnumPayValues pay){
		if(!HelperAbility.canItemStackGainAbility(forged) || !HelperAbility.canItemStackGainAbility(this.baseInfo.is)){
			return EnumWorkResult.GOOD;
		}
		HelperAbility helperBaseItem = new HelperAbility(this.baseInfo.is,smith);
		HelperAbility helperForged;
		if(forged.getItem() instanceof ItemAccessory){
			helperForged = new HelperAbility(forged,smith);
		}else{
			helperForged = new HelperSkill(forged,smith);
		}
		
		int additional = 0;
		if(helperBaseItem.getGainedAbilities().isPresent() && !this.isChangeNewCategory()){
			helperForged.setAbilityListToNBT(helperBaseItem.getGainedAbilities().get());
		}
		if(helperBaseItem.getGainedAbilities().isPresent() && this.isChangeNewCategory()){
			additional += 10;
		}
		EnumWorkResult rt = EnumWorkResult.GOOD;
		Random rand = new Random();

		if(this.smithType==ContainerSmithUnsaga.SMITH_TYPE_ADD_ABILTY){
			additional += 10;
		}
		switch(pay){
		case HIGH:
			if(rand.nextInt(100)<20 + additional){
				Unsaga.debug("引き出せる",this.getClass());
				helperForged.gainSomeAbility(rand);
				rt = EnumWorkResult.WELL;
			}
			
			break;
			
		case MID:
			if(rand.nextInt(100)<8 && additional!=0){
				Unsaga.debug("引き出せる",this.getClass());
				helperForged.gainSomeAbility(rand);
				rt = EnumWorkResult.WELL;
			}else{
				rt = helperForged.forgetRandomAbilityByChance(rand, 25 - additional) ? EnumWorkResult.FAILED : EnumWorkResult.GOOD;

			}
			break;
		case LOW:
			rt = helperForged.forgetRandomAbilityByChance(rand, 60 - additional) ? EnumWorkResult.FAILED : EnumWorkResult.GOOD;
			break;
		
		
		}
		return rt;
	}
	

	
	protected void forgotSomeEnchant(int prob){
		if(this.rand.nextInt(100)<=prob){
			this.newenchantMap = Optional.absent();
		}
	}
}
