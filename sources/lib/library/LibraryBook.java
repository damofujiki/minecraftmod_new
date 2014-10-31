package com.hinasch.lib.library;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;

import com.google.common.base.Optional;
import com.hinasch.lib.PairID;

public class LibraryBook {

	public static enum EnumSelector {ITEM,ITEMSTACK,STRING,TOOLMATERIAL,ARMORMATERIAL,_CLASS,BLOCK};
	protected EnumSelector key;
	protected boolean isAllMetadata = false;
	protected Optional<String> orekey = Optional.absent();
	protected Optional<Item.ToolMaterial> enumtool = Optional.absent();
	protected Optional<ItemArmor.ArmorMaterial> enumarmor = Optional.absent();
	protected Optional<PairID> idAndMeta = Optional.absent();
	protected Optional<Class> classstore = Optional.absent();
	
	public <T> LibraryBook(T par1){
		if(par1 instanceof Block){

			this.key = EnumSelector.ITEMSTACK;
			this.isAllMetadata = true;
			ItemStack is = new ItemStack((Block)par1,1);
			idAndMeta = Optional.of(new PairID(is.getItem(),is.getItemDamage()));
			this.isAllMetadata = true;

		}
		if(par1 instanceof String){
			this.key = EnumSelector.STRING;
			this.orekey = Optional.of((String)par1);

		}
		if(par1 instanceof Item){
			this.key = EnumSelector.ITEMSTACK;
			this.isAllMetadata = true;
			ItemStack is = new ItemStack((Item)par1,1);
			idAndMeta = Optional.of(new PairID(is.getItem(),is.getItemDamage()));

		}
		if(par1 instanceof ItemStack){
			this.key = EnumSelector.ITEMSTACK;
			ItemStack is = (ItemStack)par1;
			idAndMeta = Optional.of(new PairID(is.getItem(),is.getItemDamage()));

		}
		if(par1 instanceof ToolMaterial){
			this.key = EnumSelector.TOOLMATERIAL;
			this.enumtool = Optional.of((ToolMaterial)par1);

		}
		if(par1 instanceof ArmorMaterial){
			this.key = EnumSelector.ARMORMATERIAL;
			this.enumarmor = Optional.of((ArmorMaterial)par1);

		}
		if(par1 instanceof Class){
			this.key = EnumSelector._CLASS;
			this.classstore = Optional.of((Class)par1);

		}
	}
	
	

	
	public EnumSelector getKey(){
		return this.key;
	}
}
