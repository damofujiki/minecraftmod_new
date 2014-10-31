package com.hinasch.unlsagamagic.element;



import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import com.hinasch.lib.library.LibraryBook;
import com.hinasch.unlsagamagic.spell.SpellMixTable;

public class ElementLibraryBook extends LibraryBook{


	public SpellMixTable table;
	public Block block;
	public static final int _CLASS = 0;
	public static final int BLOCK = 1;
	public static final int MATERIAL = 2;
	public static final int BIOMETYPE = 3;
	public int childkey;
	public Material material;
	public Class _class;
	public BiomeDictionary.Type biomeType;

	public <T> ElementLibraryBook(T par1,SpellMixTable elementTable) {
		super(par1);
		if(par1 instanceof Material){
			this.childkey = MATERIAL;
			this.material = (Material)par1;
		}
		if(par1 instanceof BiomeDictionary.Type){
			this.childkey = BIOMETYPE;
			this.biomeType = (Type) par1;
		}
		if(par1 instanceof Block){
			this.childkey = BLOCK;
			this.block = (Block)par1;
		}
		if(par1 instanceof Class){
			this.childkey = _CLASS;
			this._class = (Class)par1;
		}
		
		this.table = elementTable;
	}
	

	

}
