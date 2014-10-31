package com.hinasch.unlsagamagic.spell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.util.FiveElements.Enums;
import com.hinasch.unlsagamagic.spell.effect.SpellEffectBlend;
import com.hinasch.unlsagamagic.spell.effect.SpellEffectNormal;

public class Spells {

	public static Spells INSTANCE;
	protected final Map<Integer,Spell> spells;

	public final SpellEffectNormal effectNormal;
	public final SpellEffectBlend effectBlend;
	protected final Set<SpellBlend> blendSet;

	public final Spell fireArrow,fireVeil,heroism,fireWall,fireStorm;
	
	public final Spell cloudCall,purify,waterVeil,waterShield;

	public final Spell stoneVeil,boulder;

	public final Spell animalCharm;
	public final Spell detectAnimal;
	public final Spell buildUp;

	public final Spell missuileGuard;
	public final Spell recycle;
	public final Spell woodVeil;
	public final Spell lifeBoost;
	public final Spell callThunder;
	public final Spell meditation;

	public final Spell overGrowth;
	public final Spell metalVeil;
	public final Spell detectGold;
	public final Spell magicLock;
	public final Spell superSonic;
	//public final Spell armorBless = new Spell(EnumElement.METAL,42,"Armor Blessing","アーマーブレス",13,14,70);
	//public final Spell weaponBless = new Spell(EnumElement.METAL,43,"Weapon Blessing","ウエポンブレス",13,14,70);



	public final Spell weakness,abyss,detectBlood;

	public final SpellBlend crimsonFlare,stoneShower,detectTreasure,iceNine,leavesShield,touchGold,thunderCrap,reflesh,restoration;



