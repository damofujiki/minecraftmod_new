package com.hinasch.unlsaga.ability;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.potion.Potion;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.skill.Skill;
import com.hinasch.unlsaga.ability.skill.effect.SkillAxe;
import com.hinasch.unlsaga.ability.skill.effect.SkillBow;
import com.hinasch.unlsaga.ability.skill.effect.SkillMartialArts;
import com.hinasch.unlsaga.ability.skill.effect.SkillSpear;
import com.hinasch.unlsaga.ability.skill.effect.SkillStaff;
import com.hinasch.unlsaga.ability.skill.effect.SkillSword;
import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;
import com.hinasch.unlsaga.material.UnsagaMaterial;
import com.hinasch.unlsaga.material.UnsagaMaterials;
import com.hinasch.unlsaga.util.damage.DamageHelper;

public class AbilityRegistry {

	public static AbilityRegistry instance;
	public static Map<Integer,Ability> allAbilities ;
	

	protected UnsagaMaterials materials;
	
	protected Map<String,List<Ability>> normalAbilities;
	protected Map<String,List<Ability>> inheritAbilities;
	
	public final static SkillAxe skillAxe = SkillAxe.getInstance();
	public final static SkillStaff skillStaff = SkillStaff.getInstance();
	public final static SkillSpear skillSpear = SkillSpear.getInstance();
	public final static SkillSword skillSword = SkillSword.getInstance();
	public final static SkillBow skillBow = SkillBow.getInstance();
	public final static SkillMartialArts skillPunch = SkillMartialArts.getInstance();

	public final Ability healDown5,healUp5,supportFire,supportWater,supportEarth,supportMetal,supportWood;
	public final Ability fire,water,earth,metal,wood,healUp10,healDown10;
	public final Ability lifeGuard,fireProtection,antiPoison,skillGuard,magicGuard,antiDebuff,projectileProtection;
	public final Ability blastProtection,unlock,defuse,divination,healDown20,healDown25,healDown15;
	public final Ability dummy,forbidden,supportForbidden,antiSleep;
	public final Ability antiBlind,protectionSlash,bodyGuard,antiWither,superHealing,powerGuard;


	
	public final Skill kaleidoscope,slash,smash,roundabout,chargeBlade,gust,vandalize;
	public final Skill tomahawk,fujiView,skyDrive,woodBreakerPhoenix,woodChopper;
	public final Skill aiming,acupuncture,swing,grassHopper;
	public final Skill earthDragon,skullCrash,pulvorizer,grandSlam,gonger,rockCrusher;
	public final Skill doubleShot,tripleShot,zapper,exorcist,shadowStitching,phoenix,arrowRain;
	public final Skill airThrow,cyclone,callBack;

	//TODO 一部の技、攻撃力が増えるものだけでなく下るものもあるようにする
	public final Set<Ability> healDowns;
	public final Set<Ability> healUps;
	
	public final Set<Skill> requireCoolingSet;
	
	public static Map<Potion,Ability> againstPotionMap ;
	
