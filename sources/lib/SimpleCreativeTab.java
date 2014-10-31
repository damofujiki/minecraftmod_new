package com.hinasch.lib;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SimpleCreativeTab extends CreativeTabs {

	protected Item iconItem;
	protected ItemStack iconItemStack;
	public SimpleCreativeTab(String unlname) {
		super(unlname);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public CreativeTabs setIconItem(Item par1){
		this.iconItem = par1;
		return this;
	}
	@Override
	public Item getTabIconItem() {
		// TODO 自動生成されたメソッド・スタブ
		return this.iconItem;
	}

	@Override
    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack()
    {
        if(this.iconItemStack!=null){
        	return this.iconItemStack;
        }
        return super.getIconItemStack();
    }
    
	public CreativeTabs setIconItemStack(ItemStack is){
		this.iconItemStack = is;
		return this;
	}
	public static SimpleCreativeTab getSimpleCreativeTab(String unlname){
		return new SimpleCreativeTab(unlname);
	}
	
	public static void setIconItem(CreativeTabs tab,Item par1){
		((SimpleCreativeTab)tab).setIconItem(par1);
	}
	public static void setIconItemStack(CreativeTabs tab,ItemStack par1){
		((SimpleCreativeTab)tab).setIconItemStack(par1);
	}
}
