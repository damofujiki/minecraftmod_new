package com.hinasch.unlsaga.item.tool;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.google.common.collect.Multimap;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.item.IUnsagaMaterialTool;
import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;
import com.hinasch.unlsaga.material.UnsagaMaterial;

public class ItemPickaxeUnsaga extends ItemPickaxe implements IUnsagaMaterialTool{
	

	protected final float weaponDamage;
	protected IIcon[] icons;
	protected UnsagaMaterial unsagaMaterial;
	
	public ItemPickaxeUnsaga(UnsagaMaterial us) {
		super(us.getToolMaterial());
		this.helper.init(us, itemIcon, getCategory());
		this.icons = new IIcon[2];
		this.unsagaMaterial = us;
		this.weaponDamage = 2.0F + us.getToolMaterial().getDamageVsEntity();
		this.helper.registerItem(this);
	}

	@Override
	public ToolCategory getCategory() {
		return ToolCategory.PICKAXE;
	}


	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		helper.addUnsagaItemInfo(par1ItemStack, par2EntityPlayer, par3List, par4);
	}
	
	@Override
	public IIcon getIconFromDamageForRenderPass(int par1, int par2)
	{
		if(par2==0){
			return this.icons[0];
		}
		return this.icons[1];
	
	}
	
	@Override
	public void registerIcons(IIconRegister par1IconRegister)
	{
		this.icons[0] = par1IconRegister.registerIcon(Unsaga.DOMAIN+":"+"pickaxe_1");
		this.icons[1] = par1IconRegister.registerIcon(Unsaga.DOMAIN+":"+"pickaxe_2");
	}
	
	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
	{
		return helper.getColorFromItemStack(par1ItemStack, par2);
	}
	
	@Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
		return helper.getIsRepairable(par1ItemStack, par2ItemStack) ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
    }
    

    
	@Override
    public Multimap getItemAttributeModifiers()
    {
		return this.helper.getItemAttributeModifiers(field_111210_e, weaponDamage);

    }
	
	@Override
	public UnsagaMaterial getMaterial() {
		// TODO 自動生成されたメソッド・スタブ
		return this.unsagaMaterial;
	}
	
	@Override
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}

}