	public static AbilityRegistry getInstance(){
		if(instance==null){
			instance = new AbilityRegistry();
		}
		return instance;
	}
	protected void asociateAbilitiesWithTools(){
		addInheritAbility(ToolCategory.SWORD,materials.dragonHeart,Lists.newArrayList(superHealing));
		addInheritAbility(ToolCategory.STAFF,materials.dragonHeart,Lists.newArrayList(superHealing));
		addInheritAbility(ToolCategory.AXE,materials.dragonHeart,Lists.newArrayList(superHealing));
		addInheritAbility(ToolCategory.SPEAR,materials.dragonHeart,Lists.newArrayList(superHealing));
		addInheritAbility(ToolCategory.BOW,materials.dragonHeart,Lists.newArrayList(superHealing));
		
		addInheritAbility(ToolCategory.ARMOR,materials.cloth,Lists.newArrayList(healDown5));
		addInheritAbility(ToolCategory.ARMOR,materials.categorywood,Lists.newArrayList(healDown5));
		addInheritAbility(ToolCategory.ARMOR,materials.categoryStone,Lists.newArrayList(healDown20));
		addInheritAbility(ToolCategory.ARMOR,materials.leathers,Lists.newArrayList(healDown5));
		addInheritAbility(ToolCategory.ARMOR,materials.bestial,Lists.newArrayList(healDown10));
		addInheritAbility(ToolCategory.ARMOR,materials.silver,Lists.newArrayList(healDown15));
		addInheritAbility(ToolCategory.ARMOR,materials.metals,Lists.newArrayList(healDown25));
		addInheritAbility(ToolCategory.ARMOR,materials.hydra,Lists.newArrayList(healDown10));
		addInheritAbility(ToolCategory.ARMOR,materials.fairieSilver,Lists.newArrayList(healDown15));
		addInheritAbility(ToolCategory.ARMOR,materials.obsidian,Lists.newArrayList(healDown25));
		addInheritAbility(ToolCategory.ARMOR,materials.steels,Lists.newArrayList(healDown25));
		addInheritAbility(ToolCategory.ARMOR,materials.liveSilk,Lists.newArrayList(healDown5));
		addInheritAbility(ToolCategory.ARMOR,materials.damascus,Lists.newArrayList(healDown25));
		
		addAbility(ToolCategory.ARMOR,materials.cloth,Lists.newArrayList(lifeGuard));
		addAbility(ToolCategory.ARMOR,materials.liveSilk,Lists.newArrayList(lifeGuard,antiWither));
		addAbility(ToolCategory.ARMOR,materials.categorywood,Lists.newArrayList(supportFire,lifeGuard));
		addAbility(ToolCategory.ARMOR,materials.meteorite,Lists.newArrayList(blastProtection));
		addAbility(ToolCategory.ARMOR,materials.categoryStone,Lists.newArrayList(lifeGuard));
		addAbility(ToolCategory.ARMOR,materials.leathers,Lists.newArrayList(lifeGuard));
		addAbility(ToolCategory.ARMOR,materials.fur,Lists.newArrayList(lifeGuard));
		addAbility(ToolCategory.ARMOR,materials.crocodileLeather,Lists.newArrayList(lifeGuard));
		addAbility(ToolCategory.ARMOR,materials.carnelian,Lists.newArrayList(supportFire));
		addAbility(ToolCategory.ARMOR,materials.opal,Lists.newArrayList(supportMetal));
		addAbility(ToolCategory.ARMOR,materials.topaz,Lists.newArrayList(supportEarth));
		addAbility(ToolCategory.ARMOR,materials.ravenite,Lists.newArrayList(supportWater));
		addAbility(ToolCategory.ARMOR,materials.lazuli,Lists.newArrayList(supportWood));
		addAbility(ToolCategory.ARMOR,materials.silver,Lists.newArrayList(supportWater));
		addAbility(ToolCategory.ARMOR,materials.meteoricIron,Lists.newArrayList(blastProtection));
		addAbility(ToolCategory.ARMOR,materials.metals,Lists.newArrayList(bodyGuard));
		addAbility(ToolCategory.ARMOR,materials.steel1,Lists.newArrayList(bodyGuard));
		addAbility(ToolCategory.ARMOR,materials.hydra,Lists.newArrayList(fireProtection,antiPoison));
		addAbility(ToolCategory.ARMOR,materials.fairieSilver,Lists.newArrayList(antiDebuff,blastProtection));
		addAbility(ToolCategory.ARMOR,materials.damascus,Lists.newArrayList(bodyGuard));
		
		
		addInheritAbility(ToolCategory.HELMET,materials.categoryStone,Lists.newArrayList(healDown5));
		addInheritAbility(ToolCategory.HELMET,materials.metals,Lists.newArrayList(healDown5));
		addInheritAbility(ToolCategory.HELMET,materials.hydra,Lists.newArrayList(healDown5));
		addInheritAbility(ToolCategory.HELMET,materials.steels,Lists.newArrayList(healDown5));
		addInheritAbility(ToolCategory.HELMET,materials.corundums,Lists.newArrayList(healDown5));
		addInheritAbility(ToolCategory.HELMET,materials.obsidian,Lists.newArrayList(healDown5));
		addInheritAbility(ToolCategory.HELMET,materials.diamond,Lists.newArrayList(healDown5));
		addInheritAbility(ToolCategory.HELMET,materials.damascus,Lists.newArrayList(healDown5));
		
		addAbility(ToolCategory.HELMET,materials.cloth,Lists.newArrayList(supportFire));
		addAbility(ToolCategory.HELMET,materials.liveSilk,Lists.newArrayList(supportFire));
		addAbility(ToolCategory.HELMET,materials.fur,Lists.newArrayList(supportMetal));
		addAbility(ToolCategory.HELMET,materials.leathers,Lists.newArrayList(supportMetal));
		addAbility(ToolCategory.HELMET,materials.categoryStone,Lists.newArrayList(antiBlind,magicGuard));
		addAbility(ToolCategory.HELMET,materials.metals,Lists.newArrayList(antiBlind,magicGuard));
		addAbility(ToolCategory.HELMET,materials.silver,Lists.newArrayList(supportWater));
		addAbility(ToolCategory.HELMET,materials.hydra,Lists.newArrayList(antiPoison,fireProtection));
		addAbility(ToolCategory.HELMET,materials.meteoricIron,Lists.newArrayList(antiBlind));
		addAbility(ToolCategory.HELMET,materials.corundum1,Lists.newArrayList(fireProtection));
		addAbility(ToolCategory.HELMET,materials.corundum2,Lists.newArrayList(protectionSlash));
		addAbility(ToolCategory.HELMET,materials.steels,Lists.newArrayList(antiBlind,magicGuard));
		addAbility(ToolCategory.HELMET,materials.fairieSilver,Lists.newArrayList(supportWater));
		addAbility(ToolCategory.HELMET,materials.obsidian,Lists.newArrayList(antiBlind,magicGuard));
		addAbility(ToolCategory.HELMET,materials.diamond,Lists.newArrayList(antiDebuff));
		addAbility(ToolCategory.HELMET,materials.damascus,Lists.newArrayList(antiBlind,magicGuard));
		
		addInheritAbility(ToolCategory.LEGGINS,materials.copperOre,Lists.newArrayList(dummy));
		addInheritAbility(ToolCategory.LEGGINS,materials.ironOre,Lists.newArrayList(dummy));
		addInheritAbility(ToolCategory.LEGGINS,materials.categoryStone,Lists.newArrayList(healDown5));
		addInheritAbility(ToolCategory.LEGGINS,materials.quartz,Lists.newArrayList(healDown10));
		addInheritAbility(ToolCategory.LEGGINS,materials.copper,Lists.newArrayList(healDown10));
		addInheritAbility(ToolCategory.LEGGINS,materials.lead,Lists.newArrayList(healDown10));
		addInheritAbility(ToolCategory.LEGGINS,materials.meteoricIron,Lists.newArrayList(healDown10));
		addInheritAbility(ToolCategory.LEGGINS,materials.hydra,Lists.newArrayList(healDown5));
		addInheritAbility(ToolCategory.LEGGINS,materials.obsidian,Lists.newArrayList(healDown10));
		
		addAbility(ToolCategory.LEGGINS,materials.metals,Lists.newArrayList(bodyGuard));
		addAbility(ToolCategory.LEGGINS,materials.iron,Lists.newArrayList(supportWater,blastProtection));
		addAbility(ToolCategory.BOOTS,materials.meteoricIron,Lists.newArrayList(bodyGuard));
		addAbility(ToolCategory.BOOTS,materials.hydra,Lists.newArrayList(fireProtection,antiPoison));
		addAbility(ToolCategory.BOOTS,materials.obsidian,Lists.newArrayList(skillGuard));
		
		addInheritAbility(ToolCategory.ACCESSORY,materials.sivaQueen,Lists.newArrayList(healUp10));
		
		addAbility(ToolCategory.ACCESSORY,materials.categorywood,Lists.newArrayList(healUp5,supportFire));
		addAbility(ToolCategory.ACCESSORY,materials.bone,Lists.newArrayList(healDown5,supportWood,lifeGuard));
		addAbility(ToolCategory.ACCESSORY,materials.categoryStone,Lists.newArrayList(healUp5,supportMetal));
		addAbility(ToolCategory.ACCESSORY,materials.bestial,Lists.newArrayList(healUp5));
		addAbility(ToolCategory.ACCESSORY,materials.carnelian,Lists.newArrayList(healUp5,fire));
		addAbility(ToolCategory.ACCESSORY,materials.opal,Lists.newArrayList(healUp5,metal));
		addAbility(ToolCategory.ACCESSORY,materials.topaz,Lists.newArrayList(healUp5,earth));
		addAbility(ToolCategory.ACCESSORY,materials.ravenite,Lists.newArrayList(healUp5,water));
		addAbility(ToolCategory.ACCESSORY,materials.lazuli,Lists.newArrayList(healUp5,wood));
		addAbility(ToolCategory.ACCESSORY,materials.meteorite,Lists.newArrayList(healUp5,supportFire,supportEarth,supportMetal));
		addAbility(ToolCategory.ACCESSORY,materials.angelite,Lists.newArrayList(healUp10,supportWood,lifeGuard));
		addAbility(ToolCategory.ACCESSORY,materials.demonite,Lists.newArrayList(healDown20,supportForbidden,forbidden));
		addAbility(ToolCategory.ACCESSORY,materials.silver,Lists.newArrayList(healUp5,supportWater,unlock));
		addAbility(ToolCategory.ACCESSORY,materials.corundum2,Lists.newArrayList(healUp5,supportWater,water));
		addAbility(ToolCategory.ACCESSORY,materials.corundum1,Lists.newArrayList(healUp5,supportFire,fire));
		addAbility(ToolCategory.ACCESSORY,materials.obsidian,Lists.newArrayList(healUp5,supportMetal,unlock));
		addAbility(ToolCategory.ACCESSORY,materials.diamond,Lists.newArrayList(healUp5,supportFire,antiSleep,unlock));
		addAbility(ToolCategory.ACCESSORY,materials.metals,Lists.newArrayList(healUp5,supportWater));		
		addAbility(ToolCategory.ACCESSORY,materials.steels,Lists.newArrayList(healUp5,supportWater));	
		addAbility(ToolCategory.ACCESSORY,materials.meteoricIron,Lists.newArrayList(healUp5,supportMetal,supportWater,supportWood));		
		addAbility(ToolCategory.ACCESSORY,materials.fairieSilver,Lists.newArrayList(healUp10,antiWither,unlock,supportWater));	
		addAbility(ToolCategory.ACCESSORY,materials.damascus,Lists.newArrayList(healUp5,antiWither,supportWater,lifeGuard));
		addAbility(ToolCategory.ACCESSORY,materials.sivaQueen,Lists.newArrayList(bodyGuard,powerGuard,skillGuard));
	}
	protected AbilityRegistry(){
		materials = Unsaga.materialManager;
		this.allAbilities = new HashMap();
		this.inheritAbilities = new HashMap();
		this.normalAbilities = new HashMap();
		
		healDown5 = new Ability(0,"Heal -5","回復力 -5",-1).addToMap(allAbilities);
		healUp5 = new Ability(1,"Heal +5","回復力 +5",1).addToMap(allAbilities);
		supportFire = new Ability(2,"Support Fire","火行サポート").addToMap(allAbilities);
		supportWater = new Ability(3,"Support Water","水行サポート").addToMap(allAbilities);
		supportEarth = new Ability(4,"Support Earth","土行サポート").addToMap(allAbilities);
		supportMetal = new Ability(5,"Support Metal","金行サポート").addToMap(allAbilities);
		supportWood = new Ability(6,"Support Wood","木行サポート").addToMap(allAbilities);
		fire = new Ability(7,"Spell Fire","火行術").addToMap(allAbilities);
		water = new Ability(8,"Spell Water","水行術").addToMap(allAbilities);
		earth = new Ability(9,"Spell Earth","土行術").addToMap(allAbilities);
		metal = new Ability(10,"Spell Metal","金行術").addToMap(allAbilities);
		wood = new Ability(11,"Spell Wood","木行術").addToMap(allAbilities);
		healUp10 = new Ability(12,"Heal +10","回復力 +10",2).addToMap(allAbilities);
		healDown10 = new Ability(13,"Heal -10","回復力 -10",-2).addToMap(allAbilities);
		lifeGuard = new Ability(14,"Life Guard","ライフ防御").addToMap(allAbilities);
		fireProtection = new Ability(15,"Fire Protection","熱防御").addToMap(allAbilities);
		antiPoison = new Ability(16,"Anti Poison","毒防御").addToMap(allAbilities);
		skillGuard = new Ability(17,"Anti Debuff:Skill","保護 技").addToMap(allAbilities);
		magicGuard = new Ability(18,"Anti Debuff:Magic","保護 魔").addToMap(allAbilities);
		antiDebuff = new Ability(19,"Anti Debuff","状態防御").addToMap(allAbilities);
		projectileProtection = new Ability(20,"Projectile Protection","射突防御").addToMap(allAbilities);
		blastProtection = new Ability(21,"Blast Protection","爆風防御").addToMap(allAbilities);
		unlock = new Ability(21,"Unlock","鍵開け").addToMap(allAbilities);
		defuse = new Ability(22,"Defuse","罠外し").addToMap(allAbilities);
		divination = new Ability(23,"Divination","占い").addToMap(allAbilities);
		healDown20 = new Ability(24,"Heal -20","回復力 -20",-4).addToMap(allAbilities);
		healDown25 = new Ability(25,"Heal -25","回復力 -25",-5).addToMap(allAbilities);
		healDown15 = new Ability(26,"Heal -15","回復力 -15",-3).addToMap(allAbilities);
		dummy = new Ability(27,"","").addToMap(allAbilities);
		forbidden = new Ability(28,"Spell Forbidden","禁行術").addToMap(allAbilities);
		supportForbidden = new Ability(29,"Support Forbidden","禁行サポート").addToMap(allAbilities);
		antiSleep = new Ability(30,"Anti Paralyze","マヒ防御").addToMap(allAbilities);
		antiBlind = new Ability(31,"Anti Blindness","暗闇防御").addToMap(allAbilities);
		protectionSlash = new Ability(32,"Slash Protection","斬撃防御").addToMap(allAbilities);
		//protectionProjectile = new Ability(33,"Projectile Protection","射突防御").addToMap(allAbilitiesMap);
		bodyGuard = new Ability(34,"Anti Debuff:Body","保護 体").addToMap(allAbilities);
		antiWither = new Ability(35,"Anti Wither","ウィザー耐性").addToMap(allAbilities);
		superHealing = new Ability(36,"Super Healing","超回復").addToMap(allAbilities);
		powerGuard = new Ability(37,"Power Guard","保護 力").addToMap(allAbilities);
		
		
		healDowns = Sets.newHashSet(healDown5,healDown10,healDown15,healDown20,healDown25);
		healUps = Sets.newHashSet(healUp5,healUp10);
		
		
		kaleidoscope = new Skill(100,"Bopeep","変幻自在",20,2,DamageHelper.Type.SWORD,5).setSkillEffect(skillSword.kaleidoScope).addToMap(allAbilities);
		slash = new Skill(101,"Slash","払い抜け",5,3,DamageHelper.Type.SWORD,3).setSkillEffect(null).addToMap(allAbilities);
		smash = new Skill(102,"Smash","スマッシュ",15,3,DamageHelper.Type.SWORD,8).setSkillEffect(skillSword.smash).addToMap(allAbilities);
		roundabout = new Skill(103,"Roundabout","転",0,1,DamageHelper.Type.SWORD,6).setSkillEffect(skillSword.roundabout).addToMap(allAbilities);
		chargeBlade = new Skill(104,"Charge Blade","追突剣",0,1,DamageHelper.Type.SWORDPUNCH,8).setSkillEffect(skillSword.chargeBlade).addToMap(allAbilities);
		gust = new Skill(105,"Gust","逆風の太刀",9,2,DamageHelper.Type.SWORD,5).setSkillEffect(skillSword.gust).addToMap(allAbilities);
		vandalize = new Skill(106,"Vandalize","ヴァンダライズ",10,2,DamageHelper.Type.SWORD,15).setSkillEffect(skillSword.vandelize).addToMap(allAbilities);
		tomahawk = new Skill(107,"Tomahawk","トマホーク",5,1,DamageHelper.Type.SWORDPUNCH,15).setSkillEffect(null).addToMap(allAbilities);
		fujiView = new Skill(108,"Fuji View","富嶽百景",19,2,DamageHelper.Type.SWORDPUNCH,15).setSkillEffect(skillAxe.fujiView).addToMap(allAbilities);
		skyDrive = new Skill(109,"Skydrive","スカイドライブ",10,2,DamageHelper.Type.SWORDPUNCH,15).setSkillEffect(skillAxe.skyDrive).addToMap(allAbilities);
		woodBreakerPhoenix = new Skill(111,"Wood Breaker Phoenix","マキ割りフェニックス",10,2,DamageHelper.Type.SWORDPUNCH,15).setSkillEffect(skillAxe.woodBreaker).addToMap(allAbilities);
		woodChopper = new Skill(110,"Wood Chopper","大木断",5,0,DamageHelper.Type.SWORDPUNCH,15).setSkillEffect(skillAxe.woodChopper).addToMap(allAbilities);
		aiming = new Skill(120,"Aiming","エイミング",10,2,DamageHelper.Type.SPEAR,15).setSkillEffect(skillSpear.aiming).addToMap(allAbilities);
		acupuncture = new Skill(121,"Acupuncture","独妙点穴",20,3,DamageHelper.Type.SPEAR,25).setSkillEffect(skillSpear.acupuncture).addToMap(allAbilities);
		swing = new Skill(122,"Swing","スウィング",-10,1,DamageHelper.Type.SWORDPUNCH,15).setSkillEffect(skillSpear.swing).addToMap(allAbilities).addToMap(allAbilities);
		grassHopper = new Skill(123,"Grass Hopper","草伏せ",-50,0,DamageHelper.Type.SPEAR,5).setSkillEffect(skillSpear.grassHopper).addToMap(allAbilities).addToMap(allAbilities);
		earthDragon = new Skill(133,"Earth Dragon","土竜撃",-13,0,DamageHelper.Type.PUNCH,5).setSkillEffect(skillStaff.earthDragon).addToMap(allAbilities);
		skullCrash = new Skill(134,"Skull Crash","スカルクラッシュ",1,1,DamageHelper.Type.PUNCH,5).setSkillEffect(skillStaff.skullCrusher).addToMap(allAbilities);
		pulvorizer = new Skill(135,"Pulvorizer","粉砕撃",5,0,DamageHelper.Type.PUNCH,5).setSkillEffect(skillStaff.pulverizer).addToMap(allAbilities);
		grandSlam = new Skill(136,"Grand Slam","グランドスラム",2,1,DamageHelper.Type.PUNCH,5).setSkillEffect(skillStaff.grandSlam).addToMap(allAbilities);
		gonger = new Skill(137,"Gonger","どら鳴らし",1,0,DamageHelper.Type.PUNCH,5).setSkillEffect(skillStaff.bellRinger).addToMap(allAbilities);
		rockCrusher = new Skill(138,"Rockcrusher","削岩撃",1,0,DamageHelper.Type.PUNCH,5).setSkillEffect(skillStaff.rockCrusher).addToMap(allAbilities);
		
		doubleShot = new Skill(150,"Double Shot","ニ連射",2,2,DamageHelper.Type.SPEAR,5).setSkillEffect(skillBow.multipleShoot).addToMap(allAbilities);
		tripleShot = new Skill(151,"Triple Shot","三連射",2,2,DamageHelper.Type.SPEAR,9).setSkillEffect(skillBow.multipleShoot).addToMap(allAbilities);
		zapper = new Skill(152,"Zapper","ザップショット",5,2,DamageHelper.Type.SPEAR,15).setSkillEffect(skillBow.zapper).addToMap(allAbilities);
		exorcist = new Skill(153,"Exorcist","破魔の矢",5,1,DamageHelper.Type.SPEAR,5).setSkillEffect(skillBow.exorcist).addToMap(allAbilities);
		shadowStitching = new Skill(154,"Shadow Stitching","影縫い",2,0,DamageHelper.Type.SPEAR,3).setSkillEffect(skillBow.shodowStitch).addToMap(allAbilities);
		phoenix = new Skill(155,"Phoenix Arrow","フェニックスアロー",2,2,DamageHelper.Type.SPEAR,3).setSkillEffect(skillBow.phoenixArrow).addToMap(allAbilities);
		arrowRain = new Skill(156,"Arrow Rain","アローレイン",2,0,DamageHelper.Type.SPEAR,3).setSkillEffect(skillBow.shodowStitch).addToMap(allAbilities);
		
		airThrow = new Skill(170,"Air Throwing","空気投げ",2,0,DamageHelper.Type.PUNCH,2).setSkillEffect(skillPunch.airThrow).addToMap(allAbilities);
		cyclone = new Skill(171,"Cyclone","竜巻投げ",5,0,DamageHelper.Type.PUNCH,2).setSkillEffect(skillPunch.hurricaneThrow).addToMap(allAbilities);
		callBack = new Skill(172,"Call Back","呼び戻し",2,0,DamageHelper.Type.PUNCH,2).setSkillEffect(skillPunch.callBack).addToMap(allAbilities);
		this.requireCoolingSet = Sets.newHashSet(fujiView,vandalize,skyDrive,grandSlam,zapper);
		
		this.againstPotionMap = new HashMap();
		againstPotionMap.put(Potion.poison, antiPoison);
		againstPotionMap.put(Potion.wither, antiWither);
		againstPotionMap.put(Potion.blindness, antiBlind);
		
		this.asociateAbilitiesWithTools();
		this.asociateSkillsWithWeapons();
	}
	
