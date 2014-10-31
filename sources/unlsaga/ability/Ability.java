package com.hinasch.unlsaga.ability;

import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.event.extendeddata.ExtendedPlayerData;
import com.hinasch.unlsaga.util.damage.DamageHelper;
import com.hinasch.unlsaga.util.damage.DamageSourceUnsaga;

public class Ability {


	public final int number;
	protected final String name;
	protected final String nameJP;



	protected int healPoint;

	public Ability(int num,String par1,String par2){
		name = par1;
		nameJP = par2;
		this.number = num;


		AbilityRegistry.allAbilities.put(num, this);

	}

	public Ability(int num,String par1,String par2,int par3){
		this(num,par1,par2);

		this.healPoint = par3;
	}

	public Ability addToMap(Map<Integer,Ability> map){
		map.put(this.number, this);
		return this;
	}

	public String getName(int en_or_jp){
		if(en_or_jp==0)return name;
		return nameJP;
	}

	public String getName(String lang){
		if(lang==null){
			return this.getName(0);
		}
		if(lang.equals("ja_JP")){
			return this.getName(1);
		}
		return this.getName(0);
	}

	//use Heal Ability
	public int getHealPoint(){
		return this.healPoint;
	}

	@Override
	public String toString(){
		return String.valueOf(this.number)+":"+name;
	}


	//From LivingUpdate
	public static void abilityEventOnLivingUpdate(EntityLivingBase living,AbilityRegistry abilities){

		int amountHeal = 0;
		if(!living.isPotionActive(Potion.regeneration)){
			if(HelperAbility.getAmountAbility(living, abilities.superHealing)>0){
				living.addPotionEffect(new PotionEffect(Potion.regeneration.id,150,0));
			}
		}

		for(Potion potion:abilities.againstPotionMap.keySet()){
			if(living.isPotionActive(potion)){
				if(HelperAbility.getAmountAbility(living, abilities.againstPotionMap.get(potion))>0){
					living.removePotionEffect(potion.id);
				}
			}
		}
		for(Potion potion:Potion.potionTypes){
			if(potion!=null){
				if(potion.isBadEffect() && living.isPotionActive(potion) && HelperAbility.getAmountAbility(living, abilities.antiDebuff)>0){
					living.removePotionEffect(potion.id);
				}
			}
		}

		if(!living.isPotionActive(Potion.fireResistance)){
			if(HelperAbility.getAmountAbility(living, abilities.fireProtection)>0){
				int amount = HelperAbility.getAmountAbility(living, abilities.fireProtection);
				living.addPotionEffect(new PotionEffect(Potion.fireResistance.id,150,0));
			}
		}

//		if(living instanceof EntityPlayer){
//
//
//			ExtendedPlayerData pdata = ExtendedPlayerData.getData((EntityPlayer) living);
//
//			for(ItemStack is:pdata.getAccessories()){
//				if(is!=null){
//					if(is.getItem() instanceof IUnsagaMaterialTool){
//						IUnsagaMaterialTool im = (IUnsagaMaterialTool)is.getItem();
//						UnsagaMaterial material = HelperUnsagaItem.getMaterial(is);
//						amountHeal += abilities.getInheritHealAmount(im.getCategory(), material, abilities.healUps);
//						amountHeal += abilities.getInheritHealAmount(im.getCategory(), material, abilities.healDowns);
//					}
//					if(is.getItem() instanceof IAbility){
//						HelperAbility helper = new HelperAbility(is,living);
//						amountHeal += helper.getHealAbilitiesAmount();
//					}
//				}
//			}
//
//
//
//		}
//		for(int i=1;i<5;i++){
//			ItemStack armor = living.getEquipmentInSlot(i);
//			if(armor!=null){
//				if(armor.getItem() instanceof IUnsagaMaterialTool){
//					IUnsagaMaterialTool im = (IUnsagaMaterialTool)armor.getItem();
//					UnsagaMaterial material = HelperUnsagaItem.getMaterial(armor);
//					amountHeal += abilities.getInheritHealAmount(im.getCategory(), material, abilities.healUps);
//					amountHeal += abilities.getInheritHealAmount(im.getCategory(), material, abilities.healDowns);
//				}
//				if(armor.getItem() instanceof IAbility){
//					HelperAbility helper = new HelperAbility(armor,living);
//					amountHeal += helper.getHealAbilitiesAmount();
//				}
//			}
//		}


		//		if(amountHeal<0){
		//			if(living.ticksExisted % 20 * 12 == 0){
		//				if(living instanceof EntityPlayer){
		//					((EntityPlayer) living).addExhaustion(0.025F * (float)(Math.abs(amountHeal) + 1));
		//				}
		//				
		//			}
		//		}
		//		if(amountHeal>0){
		//			if(living.ticksExisted % 20 * 12 == 0){
		//				if(living instanceof EntityPlayer){
		//					if(!((EntityPlayer) living).getFoodStats().needFood()){
		//						living.heal(amountHeal);
		//					}
		//				}else{
		//					living.heal(amountHeal);
		//				}
		//
		//
		//			}
		//		}
		if(living.ticksExisted % 20 * 12 == 0){
			//Unsaga.debug(amountHeal);
		}
	}

