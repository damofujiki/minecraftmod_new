package com.hinasch.unlsaga.debuff;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import com.google.common.collect.Lists;
import com.hinasch.lib.ChatHandler;
import com.hinasch.lib.Statics;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingDebuff;
import com.hinasch.unlsaga.debuff.state.State;
import com.hinasch.unlsaga.debuff.state.StateBow;
import com.hinasch.unlsaga.debuff.state.StateFireStorm;
import com.hinasch.unlsaga.debuff.state.StateFlyingAxe;
import com.hinasch.unlsaga.debuff.state.StateHurricane;
import com.hinasch.unlsaga.debuff.state.StateRandomThrow;
import com.hinasch.unlsaga.debuff.state.StateTarget;

public class Debuffs {


	protected Map<Integer,Debuff> debuffMap;
	
	public Debuff downSkill,downPhysical,downMagic,lockSlime,detected,sleep;
	public Buff fireVeil,waterVeil,earthVeil,woodVeil,metalVeil;

	public Buff missuileGuard,leavesShield,powerup,lifeBoost,magicUp,perseveranceUp,waterShield,physicalUp,aegisShield;
	public Buff perseveranceDown;

	public State cooling,spellTarget,flyingAxe,kalaidoscope,antiFallDamage,weaponTarget,rushBlade,roundabout,setAiming;
	public State bowDouble,stoneShower,thunderCrap,gust,crimsonFlare;
	
	public State hurricane;


	
	public Debuffs(){
		this.debuffMap = new HashMap();
	}
	
	public void init(){
		downSkill = new Debuff(1,"Down Skill").addAbilityAgainst(Unsaga.abilityManager.skillGuard);
		downPhysical = new Debuff(2,"Down Physical").addAbilityAgainst(Unsaga.abilityManager.bodyGuard);

		downMagic = new Debuff(4,"Down Magic").addAbilityAgainst(Unsaga.abilityManager.magicGuard);
		sleep = new Debuff(5,"sleep").setAbilitiesAgainst(Lists.newArrayList(Unsaga.abilityManager.antiDebuff,Unsaga.abilityManager.antiSleep));
		lockSlime = new Buff(6,"Slime Lock");
		detected = new Debuff(7,"Detected");
		 
		
		fireVeil = (Buff) new Buff(10,"Fire Veil").setParticleNumber(Statics.getParticleNumber(Statics.particleFlame));
		waterVeil = (Buff)new Buff(11,"Water Veil").setParticleNumber(202);
		earthVeil = (Buff)new Buff(12,"Earth Veil").setParticleNumber(200);
		woodVeil = (Buff)new Buff(13,"Wood Veil").setParticleNumber(201);
		metalVeil = (Buff)new Buff(14,"Metal Veil").setParticleNumber(Statics.getParticleNumber(Statics.particleReddust));
		
		missuileGuard = new Buff(15,"Missuile Guard");
		leavesShield = (Buff)new Buff(16,"Leaves Shield").setParticleNumber(201);
		powerup = (Buff) new Buff(17,"Power up").setAttributeModifier(SharedMonsterAttributes.attackDamage,new AttributeModifier(UUID.fromString("2a0fed7c-cd21-dca7-8498-f8a85db66619"),"Buff Power Up",0.25D,1));
		lifeBoost = new Buff(18,"Life Boost");
		magicUp = new Buff(19,"Magic Up");
		perseveranceUp = new Buff(20,"perseverance Up");
		waterShield = (Buff) new Buff(21,"ice shield").setParticleNumber(202);
		physicalUp = (Buff) new Buff(22,"Physical Up").setAttributeModifier(SharedMonsterAttributes.knockbackResistance,new AttributeModifier(UUID.fromString("f48ae4e8-2d96-ce9b-1fd3-2796557439d3"),"Build Up SuperArmor",1.0D,0));
		aegisShield = (Buff) new Buff(23,"Aegis Shield");
		perseveranceDown = new Buff(24,"perseverance Up");

		//_antiFallDamage = new State(18,"anti Fall","落下無敵");
		 
		cooling = new State(99,"Cooling");
		spellTarget = new StateTarget(100,"Spell Target");
		flyingAxe = new StateFlyingAxe(101,"SkyDrive");
		kalaidoscope = new State(102,"kaleidoScope");
		antiFallDamage = new State(103,"anti Fall");
		weaponTarget = new StateTarget(104,"Weapon Target");
		rushBlade = new State(105,"Rush Blade");
		roundabout = (State) new State(106,"Roundabout").setAttributeModifier(SharedMonsterAttributes.attackDamage,new AttributeModifier(UUID.fromString("9dba3eaf-7cc8-4c36-a380-1bf1bf6221df"),"Critical On Roundabout",0.25D,1));
		setAiming = new State(107,"SetAiming");
		bowDouble = new StateBow(108,"Double Shot");
		//grandSlam = new StateGrandSlam(109,"grandslam","グランドスラム");
		stoneShower = new StateRandomThrow(110,"stoneShower").setSoundString(Statics.soundFireBall).setInterval(2);
		thunderCrap = new StateRandomThrow(111,"thunderCrap").setSoundString("random.bow").setInterval(4);
		
		gust = new State(112,"HeadWind");
		crimsonFlare = new StateFireStorm(113,"CrimsonFlare");
		hurricane = new StateHurricane(114,"Hurricane");
	} 
	public Debuff getDebuff(int par1){
		return debuffMap.get(par1);
		
	}
	