	public void asociateSkillsWithWeapons(){
		//vandalize.setMethod(skillSword.getClass().getMethod("doVandelize", parameterTypes));
		addSkill(ToolCategory.SWORD,true,newSkillList(vandalize,smash,gust));
		addSkill(ToolCategory.SWORD,false,newSkillList(kaleidoscope,roundabout,chargeBlade));
		addSkill(ToolCategory.AXE,false,newSkillList(tomahawk,skyDrive,woodChopper));
		addSkill(ToolCategory.AXE,true,newSkillList(fujiView,woodBreakerPhoenix,woodChopper));
		addSkill(ToolCategory.SPEAR,false,newSkillList(swing,grassHopper));
		addSkill(ToolCategory.SPEAR,true,newSkillList(aiming,acupuncture));
		addSkill(ToolCategory.BOW,false,newSkillList(doubleShot,tripleShot,shadowStitching));
		addSkill(ToolCategory.BOW,true,newSkillList(zapper,exorcist,phoenix));
		addSkill(ToolCategory.STAFF,false,newSkillList(skullCrash,pulvorizer,gonger,rockCrusher));
		addSkill(ToolCategory.STAFF,true,newSkillList(gonger,grandSlam,earthDragon));
	}
	

	
	public static List<Ability> newSkillList(Skill... skills){
		List<Ability> newlist = new ArrayList();
		for(Skill skill:skills){
			newlist.add((Ability)skill);
		}
		return newlist;
	}
	