	public static void abilityEventOnLivingHurt(LivingHurtEvent e,DamageSourceUnsaga dmUnsaga){
		EntityLivingBase hurtEntity = e.entityLiving;
		Entity attacker = e.source.getEntity();
		AbilityRegistry ar = Unsaga.abilityManager;

		if(e.source.isExplosion()){
			if(HelperAbility.getAmountAbility(e.entityLiving, ar.blastProtection)>0){
				e.ammount = e.ammount / (2+HelperAbility.getAmountAbility(e.entityLiving, ar.blastProtection));
				e.ammount = MathHelper.clamp_float(e.ammount, 1.0F, 10.0F);
				Unsaga.debug("爆発防御！");
			}
		}

		if(HelperAbility.getAmountAbility(hurtEntity, ar.lifeGuard)>0){
			int amount = HelperAbility.getAmountAbility(hurtEntity, ar.lifeGuard);
			if(e.entityLiving.getRNG().nextInt(3)==0){
				e.ammount -= (float)amount;
				e.ammount = MathHelper.clamp_float(e.ammount, 1.0F, 100.0F);
				Unsaga.debug("ライフ防御！");
			}
		}

		if(e.source.isProjectile()){
			if(HelperAbility.getAmountAbility(hurtEntity, ar.projectileProtection)>0){
				e.ammount = e.ammount / (2+HelperAbility.getAmountAbility(hurtEntity, ar.projectileProtection));
				e.ammount = MathHelper.clamp_float(e.ammount, 1.0F, 10.0F);
				Unsaga.debug("飛び道具防御！");
			}
		}

		if(dmUnsaga!=null){
			if(dmUnsaga.damageType==DamageHelper.Type.SWORD){
				e.ammount -= (e.ammount * 0.2F);
			}
		}
		
	}
	//On LivingDeathEvent
	public static void gainAbilityEventOnLivingDeath(LivingDeathEvent e,EntityLivingBase enemy){
		Unsaga.debug("呼ばれてる");
		EntityPlayer ep = (EntityPlayer)e.source.getEntity();


		ExtendedPlayerData pdata = ExtendedPlayerData.getData(ep);
		for(int i=0;i<2;i++){
			ItemStack is = pdata.getAccessory(i);
			if(HelperAbility.canItemStackGainAbility(is)){
				Unsaga.debug("アビリティを覚えられる");
				HelperAbility helper = new HelperAbility(is,ep);
				if(!ep.worldObj.isRemote){
					helper.drawChanceToGainAbility(ep.getRNG(), enemy);
				}

			}


		}
		for(ItemStack armor:ep.inventory.armorInventory){
			if(armor!=null){
				if(HelperAbility.canItemStackGainAbility(armor)){
					HelperAbility helper = new HelperAbility(armor,ep);
					if(!ep.worldObj.isRemote){
						helper.drawChanceToGainAbility(ep.getRNG(), enemy);
					}
				}
			}
		}
	}
}
