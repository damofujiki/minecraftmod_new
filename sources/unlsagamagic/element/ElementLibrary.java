package com.hinasch.unlsagamagic.element;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import com.google.common.base.Optional;
import com.hinasch.lib.library.LibraryBook;
import com.hinasch.lib.library.LibraryShelf;
import com.hinasch.unlsaga.util.FiveElements;
import com.hinasch.unlsagamagic.spell.SpellMixTable;

public class ElementLibrary extends LibraryShelf{



	
	public ElementLibrary(){

		
		put(Material.water, new SpellMixTable(-0.05F,0,0,0.5F,0,0));
		put(Material.snow, new SpellMixTable(-0.04F,0,0,0.5F,0,0));
		put(Material.ice, new SpellMixTable(-0.04F,0,0,0.5F,0,0));
		put(Material.craftedSnow, new SpellMixTable(-0.04F,0,0,0.8F,0,0));
		put(Material.grass, new SpellMixTable(FiveElements.Enums.WOOD,0.1F));
		put(Material.wood, new SpellMixTable(FiveElements.Enums.WOOD,0.05F));
		put(Material.plants, new SpellMixTable(FiveElements.Enums.WOOD,0.02F));
		put(Material.lava, new SpellMixTable(FiveElements.Enums.FIRE,0.08F));
		put(Material.leaves, new SpellMixTable(FiveElements.Enums.WOOD,0.01F));
		put(Material.iron, new SpellMixTable(FiveElements.Enums.METAL,1.0F));
		
		put(Blocks.lava, new SpellMixTable(FiveElements.Enums.FIRE,1.0F));
		put(Blocks.flowing_lava, new SpellMixTable(FiveElements.Enums.FIRE,0.5F));
		put(Blocks.fire, new SpellMixTable(FiveElements.Enums.FIRE,1.0F));
		put(Blocks.flowing_water, new SpellMixTable(-0.04F,0,0,0.5F,0,0));
		put(Blocks.water, new SpellMixTable(-0.04F,0,0,0.8F,0,0));
		put(Blocks.log, new SpellMixTable(FiveElements.Enums.WOOD,0.1F));
		put(Blocks.log2, new SpellMixTable(FiveElements.Enums.WOOD,0.1F));
		put(Blocks.planks, new SpellMixTable(FiveElements.Enums.WOOD,0.1F));
		put(Blocks.netherrack, new SpellMixTable(FiveElements.Enums.FORBIDDEN,0.01F));
		put(Blocks.nether_brick, new SpellMixTable(FiveElements.Enums.FORBIDDEN,0.05F));
		put(Blocks.end_stone, new SpellMixTable(FiveElements.Enums.FORBIDDEN,0.1F));
		
		put(BlockOre.class, new SpellMixTable(FiveElements.Enums.METAL,0.2F));
		
		put(Type.HOT,new SpellMixTable(FiveElements.Enums.FIRE,10));
		put(Type.BEACH,new SpellMixTable(FiveElements.Enums.WATER,10));
		put(Type.DRY,new SpellMixTable(FiveElements.Enums.WATER,-10));
		put(Type.SANDY,new SpellMixTable(FiveElements.Enums.WATER,-5));
		put(Type.FOREST,new SpellMixTable(FiveElements.Enums.WOOD,10));
		put(Type.SNOWY,new SpellMixTable(FiveElements.Enums.WATER,10));
		put(Type.MAGICAL,new SpellMixTable(FiveElements.Enums.FORBIDDEN,10));
		put(Type.WASTELAND,new SpellMixTable(FiveElements.Enums.EARTH,20));
		put(Type.HILLS,new SpellMixTable(FiveElements.Enums.EARTH,10));
		put(Type.MOUNTAIN,new SpellMixTable(FiveElements.Enums.EARTH,10));
		put(Type.JUNGLE,new SpellMixTable(FiveElements.Enums.WOOD,15));
		put(Type.SWAMP,new SpellMixTable(0,5.0F,0,5.0F,0,0));
		put(Type.NETHER,new SpellMixTable(FiveElements.Enums.FORBIDDEN,30));
		put(Type.NETHER,new SpellMixTable(FiveElements.Enums.FORBIDDEN,40));
	}
	
	public <T> void put(T obj,SpellMixTable table){
		this.addShelf(new ElementLibraryBook(obj,table));
	}
	@Override
	public Optional<LibraryBook> find(Object object){
		LibraryBook returnbook = null;
		if(object instanceof Block){
			Block block = (Block)object;
			Material material = block.getMaterial();
			for(LibraryBook book:libSet){
				ElementLibraryBook bookelement = (ElementLibraryBook)book;
				if(bookelement.childkey==bookelement.MATERIAL && material==bookelement.material){
					returnbook = bookelement;
				}
				if(bookelement.childkey==bookelement.BLOCK && bookelement.block==block){
					returnbook = bookelement;
				}
				if(bookelement.childkey==bookelement._CLASS && sameOrInstanceOf(bookelement._class,block.getClass())){
					returnbook = bookelement;
				}

			}
			
		}
		if(object instanceof BiomeDictionary.Type){
			BiomeDictionary.Type type = (BiomeDictionary.Type)object;
			for(LibraryBook book:libSet){
				ElementLibraryBook bookelement = (ElementLibraryBook)book;
				if(bookelement.childkey==bookelement.BIOMETYPE && bookelement.biomeType==type){
					returnbook = bookelement;
				}
			}
		}
		if(returnbook!=null){
			return Optional.of(returnbook);
		}
		return Optional.absent();
		//return super.find(object);
	}

	public boolean sameOrInstanceOf(Class class1,Class class2){
		if(class1.isInstance(class2)){
			return true;
		}
		if(class2.isInstance(class1)){
			return true;
		}
		if(class1==class2){
			return true;
		}
		return false;
	}


}
