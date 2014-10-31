package com.hinasch.unlsaga.item.weapon.base;

import java.util.HashMap;
import java.util.List;

import javax.swing.Icon;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.google.common.collect.Multimap;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.HelperAbility;
import com.hinasch.unlsaga.ability.IAbility;
import com.hinasch.unlsaga.item.IUnsagaMaterialTool;
import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;
import com.hinasch.unlsaga.material.UnsagaMaterial;

public class ItemAxeBase extends ItemAxe implements IUnsagaMaterialTool,IAbility{



	protected final HashMap<String,Icon> iconMap = new HashMap();
	protected IIcon[] icons;
	public final UnsagaMaterial unsMaterial;
	public final float weapondamage;

	public ItemAxeBase(UnsagaMaterial mat) {
		super(mat.getToolMaterial());
		this.unsMaterial = mat;
		this.icons = new IIcon[2];
		this.weapondamage = 3.0F + mat.getToolMaterial().getDamageVsEntity() + mat.getSuitedModifier(getCategory());
		this.helper.init(this.unsMaterial,this.itemIcon,ToolCategory.AXE);
		this.canRepair = true;
		
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		helper.addUnsagaItemInfo(par1ItemStack, par2EntityPlayer, par3List, par4);
	}

	@Override
	public ToolCategory getCategory() {
		// TODO 自動生成されたメソッド・スタブ
		return ToolCategory.AXE;
	}

	@Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
		return helper.getIsRepairable(par1ItemStack, par2ItemStack) ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
    }
	
	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
	{
		return helper.getColorFromItemStack(par1ItemStack, par2);
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
	public EnumAction getItemUseAction(ItemStack par1ItemStack)
	{

		if(HelperAbility.hasAbilityFromItemStack(Unsaga.abilityManager.tomahawk, par1ItemStack)){

			return EnumAction.bow;
		}
		return EnumAction.none;
	}

	@Override
	public UnsagaMaterial getMaterial() {
		// TODO 自動生成されたメソッド・スタブ
		return this.unsMaterial;
	}

//	@Override
//	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
//	{
//		helper.getSubItems(par1, par2CreativeTabs, par3List);
//
//	}

	@Override
	public int getMaxAbility() {
		// TODO 自動生成されたメソッド・スタブ
		return 1;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
	{
		if(HelperAbility.hasAbilityFromItemStack(Unsaga.abilityManager.tomahawk, par1ItemStack)){
			return 72000;
		}
		return 0;
	}




	//thanx:http://forum.minecraftuser.jp/viewtopic.php?f=21&t=9494&start=300
	@Override
    public Multimap getItemAttributeModifiers()
    {
        return helper.getItemAttributeModifiers(field_111210_e, weapondamage);
    }
    

	@Override
	public void registerIcons(IIconRegister par1IconRegister)
	{
		this.itemIcon = par1IconRegister.registerIcon(Unsaga.DOMAIN+":"+"axe");
		this.icons[0] = par1IconRegister.registerIcon(Unsaga.DOMAIN+":"+"axe_1");
		this.icons[1] = par1IconRegister.registerIcon(Unsaga.DOMAIN+":"+"axe_2");
	}

	@Override
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}

}
