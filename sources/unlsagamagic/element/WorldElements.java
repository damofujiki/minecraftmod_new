package com.hinasch.unlsagamagic.element;

import java.util.List;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.collect.Sets;
import com.hinasch.lib.HSLibs;
import com.hinasch.lib.ScanHelper;
import com.hinasch.lib.XYZPos;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.HelperAbility;
import com.hinasch.unlsaga.debuff.Debuffs;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingDebuff;
import com.hinasch.unlsaga.util.FiveElements;
import com.hinasch.unlsagamagic.spell.SpellMixTable;

public class WorldElements {
	protected final SpellMixTable worldElementTable;

	protected static WorldElements instance;

	protected WorldElements(){
		this.worldElementTable = new SpellMixTable();
	}

	public static WorldElements getInstance(){
		if(instance==null){
			instance = new WorldElements();
		}
		return instance;
		
	}
	public SpellMixTable getElementsTableFromWorld(){
		return this.worldElementTable;
	}
	
	public SpellMixTable getFiguredTable(World world,EntityLivingBase ep){
		this.figureElements(world, ep);
		return this.getElementsTableFromWorld();
	}
	public void figureElements(World world,EntityLivingBase ep){
		this.worldElementTable.reset();
		this.figureFromWorld(world, ep);
		this.figureFromBiomeTypeAndWeather(world, ep);
		this.figureFromEquipment(ep);
		this.figureFromCurrentHeight(world, ep);
		//this.worldElementTable.multiple(0.01F);
		this.figureFromBuff(ep);
		this.worldElementTable.cut(0, 100);


		//this.worldElementTable.cut(0, 100);
	}

	private void figureFromBuff(EntityLivingBase ep) {
		Debuffs debuffs = Unsaga.debuffManager;
		if(LivingDebuff.hasDebuff(ep, debuffs.fireVeil))this.worldElementTable.add(FiveElements.Enums.FIRE,15.0F);
		if(LivingDebuff.hasDebuff(ep, debuffs.woodVeil))this.worldElementTable.add(FiveElements.Enums.WOOD,15.0F);
		if(LivingDebuff.hasDebuff(ep, debuffs.waterVeil))this.worldElementTable.add(FiveElements.Enums.WATER,15.0F);
		if(LivingDebuff.hasDebuff(ep, debuffs.earthVeil))this.worldElementTable.add(FiveElements.Enums.EARTH,15.0F);
		if(LivingDebuff.hasDebuff(ep, debuffs.metalVeil))this.worldElementTable.add(FiveElements.Enums.METAL,15.0F);
	}

	protected void figureFromWorld(World world,EntityLivingBase ep){
		XYZPos epos = XYZPos.entityPosToXYZ(ep);
		ScanHelper scanner = new ScanHelper(ep,10,8);
		boolean flag = false;
		scanner.setWorld(world);
		for(;scanner.hasNext();scanner.next()){
			flag = false;

			if(!scanner.isAirBlock() && scanner.sy>0){

				if(scanner.getBlock() instanceof IUnsagaElements){
					IUnsagaElements iu = (IUnsagaElements)scanner.getBlock();
					SpellMixTable table = iu.getElements();
					this.worldElementTable.add(table);
					flag = true;
				}
				if(Unsaga.magic.elementLibrary.find(scanner.getBlock()).isPresent() && !flag){
					ElementLibraryBook book = (ElementLibraryBook) Unsaga.magic.elementLibrary.find(scanner.getBlock()).get();
					this.worldElementTable.add(book.table);
					flag = true;
				}

				Item item = Item.getItemFromBlock(scanner.getBlock());
				if(item!=null){
					ItemStack is = new ItemStack(item,1,scanner.getMetadata());
					//Unsaga.debug(is,this.getClass());
					List<String> orenames = HSLibs.getOreNames(OreDictionary.getOreIDs(is));
					boolean isOre  = false;

					for(String orename:orenames){
						if(orename.contains("ore")){
							isOre = true;
						}
					}
					if(isOre){
						this.worldElementTable.add(FiveElements.Enums.METAL,0.1F);

					}
				}
				//scanner.setBlockHere(Block.blockClay.blockID);
			}
		}


	}

	protected void figureFromBiomeTypeAndWeather(World world,EntityLivingBase ep){
		BiomeGenBase biomegen = world.getBiomeGenForCoords((int)ep.posX, (int)ep.posZ);
		Set<BiomeDictionary.Type> biomeTypes = Sets.newHashSet(BiomeDictionary.getTypesForBiome(biomegen));


		for(BiomeDictionary.Type type:biomeTypes){
			if(Unsaga.magic.elementLibrary.find(type).isPresent()){
				ElementLibraryBook book = (ElementLibraryBook) Unsaga.magic.elementLibrary.find(type).get();
				this.worldElementTable.add(book.table);
			}
		}

		if(world.isRaining()){
			this.worldElementTable.add(FiveElements.Enums.WATER, 10);
		}
		if(world.isDaytime()){
			this.worldElementTable.add(FiveElements.Enums.FIRE, 10);
		}else{
			this.worldElementTable.add(FiveElements.Enums.FORBIDDEN, 10);
		}

	}

	protected void figureFromEquipment(EntityLivingBase ep){
		int fire = HelperAbility.getAmountAbility(ep, Unsaga.abilityManager.supportFire)*10;
		int wood = HelperAbility.getAmountAbility(ep, Unsaga.abilityManager.supportWood)*10;
		int water = HelperAbility.getAmountAbility(ep, Unsaga.abilityManager.supportWater)*10;
		int earth = HelperAbility.getAmountAbility(ep, Unsaga.abilityManager.supportEarth)*10;
		int metal = HelperAbility.getAmountAbility(ep, Unsaga.abilityManager.supportMetal)*10;
		int forbidden = HelperAbility.getAmountAbility(ep, Unsaga.abilityManager.supportForbidden)*10;
		this.worldElementTable.add(new SpellMixTable(fire,earth,metal,water,wood,forbidden));

	}

	protected void figureFromCurrentHeight(World world,EntityLivingBase ep){
		int height = (int)ep.posY;
		if(height<=10){
			this.worldElementTable.add(FiveElements.Enums.EARTH,50);
			return;
		}
		if(height<=20){
			this.worldElementTable.add(FiveElements.Enums.EARTH,36);
			return;
		}
		if(height<=30){
			this.worldElementTable.add(FiveElements.Enums.EARTH,26);
			return;
		}
		if(height<=40){
			this.worldElementTable.add(FiveElements.Enums.EARTH,16);
			return;
		}
		if(height<=50){
			this.worldElementTable.add(FiveElements.Enums.EARTH,8);
			return;
		}
		if(height<=60){
			this.worldElementTable.add(FiveElements.Enums.EARTH,5);
			return;
		}





	}


	public String getElementsTableFromWorldByString(){
		return this.worldElementTable.toString();
	}
	
	

}