	protected Spells(){
		spells = new HashMap();
		effectNormal = SpellEffectNormal.getInstance();
		effectBlend = SpellEffectBlend.getInstance();
		blendSet = new HashSet();				

		fireArrow = new Spell(Enums.FIRE,1,"Fire Arrow","炎の矢",10,10,85).setSpellEffect(effectNormal.fireArrow).setStrength(5, 1).addToMap(spells);
		fireVeil = new Spell(Enums.FIRE,2,"Fire Veil","火遁",8,8,85).setSpellEffect(effectNormal.elemntVeil).addToMap(spells);
		heroism = new Spell(Enums.FIRE,3,"Heroism","ヒロイズム",8,10,70).setSpellEffect(effectNormal.heroism).addToMap(spells);
		fireWall = new Spell(Enums.FIRE,4,"Fire Wall","ファイアウォール",14,14,75).setSpellEffect(effectNormal.fireWall).setUsePointer(true).addToMap(spells);
		fireStorm = new Spell(Enums.FIRE,5,"Fire Storm","太陽風",18,20,70).setStrength(3, 0.4F).setSpellEffect(effectNormal.fireStorm).setStrength(3, 1).addToMap(spells);

		cloudCall = new Spell(Enums.WATER,10,"Cloud Call","クラウドコール",6,10,70).setSpellEffect(effectNormal.cloudCall).addToMap(spells);
		purify = new Spell(Enums.WATER,11,"Purify","ピュリファイ",11,10,80).setSpellEffect(effectNormal.purify).setStrength(3.0F, 0).addToMap(spells);
		waterVeil = new Spell(Enums.WATER,12,"Water Veil","水遁",8,8,85).setSpellEffect(effectNormal.elemntVeil).addToMap(spells);
		waterShield = new Spell(Enums.WATER,13,"Water Shield","水の盾",8,8,85).setSpellEffect(effectNormal.waterShield).addToMap(spells);


		stoneVeil = new Spell(Enums.EARTH,21,"Earth Veil","土遁",8,8,75).setSpellEffect(effectNormal.elemntVeil).addToMap(spells);
		boulder = new Spell(Enums.EARTH,22,"Stone Barrett","ストーンバレット",10,10,85).setSpellEffect(effectNormal.boulder).setStrength(5, 0).addToMap(spells);


		animalCharm = new Spell(Enums.EARTH,23,"Animal Charm","アニマルチャーム",8,9,75).setSpellEffect(effectNormal.animalCharm).addToMap(spells);
		detectAnimal = new Spell(Enums.EARTH,24,"Detect Animal","デテクトアニマル",8,8,75).setSpellEffect(effectNormal.detectAnimal).addToMap(spells);
		buildUp = new Spell(Enums.EARTH,25,"Build Up","ビルドアップ",8,10,75).setSpellEffect(effectNormal.buildUp).addToMap(spells);

		missuileGuard = new Spell(Enums.WOOD,30,"Missuile Guard","ミサイルガード",14,15,65).setSpellEffect(effectNormal.missuileGuard).addToMap(spells);
		recycle = new Spell(Enums.WOOD,31,"Recycle","リサイクル",15,14,70).setSpellEffect(effectNormal.recycle).addToMap(spells);
		woodVeil = new Spell(Enums.WOOD,32,"Wood Veil","木遁",8,8,75).setSpellEffect(effectNormal.elemntVeil).addToMap(spells);
		lifeBoost = new Spell(Enums.WOOD,33,"Life Boost","ライフブースト",13,14,70).setSpellEffect(effectNormal.lifeBoost).addToMap(spells);
		callThunder = new Spell(Enums.WOOD,35,"Call Thunder","召雷",13,14,70).setSpellEffect(effectNormal.callThunder).setStrength(2, 1).addToMap(spells);
		meditation = new Spell(Enums.WOOD,36,"Meditation","メディテーション",13,14,80).setSpellEffect(effectNormal.meditation).setStrength(3, 0).addToMap(spells);

		overGrowth = new Spell(Enums.WOOD,34,"Over Growth","オーバーグロウス",13,14,70).setSpellEffect(effectNormal.overGrowth).addToMap(spells);
		metalVeil = new Spell(Enums.METAL,40,"Metal Veil","金遁",8,8,75).setSpellEffect(effectNormal.elemntVeil).addToMap(spells);
		detectGold = new Spell(Enums.METAL,41,"Detect Gold","デテクトゴールド",12,12,70).setSpellEffect(effectNormal.detectGold).addToMap(spells);
		magicLock = new Spell(Enums.METAL,42,"Magic Lock","マジックロック",10,10,72).setSpellEffect(effectNormal.magicLock).addToMap(spells);
		superSonic = new Spell(Enums.METAL,43,"Super Sonic","スーパーソニック",8,15,75).setSpellEffect(effectNormal.superSonic).setStrength(4, 0).addToMap(spells);
		//armorBless = new Spell(EnumElement.METAL,42,"Armor Blessing","アーマーブレス",13,14,70).addToMap(spellMap);
		//weaponBless = new Spell(EnumElement.METAL,43,"Weapon Blessing","ウエポンブレス",13,14,70).addToMap(spellMap);



		weakness = new Spell(Enums.FORBIDDEN,50,"Weakness","ウィークネス",10,10,70).setSpellEffect(effectNormal.weakness).addToMap(spells);
		abyss = new Spell(Enums.FORBIDDEN,51,"Seal of the Abyss","魔印",12,12,70).setSpellEffect(effectNormal.abyss).setStrength(5, 0).addToMap(spells);
		detectBlood = new Spell(Enums.FORBIDDEN,52,"Detect Blood","デテクトブラッド",10,8,75).setSpellEffect(effectNormal.detectBlood).addToMap(spells);



		crimsonFlare = (SpellBlend) new SpellBlend(Enums.FIRE,60,"Crimson Flare","クリムゾンフレア",25,70).setStrength(5, 2).setSpellEffect(effectBlend.crimsonFlare).addToMap(spells).addToBlenderSet(blendSet);
		stoneShower = (SpellBlend)new SpellBlend(Enums.EARTH,61,"Stone Shower","ストーンシャワー",25,70).setSpellEffect(effectBlend.stoneShower).setStrength(7, 2).addToMap(spells).addToBlenderSet(blendSet);
		detectTreasure = (SpellBlend)new SpellBlend(Enums.METAL,62,"Detect Treasire","デテクトトレジャー",15,70).setSpellEffect(effectBlend.detectTreasure).addToMap(spells).addToBlenderSet(blendSet);
		leavesShield = (SpellBlend)new SpellBlend(Enums.WOOD,63,"Leaves Shield","木の葉の盾",15,70).setSpellEffect(effectBlend.leavesShield).addToMap(spells).addToBlenderSet(blendSet);
		iceNine = (SpellBlend)new SpellBlend(Enums.WATER,64,"Ice Nine","アイスナイン",15,70).setSpellEffect(effectBlend.iceNine).addToMap(spells).addToBlenderSet(blendSet);
		touchGold = (SpellBlend)new SpellBlend(Enums.METAL,65,"Gold Finger","ゴールドフィンガー",20,70).setSpellEffect(effectBlend.goldFinger).addToMap(spells).addToBlenderSet(blendSet);
		thunderCrap = (SpellBlend)new SpellBlend(Enums.WATER,66,"Thunder crap","サンダークラップ",20,70).setSpellEffect(effectBlend.thudnerCrap).setStrength(5, 1).addToMap(spells).addToBlenderSet(blendSet);
		reflesh = (SpellBlend)new SpellBlend(Enums.WATER,67,"Reflesh","リフレッシュ",20,70).setSpellEffect(effectBlend.reflesh).setStrength(4, 0).addToMap(spells).addToBlenderSet(blendSet);
		restoration =(SpellBlend)new SpellBlend(Enums.WOOD,68,"Restoration","レストレーション",25,70).setSpellEffect(effectBlend.restoration).setStrength(10, 0).addToMap(spells).addToBlenderSet(blendSet);
	}