	public void addAbility(ToolCategory category,UnsagaMaterial material,List<Ability> abilityList){
		this.normalAbilities.put(category.toString()+"."+material.name,abilityList);
	}
	
	public void addSkill(ToolCategory category,boolean heavy,List<Ability> abilityList){
		String keyweight = (heavy ? "HEAVY" : "LIGHT");
		this.normalAbilities.put(category.toString()+"."+keyweight,abilityList);
	}
	
	public void addInheritAbility(ToolCategory sword,UnsagaMaterial material,List<Ability> abilityList){
		this.inheritAbilities.put(sword.toString()+"."+material.name,abilityList);
	}
	
	private Optional<List<Ability>> getAbilityList(ToolCategory category,UnsagaMaterial material){
		if(!this.normalAbilities.isEmpty()){
			if(this.normalAbilities.get(category.toString()+"."+material.name)!=null){
				return Optional.of(this.normalAbilities.get(category.toString()+"."+material.name));
			}
		}
		return Optional.absent();
	}
	
	public Optional<List<Ability>> getSkillList(ToolCategory category,boolean heavy){
		String keyweight = (heavy ? "HEAVY" : "LIGHT");
		if(!this.normalAbilities.isEmpty()){
			if(this.normalAbilities.get(category.toString()+"."+keyweight)!=null){
				return Optional.of(this.normalAbilities.get(category.toString()+"."+keyweight));
			}
		}
		return Optional.absent();
	}
	
