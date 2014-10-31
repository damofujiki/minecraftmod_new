package com.hinasch.unlsaga.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import com.hinasch.lib.ItemUtil;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.Ability;
import com.hinasch.unlsaga.ability.HelperAbility;
import com.hinasch.unlsaga.ability.skill.effect.InvokeSkill;
import com.hinasch.unlsaga.ability.skill.effect.SkillBow;
import com.hinasch.unlsaga.debuff.Debuffs;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingDebuff;
import com.hinasch.unlsaga.entity.projectile.EntityArrowUnsaga;
import com.hinasch.unlsaga.event.extendeddata.ExtendedDataLP;
import com.hinasch.unlsaga.event.extendeddata.WorldSaveDataUnsaga;
import com.hinasch.unlsaga.item.IUnsagaMaterialTool;
import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;
import com.hinasch.unlsaga.item.panel.SkillPanels;
import com.hinasch.unlsaga.item.weapon.ItemBowUnsaga;
import com.hinasch.unlsaga.item.weapon.ItemSwordUnsaga;
import com.hinasch.unlsaga.util.ChatMessageHandler;
import com.hinasch.unlsaga.util.damage.DamageHelper;
import com.hinasch.unlsaga.util.damage.DamageSourceUnsaga;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventLivingHurt {

	
	boolean flagBowAttack;
	protected DamageSourceUnsaga unsagaDamageSource;
	
	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent e){
		Entity attacker = e.source.getEntity();
		//Unsaga.debug(e.source);
		this.unsagaDamageSource = null;
		if(!(e.source instanceof DamageSourceUnsaga)){
			this.setUnsagaDamage(e, new DamageSourceUnsaga(e.source));
		}
		//this.flagBowAttack = false;
		//ダメージを受けた時のフック（デバフ、バフ、アビリティ、いろいろ）
		this.processOnHurt(e,this.unsagaDamageSource);



		if(e.source.getSourceOfDamage() instanceof EntityArrow || e.source.getSourceOfDamage() instanceof EntityArrowUnsaga){
			this.onArrowHitEvent(e);
		}
		this.onNormalAttackEvent(e);
		
		EntityLivingBase entityHurt = (EntityLivingBase)e.entityLiving;

		if(e.source instanceof DamageSourceUnsaga){
			this.unsagaDamageSource = (DamageSourceUnsaga) e.source;
		}
		if(this.unsagaDamageSource!=null){
			this.resultDamageForUnsagaMod(e);
		}

		
		if(e.entity instanceof EntityLivingBase){
			if(Unsaga.lpManager.isLPEnabled()){
				ExtendedDataLP data = Unsaga.lpManager.getData(e.entityLiving);
				if(data.getLP()>0){
					if(e.entityLiving.getHealth() - e.ammount<1){
						e.ammount = e.entityLiving.getHealth()<=1.0F?0.0F : e.entityLiving.getHealth() - 1.0F;
					}
				}
			}


		}

	}

	public static float getModifiedPunchDamage(EntityPlayer ep){
		int punchLevel = WorldSaveDataUnsaga.getHighestLevelOfPanel(ep.worldObj, ep, Unsaga.skillPanels.punch);
		float amount = 0;
		if(punchLevel>=0){
			amount = (float)(punchLevel + 1) * 1.8F;
		}
		return amount;
	}
	public void processOnHurt(LivingHurtEvent e,DamageSourceUnsaga dsu){
		Debuffs.debuffEventOnLivingHurt(e);
		Ability.abilityEventOnLivingHurt(e,dsu);
		ItemSwordUnsaga.hitExplodeByVandalize(e);
		SkillBow.checkArrowOnLivingHurtEvent(e);
	}

	public void onArrowHitEvent(LivingHurtEvent e){
		if(e.source.getSourceOfDamage() instanceof EntityArrow || e.source.getSourceOfDamage() instanceof EntityArrow){
			if(HelperAbility.getAmountAbility(e.entityLiving, Unsaga.abilityManager.projectileProtection)>0){
				e.ammount = e.ammount / (2+HelperAbility.getAmountAbility(e.entityLiving, Unsaga.abilityManager.projectileProtection));
				e.ammount = MathHelper.clamp_float(e.ammount, 1.0F, 10.0F);
			}
			
			
			EntityLivingBase living = (EntityLivingBase)e.entityLiving;
			if(e.source.getEntity() instanceof EntityPlayer){
				EntityPlayer player = (EntityPlayer)e.source.getEntity();
				
				if(ItemUtil.hasItemInstance(player, ItemBowUnsaga.class) && e.source.getSourceOfDamage() instanceof EntityArrow){
					ItemStack is = player.getHeldItem();
					if(HelperAbility.hasAbilityFromItemStack(Unsaga.abilityManager.exorcist,is) ||HelperAbility.hasAbilityFromItemStack(Unsaga.abilityManager.zapper,is) ){
						InvokeSkill helper = new InvokeSkill(player.worldObj, player, Unsaga.abilityManager.zapper, is);
						helper.setParent(e);
						helper.doSkill();
					}

					
				}

			}
		}
		
		this.setUnsagaDamage(e, DamageHelper.getUnsagaDamage(e.source, ToolCategory.BOW));
		

		e.entityLiving.hurtResistantTime = 0;
	}
	
	public void onNormalAttackEvent(LivingHurtEvent e){
		if(e.source.getEntity() instanceof EntityLivingBase){

			EntityLivingBase attacker = (EntityLivingBase)e.source.getEntity();
			EntityLivingBase damagedEntity = e.entityLiving;
			
//			if(attacker.getHeldItem()!=null && attacker instanceof EntityPlayer){
//				if(HelperAbility.hasAbilityFromItemStack(AbilityRegistry.skullCrash, attacker.getHeldItem())){
//					SkillEffectHelper helper = new SkillEffectHelper(attacker.worldObj,(EntityPlayer) attacker,AbilityRegistry.skullCrash,attacker.getHeldItem());
//					helper.setParent(e);
//					helper.doSkill();
//				}
//			}
			
			Entity sourceent = e.source.getSourceOfDamage();

			ItemStack weapon = attacker.getHeldItem();

			if(weapon!=null && weapon.getItem() instanceof IUnsagaMaterialTool){
				float baseStr = e.ammount;
				IUnsagaMaterialTool iu = (IUnsagaMaterialTool)weapon.getItem();
				//弓関係も実装する
				this.setUnsagaDamage(e, DamageHelper.getUnsagaDamage(e.source,iu.getCategory()));

				

			}
		}
	}
	
	//このmod専用のダメージフック
	public void resultDamageForUnsagaMod(LivingHurtEvent e){
		EntityLivingBase entityHurt = e.entityLiving;
		Entity attacker = e.source.getEntity();
		float lphurt = this.unsagaDamageSource.getStrengthLPHurt();
		if(attacker instanceof EntityPlayer){
			if(((EntityPlayer) attacker).getHeldItem()==null){
				e.ammount += this.getModifiedPunchDamage((EntityPlayer) attacker);
			}
			
		}
		if(entityHurt instanceof EntityLivingBase){
			//e.ammount += this.getDamageModifierFromLP(entityHurt, e.ammount);
		}
		//相性によるダメージ上限
		float modifier = DamageHelper.getDamageModifierFromType(this.unsagaDamageSource.getUnsagaDamageType(), entityHurt, e.ammount);
		float emodifier = DamageHelper.getDamageModifierFromSubType(this.unsagaDamageSource.getSubDamageType(),entityHurt, e.ammount);
		e.ammount += modifier;
		e.ammount += emodifier;
		e.ammount = MathHelper.clamp_float(e.ammount, 0.0F, 1000.0F);
		Unsaga.debug(modifier,emodifier,this.getClass().getName());
		//if(Unsaga.configs.enableLP){

		//恐怖症、ここでなくていいかも
		SkillPanels.onLivingHurt(e);

		boolean isPoison = false;

		if(attacker instanceof EntityLivingBase){
			if(LivingDebuff.hasDebuff((EntityLivingBase) attacker, Unsaga.debuffManager.downSkill)){
				lphurt *= 0.6F;
			}

			if(LivingDebuff.hasDebuff(entityHurt, Unsaga.debuffManager.downPhysical)){
				lphurt *= 1.3F;
			}
		}

		if(attacker==null && e.source.isMagicDamage()){
			lphurt = 0;
			isPoison = true;
		}

		if(e.source.damageType=="onFire"){
			lphurt = 0;
			isPoison = true;
		}
		if(e.source.damageType=="inFire"){
			lphurt = 0;
			isPoison = true;
			
		}
		if(e.source.damageType=="fall"){
			 if(e.ammount>3){
				 lphurt = 1;
			 }
			 if(e.ammount>5){
				 lphurt = 2;
			 }
			 if(e.ammount>7){
				 lphurt = 3;
			 }
			 if(e.ammount>8){
				 lphurt = 6;
			 }
		}
		if(e.source.isExplosion()){
			 if(e.ammount>3){
				 lphurt = 1;
			 }
			 if(e.ammount>5){
				 lphurt = 3;
			 }
			 if(e.ammount>7){
				 lphurt = 5;
			 }
			 if(e.ammount>9){
				 lphurt = 8;
			 }
		}
		if(entityHurt instanceof EntityPlayer && (!e.source.isMagicDamage() || e.source.isUnblockable() || e.source.isDamageAbsolute())){
			if(((EntityPlayer) entityHurt).isBlocking()){
				lphurt *= 0.5F;
				
			}
		}
		
		


		if(Unsaga.lpManager.isLPEnabled()){
			float lpdamage = Unsaga.lpManager.getLPHurtAmount(entityHurt, lphurt, entityHurt.getRNG(),isPoison);
			if((isPoison && Unsaga.lpManager.getData(entityHurt).hurtInterval<=0)|| !isPoison){
				Unsaga.lpManager.processLPHurt(attacker, entityHurt, lpdamage);
			}
		}


			//Unsaga.lpHandler.tryHurtLP(e.entityLiving, lphurt);
		//}
		//デバッグ用（ダメージ表示）
		if(Unsaga.debug){
			if(attacker instanceof EntityPlayer){
				EntityPlayer ep = (EntityPlayer) attacker;
				ChatMessageHandler.sendChatToPlayer((EntityPlayer) e.source.getEntity(), e.ammount+" Damage! and LPSTR:"+lphurt+":Attribute>"+this.unsagaDamageSource.getUnsagaDamageType());

				Unsaga.debug(ep.getEntityAttribute(SharedMonsterAttributes.attackDamage).func_111122_c(),this.getClass());
			}
			if(entityHurt instanceof EntityPlayer){
				ChatMessageHandler.sendChatToPlayer((EntityPlayer) entityHurt, e.ammount+" Damage!:Attribute>"+this.unsagaDamageSource.getUnsagaDamageType());
			}
		}
	}

//	public float getDamageModifierFromLP(EntityLivingBase damaged,float baseDamage){
//		float amount = 0;
//		int lp = ExtendedDataLP.getData(damaged).getLP();
//		if(lp==0){
//			amount = baseDamage;
//		}else{
//			amount = -(baseDamage * 0.5F);
//		}
//
//		return amount;
//	}
	public void setUnsagaDamage(LivingHurtEvent e,DamageSourceUnsaga damagesource){
		if(e.source instanceof DamageSourceUnsaga){
			return;
		}else{
			this.unsagaDamageSource = damagesource;
		}
	}
}
