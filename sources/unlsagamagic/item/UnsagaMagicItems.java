package com.hinasch.unlsagamagic.item;

import com.hinasch.unlsaga.Unsaga;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;

public class UnsagaMagicItems {

	public Item magicTablet;
	public Item spellBook;
	public Item blender;

	
	public void register(){
		CreativeTabs tabMagic = Unsaga.magic.tabMagic;
		
		magicTablet = new ItemTablet().setCreativeTab(tabMagic).setUnlocalizedName("unsaga.magictablet");
		spellBook = new ItemSpellBook().setCreativeTab(tabMagic).setUnlocalizedName("unsaga.spellbook");
		blender = new ItemBlender().setCreativeTab(tabMagic).setUnlocalizedName("unsaga.blender");
		

		GameRegistry.registerItem(magicTablet, "itemMagicTablet");
		GameRegistry.registerItem(spellBook, "itemSpellBook");
		GameRegistry.registerItem(blender, "itemBlender");
	}
}