	private Optional<List<Ability>> getInheritAbilityList(ToolCategory category,UnsagaMaterial material){
		if(!this.inheritAbilities.isEmpty()){
			String key = category.toString()+"."+material.name;
			if(this.inheritAbilities.get(key)!=null){
				return Optional.of(this.inheritAbilities.get(key));
			}
			
		}
		return Optional.absent();
	}
	
	public Optional<List<Ability>> getInheritAbilities(ToolCategory category,UnsagaMaterial material){
		if(getInheritAbilityList(category,material).isPresent()){
			return getInheritAbilityList(category,material);
		}
		if(material.isChild){
			UnsagaMaterial parent = material.getParentMaterial();
			if(getInheritAbilityList(category,parent).isPresent()){
				return getInheritAbilityList(category,parent);
			}
		}
		return Optional.absent();
	}
	public boolean isSkillSuited(Skill skillIn,ToolCategory category,boolean isHeavy){
		if(this.getSkillList(category, isHeavy).isPresent()){
			List<Ability> list = this.getSkillList(category, isHeavy).get();
			if(list.contains(skillIn)){
				return true;
			}
		}
		return false;
	}
	public Optional<List<Ability>> getAbilities(ToolCategory category,UnsagaMaterial material){
		if(getAbilityList(category,material).isPresent()){
			return getAbilityList(category,material);
		}
		if(material.isChild){
			UnsagaMaterial parent = material.getParentMaterial();
			if(getAbilityList(category,parent).isPresent()){
				return getAbilityList(category,parent);
			}
		}
		return Optional.absent();
	}
	