	public static Spells getInstance(){
		if(INSTANCE==null){
			INSTANCE = new Spells();
		}
		return INSTANCE;

	}
	
	public Set<SpellBlend> getAllBlendsMap(){
		return this.blendSet;
	}
	public Map<Integer,Spell> getMap(){
		return this.spells;
	}
	
	public List<Spell> getValidSpells(){
		List<Spell> list = new ArrayList();
		for(Spell spell:spells.values()){
			if(!(spell instanceof SpellBlend)){
				list.add(spell);
			}

		}

		return list;
	}

	public Spell getSpell(int par1){
		return spells.get(par1);
	}

	public void initBlenderData(){
		fireVeil.setSpellMixElements(new SpellMixTable(3,0,0,0,0,0));
		fireVeil.setSpellAmplifier(new SpellMixTable(15,15,-15,0,0,0),new SpellMixTable(15,15,-15,0,0,0));
		fireArrow.setSpellMixElements(new SpellMixTable(1,2,0,0,0,1));
		fireArrow.setSpellAmplifier(new SpellMixTable(10,20,10,-5,5,5),new SpellMixTable(15,25,15,0,10,10));
		heroism.setSpellMixElements(new SpellMixTable(2,2,0,0,1,0));
		heroism.setSpellAmplifier(new SpellMixTable(15,15,0,-10,5,0),new SpellMixTable(15,15,0,-10,5,0));
		crimsonFlare.addRequireBlend(fireArrow, new SpellMixTable(4,1,1,0,1,5));
		waterVeil.setSpellMixElements(new SpellMixTable(0,0,0,3,0,0));
		waterVeil.setSpellAmplifier(new SpellMixTable(-15,0,0,15,15,0),new SpellMixTable(-15,0,0,15,15,0));
		cloudCall.setSpellMixElements(new SpellMixTable(0,1,0,2,1,0));
		cloudCall.setSpellAmplifier(new SpellMixTable(-5,0,5,5,15,0),new SpellMixTable(-5,0,5,5,15,0));	
		purify.setSpellMixElements(new SpellMixTable(0,0,0,2,1,0));
		purify.setSpellAmplifier(new SpellMixTable(-5,5,0,10,15,0),new SpellMixTable(-5,5,0,10,15,0));
		iceNine.addRequireBlend(cloudCall, new SpellMixTable(0,4,0,4,0,4));
		stoneVeil.setSpellMixElements(new SpellMixTable(0,3,0,0,0,0));
		stoneVeil.setSpellAmplifier(new SpellMixTable(0,15,15,-15,0,0),new SpellMixTable(0,15,15,-15,0,0));
		boulder.setSpellMixElements(new SpellMixTable(0,2,1,0,0,1));
		boulder.setSpellAmplifier(new SpellMixTable(5,15,20,0,0,5),new SpellMixTable(10,20,25,5,5,10));
		animalCharm.setSpellMixElements(new SpellMixTable(1,3,0,0,2,0));
		animalCharm.setSpellAmplifier(new SpellMixTable(15,10,10,-15,10,0),new SpellMixTable(15,10,10,-15,10,0));
		stoneShower.addRequireBlend(boulder, new SpellMixTable(0,4,0,0,0,6));
		metalVeil.setSpellMixElements(new SpellMixTable(0,0,3,0,0,0));
		metalVeil.setSpellAmplifier(new SpellMixTable(0,0,15,15,-15,0),new SpellMixTable(0,0,15,15,-15,0));
		detectGold.setSpellMixElements(new SpellMixTable(1,0,2,0,0,0));
		detectGold.setSpellAmplifier(new SpellMixTable(5,5,5,10,-10,0),new SpellMixTable(5,5,5,10,-10,0));
		magicLock.setSpellMixElements(new SpellMixTable(0,0,2,1,0,1));
		magicLock.setSpellAmplifier(new SpellMixTable(0,5,15,20,0,5),new SpellMixTable(5,10,20,25,5,10));
		detectTreasure.addRequireBlend(detectGold, new SpellMixTable(0,0,5,2,0,2));
		woodVeil.setSpellMixElements(new SpellMixTable(0,0,0,0,3,0));
		woodVeil.setSpellAmplifier(new SpellMixTable(15,15,0,0,15,0),new SpellMixTable(15,15,0,0,15,0));
		overGrowth.setSpellMixElements(new SpellMixTable(0,1,0,1,2,0));
		overGrowth.setSpellAmplifier(new SpellMixTable(5,-5,5,0,15,0),new SpellMixTable(5,-5,5,0,15,0));
		lifeBoost.setSpellMixElements(new SpellMixTable(2,0,0,0,2,0));
		lifeBoost.setSpellAmplifier(new SpellMixTable(20,0,-10,0,10,0),new SpellMixTable(20,0,-10,0,10,0));		
		recycle.setSpellMixElements(new SpellMixTable(1,1,1,0,2,0));
		recycle.setSpellAmplifier(new SpellMixTable(15,0,5,0,5,0),new SpellMixTable(15,0,5,0,5,0));	
		missuileGuard.setSpellMixElements(new SpellMixTable(0,2,1,0,2,0));
		missuileGuard.setSpellAmplifier(new SpellMixTable(10,0,15,-5,5,0),new SpellMixTable(10,0,15,-5,5,0));
		weakness.setSpellMixElements(new SpellMixTable(0,0,2,0,0,2));
		weakness.setSpellAmplifier(new SpellMixTable(10,10,20,20,0,10),new SpellMixTable(20,20,30,30,10,20));
		abyss.setSpellMixElements(new SpellMixTable(0,2,0,0,0,2));
		abyss.setSpellAmplifier(new SpellMixTable(10,10,20,20,10,10),new SpellMixTable(20,30,30,10,20,20));	
		superSonic.setSpellMixElements(new SpellMixTable(0,0,3,2,0,1));
		superSonic.setSpellAmplifier(new SpellMixTable(-5,5,20,30,0,5), new SpellMixTable(0,10,25,35,5,10));
		detectBlood.setSpellMixElements(new SpellMixTable(2,0,0,0,0,2));
		detectBlood.setSpellAmplifier(new SpellMixTable(20,20,0,10,10,10),new SpellMixTable(30,30,10,20,20,20));
		meditation.setSpellMixElements(new SpellMixTable(1,1,0,1,3,0));
		meditation.setSpellAmplifier(new SpellMixTable(15,0,5,0,5,0),new SpellMixTable(15,0,5,0,5,0));
		leavesShield.addRequireBlend(missuileGuard, new SpellMixTable(0,2,0,2,3,0));
		//		armorBless.setSpellMixElements(new SpellMixTable(0,2,2,1,0,0));
		//		armorBless.setSpellAmplifier(new SpellMixTable(-5,10,20,5,-5,0),new SpellMixTable(-5,10,20,5,-5,0));
		//		weaponBless.setSpellMixElements(new SpellMixTable(0,2,2,1,0,0));
		//		weaponBless.setSpellAmplifier(new SpellMixTable(-5,10,20,5,-5,0),new SpellMixTable(-5,10,20,5,-5,0));
		touchGold.addRequireBlend(detectGold, new SpellMixTable(0,0,5,0,0,4));
		touchGold.addRequireBlend(magicLock, new SpellMixTable(2,0,4,0,0,3));
		fireWall.setSpellMixElements(new SpellMixTable(3,0,1,1,1,0));
		fireWall.setSpellAmplifier(new SpellMixTable(15,10,-10,10,5,0),new SpellMixTable(15,10,-10,10,5,0));
		fireStorm.setSpellMixElements(new SpellMixTable(3,1,0,0,1,1));
		fireStorm.setSpellAmplifier(new SpellMixTable(25,20,-5,0,10,5),new SpellMixTable(30,25,0,5,15,10));
		thunderCrap.addRequireBlend(cloudCall, new SpellMixTable(0,2,2,3,2,2));
		reflesh.addRequireBlend(purify, new SpellMixTable(0,0,0,4,5,0));
		waterShield.setSpellMixElements(new SpellMixTable(0,1,1,2,1,0));
		waterShield.setSpellAmplifier(new SpellMixTable(-5,0,10,10,10,0),new SpellMixTable(-5,0,10,10,10,0));
		buildUp.setSpellMixElements(new SpellMixTable(1,2,1,0,0,0));
		buildUp.setSpellAmplifier(new SpellMixTable(5,15,10,-5,-5,0),new SpellMixTable(5,15,10,-5,-5,0));
		detectAnimal.setSpellMixElements(new SpellMixTable(0,2,0,1,1,0));
		detectAnimal.setSpellAmplifier(new SpellMixTable(0,5,10,-5,10,0),new SpellMixTable(0,5,10,-5,10,0));
		callThunder.setSpellMixElements(new SpellMixTable(1,1,0,1,3,0));
		callThunder.setSpellAmplifier(new SpellMixTable(10,0,-10,10,10,0),new SpellMixTable(10,0,-10,10,10,0));
		restoration.addRequireBlend(overGrowth, new SpellMixTable(0,3,0,2,4,0));
		restoration.addRequireBlend(meditation, new SpellMixTable(2,2,0,2,3,0));
		restoration.addRequireBlend(lifeBoost, new SpellMixTable(3,0,0,2,4,0));
	}

	public void init() {
		initBlenderData();
		fireWall.setUsePointer(true);
		if(Unsaga.debug){
			for(Spell spell:spells.values()){
				if(spell.getSpellMixElements()==null && !(spell instanceof SpellBlend)){
					Unsaga.debug(spell.nameJp+"の合成五行値がありません");
					spell.setSpellMixElements(fireWall.getSpellMixElements());
					spell.setSpellAmplifier(fireWall.getAmp(),fireWall.getCost());
				}
				if((spell instanceof SpellBlend)){
					if(((SpellBlend) spell).getRequireMap().isEmpty()){
						Unsaga.debug(spell.nameJp+"の要求五行値がありません");
						((SpellBlend) spell).addRequireBlend(fireWall, new SpellMixTable(0,0,3,0,3,0));
					}
				}
			}
		}

	}

}