	public static void debuffEventOnLivingUpdate(LivingUpdateEvent e){
		
		EntityLivingBase living = e.entityLiving;
		if(LivingDebuff.hasDebuff(living, Unsaga.debuffManager.sleep)){
			living.motionX = 0;
			living.motionY = 0;
			living.motionZ = 0;
		}
	}
	public static void debuffEventOnLivingHurt(LivingHurtEvent e){
		EntityLivingBase hurtEntity = e.entityLiving;
		Entity attacker = e.source.getEntity();
		Debuffs debuffs = Unsaga.debuffManager;
		
		if((LivingDebuff.hasDebuff(hurtEntity,debuffs.detected))){
			e.ammount *= 1.25F;
		}
		
		
	
		//攻撃手に関するデバフ
		if(attacker instanceof EntityLivingBase){
			EntityLivingBase livingAttacker = (EntityLivingBase)attacker;

			if(LivingDebuff.hasDebuff(livingAttacker,debuffs.downSkill)){
				if(!Unsaga.lpManager.isLPEnabled()){
					LivingDebuff buff = (LivingDebuff) LivingDebuff.getLivingDebuff((EntityLivingBase) attacker, debuffs.downSkill).get();
					if(attacker instanceof EntityPlayer){
						ChatHandler.sendChatToPlayer((EntityPlayer) attacker, "miss!");
					}

					
					if(((EntityLivingBase) attacker).getRNG().nextInt(100)<20){
						e.ammount = 1;
					}
					
				}

			}
			if(LivingDebuff.hasDebuff(livingAttacker,debuffs.downMagic)){
				if(e.source.isMagicDamage()){
					e.ammount *= 0.5F;
					Unsaga.debug("魔力減がきいている");
				}


			}


			
			if(LivingDebuff.hasDebuff(hurtEntity, debuffs.downPhysical)){
				float yaw = MathHelper.wrapAngleTo180_float(hurtEntity.rotationYaw + 180.0F);
				float i = 1.0F;
				hurtEntity.addVelocity((double)(-MathHelper.sin((yaw) * (float)Math.PI / 180.0F) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(yaw * (float)Math.PI / 180.0F) * (float)i * 0.5F));
			}
		}

		if(e.source.isUnblockable()){
			if(LivingDebuff.hasDebuff(hurtEntity,debuffs.perseveranceDown)){
					e.ammount *= 1.25F;
					Unsaga.debug("心DOWNがきいている");
				


			}
		}
		doBuffs(e);


		
		
	}
	

	
	protected static void doBuffs(LivingHurtEvent e){
		EntityLivingBase hurtEntity = e.entityLiving;
		Entity attacker = e.source.getEntity();
		Debuffs debuffs = Unsaga.debuffManager;
		//攻撃手に関係するバフ
		if(attacker instanceof EntityLivingBase){
			EntityLivingBase livingAttacker = (EntityLivingBase)attacker;
			if(LivingDebuff.hasDebuff(livingAttacker,debuffs.magicUp)){
				if(e.source.isMagicDamage()){
					e.ammount *= 1.5F;
					Unsaga.debug("魔力upがきいている");
				}


			}
		}
		if(e.source.isUnblockable()){
			if(LivingDebuff.hasDebuff(hurtEntity,debuffs.perseveranceUp)){
					e.ammount *= 0.6F;
					Unsaga.debug("心UPがきいている");
				


			}
		}
//		if(LivingDebuff.hasDebuff(hurtEntity, debuffs.antiFallDamage) && e.source == DamageSource.fall){
//			e.ammount = 0;
//			hurtEntity.fallDistance = 0;
//			e.setCanceled(true);
//			LivingDebuff.removeDebuff(hurtEntity, debuffs.antiFallDamage);
//			return;
//		}
	}
}