	public boolean hasInherit(ToolCategory category,UnsagaMaterial material,Ability ability){
		if(getInheritAbilities(category,material).isPresent()){
			return getInheritAbilities(category,material).get().contains(ability);
		}
		return false;
	}
	
	public int getInheritHealAmount(ToolCategory category,UnsagaMaterial material,Collection<Ability> ability){
		if(getInheritAbilities(category,material).isPresent()){
			//Unsaga.debug(getInheritAbilities(category,material).get().toString());
			int healpoint = 0;
			for(Iterator<Ability> ite=ability.iterator();ite.hasNext();){
				Ability inputAbility = ite.next();
				if(getInheritAbilities(category,material).get().contains(inputAbility)){
					healpoint += inputAbility.healPoint;
					//Unsaga.debug(healpoint);
				}
			}
			return healpoint;
		}
		return 0;
	}
	

	
	public Ability getAbilityFromInt(int par1){
		return this.allAbilities.get(par1);
	}
	
	public static List<Integer> toIntegerList(List<Ability> input){
		List<Integer> output = new ArrayList();
		for(Ability ab:input){
			output.add(ab.number);
		}
		return output;
	}
	
	public List<Ability> exchangeIntListToAbilityList(List<Integer> input){
		List<Ability> output = new ArrayList();
		for(Integer i:input){
			output.add(this.getAbilityFromInt(i));
		}
		return output;
	}
	

}
