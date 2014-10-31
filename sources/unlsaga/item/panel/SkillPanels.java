package com.hinasch.unlsaga.item.panel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

import com.google.common.collect.Sets;
import com.hinasch.lib.UtilCollection;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.event.extendeddata.WorldSaveDataUnsaga;
import com.hinasch.unlsaga.util.damage.DamageSourceUnsaga;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class SkillPanels {

	protected Map<Integer,PanelData> allPanels;

	public PanelData punch,kick,throwing,weaponMaster,punchMaster;
	public PanelData superArmor,toughness,dauntless;
	public PanelData familiarFire,familiarWater,familiarEarth,familiarWood,familiarMetal;
	public PanelData magicExpert,savingDamage;
	public PanelData zombiePhobia,creeperPhobia;
	public PanelData spellBlend;
	public PanelData roadAdviser,cavernExprorer,knowledgeBuildings;
	public PanelData swimming,avoidTrap;
	public PanelData unlock,defuse,penetration;
	public PanelData discount,gratuity,fashionable;
	public PanelData luckyFind;
	public PanelData easyRepair;
	public PanelData divination,knowledgeAncient;
	public PanelData noKillingAnimals;

	public static final int RARITY_MIRACLE = 2;
	public static final int RARITY_ULTRARARE = 5;
	public static final int RARITY_RARE = 10;
	public static final int RARITY_UNCOMMON = 20;
	public static final int RARITY_COMMON = 30;

	public WeightedRandom.Item[] weightedLevels;

	public SkillPanels(){
		allPanels = new HashMap();

	}

	public Collection<PanelData> getPanels(){
		return this.allPanels.values();
	}

	public List<WeightedRandomPanel> getWeightedRandomPanels(){
		List<WeightedRandomPanel>list = new ArrayList();
		for(PanelData data:this.getPanels()){
			for(int i=0;i<5;i++){
				list.add(new WeightedRandomPanel(data.getRarity(),data,i));
			}

		}
		return list;
	}

	//ネガティブスキルまわりがまだ

	public void init(){
		this.punch = new PanelData(0,"punch").setIconNumber(1);
		this.knowledgeAncient = new PanelData(1,"knowledgeAncient").setIconNumber(2);
		this.spellBlend = new PanelData(2,"spellBlend").setIconNumber(2).setRarity(RARITY_ULTRARARE);
		this.unlock = new PanelData(3,"unlock");
		this.defuse = new PanelData(4,"defuse");
		this.penetration = new PanelData(5,"penetration");
		this.luckyFind = new PanelData(6,"luckyFind").setIconNumber(3);
		this.discount = new PanelData(7,"discount").setIconNumber(3);
		this.gratuity = new PanelData(8,"gratuity").setIconNumber(3);
		this.divination = new PanelData(9,"divination").setIconNumber(2);
		this.zombiePhobia = new PanelData(10,"zombiePhobia").setIconNumber(4).setNegativeSkill(true).setRarity(RARITY_RARE);
		this.creeperPhobia = new PanelData(11,"creeperPhobia").setIconNumber(4).setNegativeSkill(true).setRarity(RARITY_RARE);
		this.noKillingAnimals = new PanelData(12,"noKillingAnimals").setIconNumber(4).setNegativeSkill(true).setRarity(RARITY_RARE);
		this.superArmor = new PanelData(13,"superArmor").setIconNumber(1).setRarity(RARITY_RARE);
		this.toughness = new PanelData(14,"toughness").setIconNumber(1).setRarity(RARITY_UNCOMMON);
		this.roadAdviser = new PanelData(15,"roadAdviser").setIconNumber(1);
		this.cavernExprorer = new PanelData(16,"cavernExprorer").setIconNumber(1);
		this.avoidTrap = new PanelData(17,"agility");;
		this.weaponMaster = new PanelData(18,"weaponMaster").setIconNumber(1).setRarity(RARITY_ULTRARARE);

	}

	public PanelData getData(int num){
		return this.allPanels.get(num);
	}

	public Map<Integer,PanelData> getMap(){
		return allPanels;
	}
	public static class PanelData{

		public final int id;
		public final String name;

		public int rarity;
		public int bonusElements;
		public int bonusPower;
		public int iconNum;

		public boolean isNegativeSkill;

		public PanelData(int id,String name){
			this.id = id;
			this.name = "panel."+name;
			this.iconNum = 0;
			this.rarity = RARITY_COMMON;
			this.isNegativeSkill = false;

			Unsaga.skillPanels.getMap().put(id, this);
		}

		
		public PanelData(int id,String name,int rarity){
			this(id,name);
			this.rarity = rarity;
		}

		public int getRarity(){
			return this.rarity;
		}
		public PanelData setNegativeSkill(boolean par1){
			this.isNegativeSkill = par1;
			return this;
		}
		public String getName(){
			return this.name;
		}

		public int getID(){
			return this.id;
		}

		public int getIconNumber(){
			return this.iconNum;
		}


		public PanelData setRarity(int par1){
			this.rarity = par1;
			return this;
		}
		public PanelData setIconNumber(int num){
			this.iconNum = num;
			return this;
		}

		public ItemStack getItemStack(){
			return new ItemStack(Unsaga.items.skillPanels,1,this.id);
		}

		@Override
		public String toString(){
			return this.id+":"+this.name;
		}
	}

	public static class WeightedRandomPanel extends WeightedRandom.Item{

		public final  SkillPanels.PanelData panel;
		public int level;
		public WeightedRandomPanel(int par1,SkillPanels.PanelData data) {
			super(par1);
			this.panel = data;
			// TODO 自動生成されたコンストラクター・スタブ
		}

		public WeightedRandomPanel(int par1,SkillPanels.PanelData data,int level){
			this(par1,data);
			this.level = level;

		}

		@Override
		public String toString(){
			return "["+this.panel.getName()+" level:"+this.level+" weight:"+this.itemWeight+"]";
		}
	}

	public static boolean hasPanel(World world,EntityPlayer ep,PanelData panel){
		return getHighestLevelOfPanel(world, ep, panel)>=0;
	}
	/*
	 * 
	 *  return (level-1)
	 *  (level 1 = 0 )
	 */
	public static int getHighestLevelOfPanel(World world,EntityPlayer ep,PanelData panel){
		return WorldSaveDataUnsaga.getHighestLevelOfPanel(world, ep, panel);
	}


	public static class EventBreakSpeed{

		public static final Set<Material> woodMaterials = Sets.newHashSet(Material.vine,Material.plants,Material.wood);
		public static final Set<Material> pickaxeMaterials = Sets.newHashSet(Material.iron,Material.rock);
		public static final Set<Block> field_150916_c = Sets.newHashSet(new Block[] {Blocks.grass, Blocks.dirt, Blocks.sand, Blocks.gravel, Blocks.snow_layer, Blocks.snow, Blocks.clay, Blocks.farmland, Blocks.soul_sand, Blocks.mycelium});	
		
		public static EventBreakSpeed getEvent(){
			return new EventBreakSpeed();
		}
		@SubscribeEvent
		public void breakSpeedEvent(BreakSpeed e){
			EntityPlayer ep = e.entityPlayer;
			World world = ep.worldObj;
			ItemStack is = ep.getHeldItem();
			Item item = is!=null? is.getItem() : null;
			Material material = e.block.getMaterial();
			Unsaga.debug(e.originalSpeed);
			if(hasPanel(world,ep,Unsaga.skillPanels.roadAdviser)){
				if(item instanceof ItemAxe && UtilCollection.collectionContains(material, woodMaterials) ){
					e.newSpeed = e.originalSpeed + (0.7F * (getHighestLevelOfPanel(world, ep, Unsaga.skillPanels.roadAdviser)+1));
					Unsaga.debug(e.newSpeed);
				}
				if(item instanceof ItemSpade && UtilCollection.collectionContains(e.block, field_150916_c) ){
					e.newSpeed = e.originalSpeed + (0.7F * (getHighestLevelOfPanel(world, ep, Unsaga.skillPanels.roadAdviser)+1));
					Unsaga.debug(e.newSpeed);
				}
				if(is==null && UtilCollection.collectionContains(e.block, field_150916_c)){
					e.newSpeed = e.originalSpeed + (0.7F * (getHighestLevelOfPanel(world, ep, Unsaga.skillPanels.roadAdviser)+1));
					Unsaga.debug(e.newSpeed);
				}
			}
			if(hasPanel(world,ep,Unsaga.skillPanels.cavernExprorer)){
				if(item instanceof ItemPickaxe && UtilCollection.collectionContains(material, pickaxeMaterials) ){
					e.newSpeed = e.originalSpeed + (0.7F * (getHighestLevelOfPanel(world, ep, Unsaga.skillPanels.cavernExprorer)+1));
					Unsaga.debug(e.newSpeed);
				}
			}
		}
	}

	public static void onLivingHurt(LivingHurtEvent e){
		Entity attacker = e.source.getEntity();
		EntityLivingBase entityHurt = e.entityLiving;
		if(attacker instanceof EntityPlayer){
			EntityPlayer ep = (EntityPlayer) attacker;
			DamageSourceUnsaga uds = new DamageSourceUnsaga(DamageSource.causeIndirectMagicDamage(null, null));
			if(getHighestLevelOfPanel(ep.worldObj, ep, Unsaga.skillPanels.creeperPhobia)>=0 && entityHurt instanceof EntityCreeper){

				e.ammount -= e.ammount * ((0.1 * getHighestLevelOfPanel(ep.worldObj, ep, Unsaga.skillPanels.creeperPhobia)+1));
			}
			if(getHighestLevelOfPanel(ep.worldObj, ep, Unsaga.skillPanels.zombiePhobia)>=0 && entityHurt instanceof EntityZombie){

				e.ammount -= e.ammount * ((0.1 * getHighestLevelOfPanel(ep.worldObj, ep, Unsaga.skillPanels.zombiePhobia)+1));
			}

			if(e.ammount<0){
				e.ammount = 1;
			}


		}



		if(entityHurt instanceof EntityPlayer){
			EntityPlayer ep = (EntityPlayer) entityHurt;
			if(!e.source.isUnblockable() && !e.source.isFireDamage() && !e.source.isMagicDamage())
			if(getHighestLevelOfPanel(ep.worldObj,ep,Unsaga.skillPanels.superArmor)>=0){
				if(!e.source.isMagicDamage() && !e.source.isUnblockable()){
					e.ammount -= 0.5F * (getHighestLevelOfPanel(ep.worldObj,ep,Unsaga.skillPanels.superArmor) + 1);

				}
			}
			if(attacker==null && e.source.isMagicDamage()){
				if(getHighestLevelOfPanel(ep.worldObj,ep,Unsaga.skillPanels.toughness)>=0){
					if(!e.source.isMagicDamage() && !e.source.isUnblockable()){
						e.ammount -= 0.5F * (getHighestLevelOfPanel(ep.worldObj,ep,Unsaga.skillPanels.toughness) + 1);

					}
				}
			}
			
			if(e.ammount<0){
				e.ammount = 0.5F;
			}
		}


	}
}
